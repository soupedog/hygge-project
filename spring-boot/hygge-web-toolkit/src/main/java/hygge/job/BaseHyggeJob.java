/*
 * Copyright 2022-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hygge.job;

import com.fasterxml.jackson.databind.ObjectMapper;
import hygge.commons.exception.InternalRuntimeException;
import hygge.util.UtilCreator;
import hygge.util.definition.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * 如果不需要扩展 JobBatchItem，直接继承 {@link SimpleHyggeJob} 即可。
 * <p>
 * 大致执行逻辑：<br/>
 * <p>
 * 1.firstFetch、getNextBatch 扫描数据库数据，一次拉取对应了一个批次
 * <p>
 * 2.处理扫描到的数据，如果 fetch 返回了空 List，那么终止任务
 * <p>
 * 3.进行简要的日志打印
 * <p>
 * 得到的特性：<br/>
 * 1.调整 bachAsynchronousEnable 参数值就可便捷实现同批次数据进行异步处理。
 * 2.最小单元异步处理时，仅当同批次所有数据处理完成才可能进入下一批次。<br/>
 * 每个批次结束可以在 {@link BaseHyggeJob#batchCompleteHook} 中对处理后的数据进行集中处理，这是批量进行数据持久化的好位点。<br/>
 * 3.简要的执行报告，会统计批次耗时与 Job 总耗时，以及错误摘要等信息。默认情况下，执行中发生的异常会被完整输出到日志系统。
 *
 * @param <C>   上下文容器，所有执行单元共享。
 * @param <JBI> 批次容器，同批次内执行单元共享。
 * @param <JI>  最小执行单元容器，JI 内部包含 RD 和 PD。
 * @param <RD>  待处理的原始数据。
 * @param <PD>  处理完成后的数据。
 * @author Xavier
 * @date 2026/7/2
 */
