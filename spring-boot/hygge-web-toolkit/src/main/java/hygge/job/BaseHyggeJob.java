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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * @param <C>   上下文容器，所有执行单元共享。
 * @param <JBI> 批次容器，同批次内执行单元共享。
 * @param <JI>  最小执行单元容器，JI 内部泛型是 DF 处理过后的数据类型。
 * @param <RD>  待处理的原始数据。
 * @param <PD>  处理完成后的数据。
 * @author Xavier
 * @date 2026/7/2
 */
public abstract class BaseHyggeJob<
        C extends HyggeJobContext,
        JBI extends DefaultHyggeJobBatchItem<JI>,
        JI extends BaseHyggeJobItem<RD, PD, ?>,
        RD,
        PD> {
    private static final Logger log = LoggerFactory.getLogger(BaseHyggeJob.class);
    private static final JsonHelper<ObjectMapper> jsonHelper_indent = UtilCreator.INSTANCE.getDefaultJsonHelperInstance(true);
    private static final JsonHelper<ObjectMapper> jsonHelper = UtilCreator.INSTANCE.getDefaultJsonHelperInstance(false);
    /**
     * 默认的 批次编号，从 1 开始
     */
    protected int defaultBatchCount = 1;
    /**
     * 默认的 单批次拉取多少最小执行单元
     */
    protected int defaultBatchSize;
    /**
     * 为 true 时，单批次内的最小单元互相会异步执行，单批次内所有最小单元执行完成后才可能进入下一个批次
     */
    protected boolean bachAsynchronousEnable;

    protected BaseHyggeJob(int defaultBatchSize, boolean bachAsynchronousEnable) {
        this.defaultBatchSize = defaultBatchSize;
        this.bachAsynchronousEnable = bachAsynchronousEnable;
    }

    /**
     * 获取名称，用于自动日志记录
     */
    protected abstract String getJobName();

    public void execute() {
        C context = null;
        try {
            context = createContext();
            context.setBatchSize(defaultBatchSize);
            context.setBatchCount(defaultBatchCount);
            HyggeJobReporter jobReporter = createHyggeJobReporter(context);
            context.setJobReporter(jobReporter);
            mainProcess(context);
        } catch (Throwable throwable) {
            // 兜底包括 JVM 层面也不放过
            ultimateThrowableHook(context, throwable);
        }
    }

    protected void mainProcess(C context) {
        try {
            JBI jobBatchItem = createJobBatchItem(context);
            jobBatchItem.initStartTs();
            jobBatchItem.setBatchCount(context.getBatchCount());
            boolean isFirstBatch = true;

            List<RD> rawDataCollection = firstFetch(context, jobBatchItem);
            List<PD> processedDataCollection;
            batchInitHook(context, jobBatchItem, rawDataCollection);

            while (rawDataCollection != null && !rawDataCollection.isEmpty()) {
                if (isFirstBatch) {
                    isFirstBatch = false;
                } else {
                    // 仅当不是第 1 批次时需要创建
                    jobBatchItem = createJobBatchItem(context);
                    jobBatchItem.initStartTs();
                    jobBatchItem.setBatchCount(context.getBatchCount());
                    batchInitHook(context, jobBatchItem, rawDataCollection);
                }

                List<JI> jobItemList = pressToJobItem(context, jobBatchItem, rawDataCollection);
                jobBatchItem.setJobItemCollection(jobItemList);

                if (bachAsynchronousEnable) {
                    processedDataCollection = asynchronousProcess(context, jobBatchItem, jobItemList);
                } else {
                    processedDataCollection = new ArrayList<>();
                    for (JI item : jobItemList) {
                        PD jobItemProcessedData = executeSingleItem(context, item);
                        jobItemSuccessCheck(item);

                        processedDataCollection.add(jobItemProcessedData);
                    }
                }

                batchCompleteHook(context, jobBatchItem, rawDataCollection, processedDataCollection);
                jobBatchItem.stop();
                //当前批次结束
                context.batchContIncrease();

                rawDataCollection = getNextBatch(context, jobBatchItem);
            }
        } catch (Exception exception) {
            handleException(context, exception);
        } finally {
            finallyHook(context);
            printLog(context);
        }
    }

    protected HyggeJobReporter createHyggeJobReporter(C context) {
        return new DefaultHyggeJobReporter(new ArrayList<>(context.getBatchSize()), new ConcurrentLinkedQueue<>());
    }

    protected abstract C createContext();

    protected abstract JBI createJobBatchItem(C context);

    /**
     * 全局中发生异常的处理机制
     *
     * @param exception 运行过程中抛出的异常
     */
    protected void handleException(C context, Exception exception) {
        String loginInfo = getJobName() + " unexpected exception.";
        log.error(loginInfo, exception);
    }

    /**
     * 无论成功与否，必然会在最后执行的钩子函数，默认什么也不做。
     */
    protected void finallyHook(C context) {
        // 默认什么也不做，给子类提供一个钩子函数
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
     * 返回 null 时，任务会自动结束
     */
    protected abstract List<RD> firstFetch(C context, JBI jobBatchItem);

    /**
     * 返回 null 时，任务会自动结束
     */
    protected abstract List<RD> getNextBatch(C context, JBI jobBatchItem);

    protected abstract JI createJobItem(C context, JBI jobBatchItem, RD rawData);

    protected abstract PD handleSingleItem(C context, JI jobItem);

    /**
     * 执行中，单个数据执行发生异常的处理机制
     *
     * @param jobItem   引发异常的待处理数据
     * @param exception 运行过程中抛出的异常
     */
    protected void handleExceptionForItem(C context, JI jobItem, Exception exception) {
        // 默认什么也不干
    }

    protected PD executeSingleItem(C context, JI jobItem) {
        PD result = null;
        try {
            jobItem.initStartTs();

            result = handleSingleItem(context, jobItem);
            jobItem.setProcessedData(result);
            context.itemCountIncrease();
        } catch (Exception exception) {
            jobItem.setException(exception);
            context.getJobReporter().addFailedInfo(jobItem.getErrorInfo());
            handleExceptionForItem(context, jobItem, exception);
        } finally {
            try {
                finallyHookForItem(context, jobItem);

                jobItem.stop();
            } catch (Throwable throwable) {
                ultimateThrowableHook(context, throwable);
            }
        }
        return result;
    }

    /**
     * 无论成功与否，必然会单个数据处理完成后执行的钩子函数，默认什么也不做。
     */
    protected void finallyHookForItem(C context, JI jobItem) {
        // 默认什么也不做，给子类提供一个钩子函数
    }

    protected List<PD> asynchronousProcess(C context, JBI jobBatchItem, Collection<JI> batchItemContainer) {
        List<CompletableFuture<PD>> futures = batchItemContainer.stream()
                .map(jobItem -> CompletableFuture.supplyAsync(() -> executeSingleItem(context, jobItem)))
                .collect(Collectors.toList());

        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0])
        );

        allFutures.join();

        jobItemSuccessCheck(batchItemContainer);

        return futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    protected void jobItemSuccessCheck(JI jobItem) {
        if (jobItem.isFailure()) {
            throw new InternalRuntimeException(getJobName() + " sub-task(" + jobItem.getUniqueIdentifier() + ") were failed.");
        }
    }

    protected void jobItemSuccessCheck(Collection<JI> batchItemContainer) {
        List<Object> jobItemIdList = batchItemContainer
                .stream()
                .filter(BaseHyggeJobItem::isFailure)
                .map(BaseHyggeJobItem::getUniqueIdentifier)
                .collect(Collectors.toList());

        if (!jobItemIdList.isEmpty()) {
            throw new InternalRuntimeException(getJobName() + " sub-task(" + jsonHelper.formatAsString(jobItemIdList) + ") were failed.");
        }
    }

    protected void batchInitHook(C context, JBI jobBatchItem, List<RD> rawDataCollection) {
        // 批次开始钩子函数，默认什么也不干
    }

    protected void batchCompleteHook(C context, JBI jobBatchItem, List<RD> rawDataCollection, List<PD> processedDataCollection) {
        long batchCost = System.currentTimeMillis() - jobBatchItem.getStartTs();

        context.getJobReporter().addBatchInfo(String.format("batch:%d itemSize:%d cost:%d",
                context.getBatchCount(),
                jobBatchItem.getJobItemCollection().size(),
                batchCost
        ));
    }

    /**
     * 最终的异常处理器，这是最后一道防线，该方法严禁抛出异常。默认实现是单纯打印日志。
     *
     * @param context 可能为空，仅在 {@link BaseHyggeJob#createContext()} ()} 异常或无返回值时为 null，
     */
    protected void ultimateThrowableHook(C context, Throwable throwable) {
        String logInfo = getJobName() + " unexpected error.";
        log.error(logInfo, throwable);
    }

    protected List<JI> pressToJobItem(C context, JBI jobBatchItem, Collection<RD> rawDataCollection) {
        return rawDataCollection.stream()
                .map(raw -> {
                    JI jobItem = createJobItem(context, jobBatchItem, raw);
                    jobItem.setBatchCount(jobBatchItem.getBatchCount());
                    return jobItem;
                })
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