public abstract class BaseHyggeJob<
        C extends HyggeJobContext,
        JBI extends HyggeJobBatchItem<JI>,
        JI extends BaseHyggeJobItem<RD, PD, ?>,
        RD,
        PD> {
    private static final Logger log = LoggerFactory.getLogger(BaseHyggeJob.class);
    private static final JsonHelper<ObjectMapper> jsonHelper_indent = UtilCreator.INSTANCE.getDefaultJsonHelperInstance(true);
    private static final JsonHelper<ObjectMapper> jsonHelper = UtilCreator.INSTANCE.getDefaultJsonHelperInstance(false);
    /**
     * 初始批次编号，从 1 开始
     */
    protected int initialBatchCount = 1;

    /**
     * 获取名称，用于自动日志记录
     */
    protected abstract String getJobName();

    /**
     * 开始执行 Job。<br/>
     * <p>
     * 这是语法糖，会自行调用 {@link BaseHyggeJob#createContext()} 作为初始上下文，请确保，创建方法已初始了必要参数。
     *
     */
    public C execute() {
        return execute(null);
    }

    /**
     * 开始执行 Job。
     */
    public C execute(C inputContext) {
        C context = null;

        try {
            context = mainProcess(inputContext);
        } catch (Throwable throwable) {
            ultimateThrowableHook(context, throwable);
        }
        return context;
    }

    protected C mainProcess(C context) {
        try {
            if (context == null) {
                context = createContext();
            }
            // 任务开始扩展点
            jobStartHook(context);

            // 配置项合法性检测
            context.initConfigurationCheck();
            // 初始化批次编号
            context.setBatchCount(initialBatchCount);

            // 初始化执行报告器
            HyggeJobReporter jobReporter = createHyggeJobReporter(context);
            context.setJobReporter(jobReporter);

            // 初始化批次对象
            JBI jobBatchItem = createJobBatchItem(context);
            initBatchItem(context, jobBatchItem);

            boolean isFirstBatch = true;

            List<RD> rawDataList = firstFetch(context, jobBatchItem);
            List<PD> processedDataList;
            batchStartHook(context, jobBatchItem, rawDataList);

            while (rawDataList != null && !rawDataList.isEmpty()) {
                if (isFirstBatch) {
                    isFirstBatch = false;
                } else {
                    // 仅当不是第 1 批次时需要创建
                    jobBatchItem = createJobBatchItem(context);
                    initBatchItem(context, jobBatchItem);
                    batchStartHook(context, jobBatchItem, rawDataList);
                }

                List<JI> jobItemList = pressToJobItem(context, jobBatchItem, rawDataList);
                jobBatchItem.setJobItemList(jobItemList);

                if (context.isBachAsynchronousEnable()) {
                    processedDataList = asynchronousProcess(context, jobBatchItem, jobItemList);
                } else {
                    processedDataList = new ArrayList<>(context.getBatchSize());
                    for (JI item : jobItemList) {
                        PD jobItemProcessedData = executeSingleItem(context, item);
                        jobItemSuccessCheck(context, item);

                        processedDataList.add(jobItemProcessedData);
                    }
                }

                batchCompleteHook(context, jobBatchItem, rawDataList, processedDataList);
                jobBatchItem.stop();
                //当前批次结束
                context.batchContIncrease();

                rawDataList = getNextBatch(context, jobBatchItem);
            }
        } catch (Exception exception) {
            handleException(context, exception);
        } finally {
            // 任务结束扩展点
            finallyHook(context);
            printLog(context);
        }

        return context;
    }

    /**
     * 全局中发生异常的处理机制，原则上这个方法不能再抛出异常。
     *
     * @param context   可能为空
     * @param exception 运行过程中抛出的异常
     */
    protected void handleException(C context, Exception exception) {
        // 最小执行单元处理过异常时，状态会被置为 FAILURE，防止同一条错误重复汇报
        if (context != null && !context.getStatus().equals(JobStatusEnum.FAILURE)) {
            context.setStatus(JobStatusEnum.FAILURE);
            LinkedHashMap<String, Object> exceptionInfo = new LinkedHashMap<>();
            // 单元之外的整个运行上下文（调度、线程池、资源等）
            exceptionInfo.put("scope", "context");
            exceptionInfo.put("batchCount", context.getBatchCount());
            exceptionInfo.put("message", exception.getMessage());
            context.getJobReporter().addFailureInfo(exceptionInfo);
        }

        String loginInfo = getJobName() + " unexpected exception.";
        log.error(loginInfo, exception);
    }

    /**
     * 确认 context 对象非空后，{@link HyggeJobContext#initConfigurationCheck()} 调用前执行的钩子函数，等效于任务的最开端。
     */
    protected void jobStartHook(C context) {
        // 默认什么都不做
    }

    /**
     * 无论成功与否，必然会在最后执行的钩子函数，默认什么也不做。
     */
    protected void finallyHook(C context) {
        // 默认什么都不做
    }

    protected Map<String, Object> getLogInfo(C context) {
        return context.getJobReporter().createReportInfo(context);
    }

    protected void printLog(C context) {
        Map<String, Object> logInfo = getLogInfo(context);

        boolean isSuccess = logInfo.containsKey("totalItem");

        String jsonInfo = jsonHelper_indent.formatAsString(logInfo);

        if (isSuccess) {
            log.info("{} execute success:{}", getJobName(), jsonInfo);
        } else {
            log.warn("{} execute fail:{}", getJobName(), jsonInfo);
        }
    }

    /**
     * 如果无扩展需求，可以直接创建 {@link HyggeJobContext}，无需创建自定义 Context 类。
     */
    public abstract C createContext();

    /**
     * 如果无扩展需求，可以直接创建 {@link HyggeJobBatchItem}，无需创建自定义 HyggeJobBatchItem 类。
     */
    protected abstract JBI createJobBatchItem(C context);

    protected void initBatchItem(C context, JBI jobBatchItem) {
        jobBatchItem.initStartTs();
        jobBatchItem.setBatchCount(context.getBatchCount());
    }

    /**
     * 初次拉取待处理的数据，该方法最多执行一次，后续改为循环调用 {@link BaseHyggeJob#getNextBatch(HyggeJobContext, HyggeJobBatchItem)}。<br/>
     * <p>
     * 仅当返回 null 或 空列表 时，任务会自动结束。
     */
    protected abstract List<RD> firstFetch(C context, JBI jobBatchItem);

    /**
     * 上一个批次任务结束后会循环调用该方法。<br/>
     * 仅当返回 null 或 空列表 时，任务会自动结束。
     */
    protected abstract List<RD> getNextBatch(C context, JBI jobBatchItem);

    /**
     * 将待处理数据包装成任务最小执行单元。
     */
    protected abstract JI createJobItem(C context, JBI jobBatchItem, RD rawData);

    protected abstract PD handleSingleItem(C context, JI jobItem);

    /**
     * 最小执行单元中发生异常的处理机制，原则上这个方法不能再抛出异常。
     *
     * @param context   可能为空
     * @param jobItem   引发异常的待处理数据
     * @param exception 运行过程中抛出的异常
     */
    protected void handleExceptionForItem(C context, JI jobItem, Exception exception) {
        jobItem.setException(exception);
        context.getJobReporter().addFailureInfo(jobItem.getErrorInfo());

        String loginInfo = getJobName() + " unexpected exception(in unit).";
        log.error(loginInfo, exception);
    }

    protected PD executeSingleItem(C context, JI jobItem) {
        PD result = null;
        try {
            jobItem.initStartTs();
            result = handleSingleItem(context, jobItem);
            jobItem.setProcessedData(result);
            // 成功执行才 + 1
            context.itemCountIncrease();
        } catch (Exception exception) {
            handleExceptionForItem(context, jobItem, exception);
        } finally {
            try {
                finallyHookForItem(context, jobItem);
            } catch (Exception exception) {
                handleExceptionForItem(context, jobItem, exception);
            } finally {
                jobItem.stop();
            }
        }
        return result;
    }

    /**
     * 无论成功与否，最小执行单元执行结束后会执行的钩子函数。
     */
    protected void finallyHookForItem(C context, JI jobItem) {
        // 默认什么也不做
    }

    protected List<PD> asynchronousProcess(C context, JBI jobBatchItem, List<JI> batchItemContainer) {
        List<CompletableFuture<PD>> futures = batchItemContainer.stream()
                .map(jobItem -> CompletableFuture.supplyAsync(() -> executeSingleItem(context, jobItem)))
                .collect(Collectors.toList());

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        );

        allFutures.join();

        jobItemSuccessCheck(context, batchItemContainer);

        return futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    protected void jobItemSuccessCheck(C contex, JI jobItem) {
        if (jobItem.isFailure()) {
            contex.setStatus(JobStatusEnum.FAILURE);
            throw new InternalRuntimeException(getJobName() + " sub-task(" + jobItem.getUniqueIdentifier() + ") was failed.");
        }
    }

    protected void jobItemSuccessCheck(C contex, List<JI> batchItemContainer) {
        List<Object> jobItemIdList = batchItemContainer
                .stream()
                .filter(BaseHyggeJobItem::isFailure)
                .map(BaseHyggeJobItem::getUniqueIdentifier)
                .collect(Collectors.toList());

        if (!jobItemIdList.isEmpty()) {
            contex.setStatus(JobStatusEnum.FAILURE);
            throw new InternalRuntimeException(getJobName() + " sub-task(" + jsonHelper.formatAsString(jobItemIdList) + ") were failed.");
        }
    }

    /**
     * 每个批次开始时都会执行的钩子函数。
     */
    protected void batchStartHook(C context, JBI jobBatchItem, List<RD> rawDataList) {
        // 默认什么也不干
    }

    /**
     * 每个批次结束时都会执行的钩子函数。
     */
    protected void batchCompleteHook(C context, JBI jobBatchItem, List<RD> rawDataList, List<PD> processedDataList) {
        long batchCost = System.currentTimeMillis() - jobBatchItem.getStartTs();

        context.getJobReporter().addBatchInfo(String.format("batch:%d itemSize:%d cost:%d",
                context.getBatchCount(),
                jobBatchItem.getJobItemList().size(),
                batchCost
        ));
    }

    /**
     * 最终的异常处理器，这是最后一道防线，该方法严禁抛出异常。默认实现是单纯打印日志。
     *
     * @param context 可能为空，仅在 {@link BaseHyggeJob#createContext()} ()} 异常或无返回值时为 null，
     */
    protected void ultimateThrowableHook(C context, Throwable throwable) {
        if (context != null) {
            context.setStatus(JobStatusEnum.FAILURE);
        }

        String logInfo = getJobName() + " unexpected error.";
        log.error(logInfo, throwable);
    }

    protected List<JI> pressToJobItem(C context, JBI jobBatchItem, List<RD> rawDataList) {
        return rawDataList.stream()
                .map(raw -> {
                    JI jobItem = createJobItem(context, jobBatchItem, raw);
                    jobItem.setBatchCount(jobBatchItem.getBatchCount());
                    return jobItem;
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }

    protected HyggeJobReporter createHyggeJobReporter(C context) {
        return new DefaultHyggeJobReporter(new ArrayList<>(context.getBatchSize()), new ConcurrentLinkedQueue<>(), new ConcurrentLinkedQueue<>());
    }
}
