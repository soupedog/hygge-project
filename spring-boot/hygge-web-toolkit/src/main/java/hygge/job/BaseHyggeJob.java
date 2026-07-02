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
import hygge.util.UtilCreator;
import hygge.util.definition.JsonHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Xavier
 * @date 2026/7/2
 */
public abstract class BaseHyggeJob<JT extends BaseHyggeJobItem<S, IUI>, S, IUI> {
    private static final Logger log = LoggerFactory.getLogger(BaseHyggeJob.class);
    private static final JsonHelper<ObjectMapper> jsonHelper_indent = UtilCreator.INSTANCE.getDefaultJsonHelperInstance(true);
    /**
     * 默认的 批次编号，从 1 开始
     */
    protected int defaultBatchCount = 1;
    /**
     * 默认的 单批次拉取多少最小执行单元
     */
    protected int defaultBatchSize;
    /**
     * 执行报告
     */
    protected JobReport<IUI> jobReport;
    /**
     * 为 true 时，单批次内的最小单元互相会异步执行，单批次内所有最小单元执行完成后才可能进入下一个批次
     */
    protected boolean bachAsynchronousEnable;

    protected BaseHyggeJob(String title, int defaultBatchSize, boolean bachAsynchronousEnable) {
        this.defaultBatchSize = defaultBatchSize;
        this.bachAsynchronousEnable = bachAsynchronousEnable;

        jobReport = new JobReport<>(title, new ConcurrentLinkedQueue<>());
    }

    /**
     * 获取名称，用于自动日志记录
     */
    protected abstract String getJobName();

    public void execute() {
        HyggeJobContext context = new HyggeJobContext();
        try {
            mainProcess(context);
        } catch (Throwable throwable) {
            // 兜底包括 JVM 层面也不放过
            ultimateThrowableHook(context, throwable);
        }
    }

    protected void mainProcess(HyggeJobContext context) {
        try {
            initHook(context);

            Collection<JT> batchItemContainer = firstFetch(context);
            long batchStartTs;

            while (batchItemContainer != null && !batchItemContainer.isEmpty()) {
                batchStartTs = System.currentTimeMillis();

                batchInitHook(context, batchStartTs, batchItemContainer);

                if (bachAsynchronousEnable) {
                    asynchronousProcess(batchItemContainer, context);
                } else {
                    for (JT item : batchItemContainer) {
                        executeSingleItem(context, item);
                    }
                }

                batchCompleteHook(context, batchStartTs, batchItemContainer);

                context.batchContIncrease();
                batchItemContainer = getNextBatch(context);
            }
        } catch (Exception exception) {
            handleException(context, exception);
        } finally {
            finallyHook(context);
            printLog(context);
        }
    }

    protected void initHook(HyggeJobContext context) {
        context.setBatchCount(defaultBatchCount);
        context.setBatchSize(defaultBatchSize);
    }

    /**
     * 全局中发生异常的处理机制
     *
     * @param exception 运行过程中抛出的异常
     */
    protected void handleException(HyggeJobContext context, Exception exception) {
        String loginInfo = getJobName() + " unexpected exception.";
        log.error(loginInfo, exception);
    }

    /**
     * 无论成功与否，必然会在最后执行的钩子函数，默认什么也不做。
     */
    protected void finallyHook(HyggeJobContext context) {
        // 默认什么也不做，给子类提供一个钩子函数
    }

    protected Map<String, Object> getLogInfo(HyggeJobContext context) {
        return jobReport.createReportInfo(context);
    }

    protected void printLog(HyggeJobContext context) {
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
    protected abstract Collection<JT> firstFetch(HyggeJobContext context);

    /**
     * 返回 null 时，任务会自动结束
     */
    protected abstract Collection<JT> getNextBatch(HyggeJobContext context);

    protected abstract void handleSingleItem(HyggeJobContext context, JT jobItem);

    /**
     * 执行中，单个数据执行发生异常的处理机制
     *
     * @param jobItem   引发异常的待处理数据
     * @param exception 运行过程中抛出的异常
     */
    protected void handleExceptionForItem(HyggeJobContext context, JT jobItem, Exception exception) {
        // 默认用户会在 JobItem 的 createReportAfterStop 中输出日志，该方法不会有额外操作
    }

    protected void executeSingleItem(HyggeJobContext context, JT jobItem) {
        long startTs = System.currentTimeMillis();
        try {
            jobItem.setStartTs(startTs);

            handleSingleItem(context, jobItem);
            context.itemCountIncrease();
        } catch (Exception exception) {
            jobItem.setException(exception);
            handleExceptionForItem(context, jobItem, exception);
        } finally {
            try {
                finallyHookForItem(context, jobItem);

                long cost = System.currentTimeMillis() - startTs;

                JobReportItem<IUI> reportItem = jobItem.createReportAfterStop(context, cost);
                if (reportItem != null) {
                    reportItem.setBatchCount(context.getBatchCount());
                    jobReport.addReportItem(reportItem);
                }
            } catch (Throwable throwable) {
                ultimateThrowableHook(context, throwable);
            }
        }
    }

    /**
     * 无论成功与否，必然会单个数据处理完成后执行的钩子函数，默认什么也不做。
     */
    protected void finallyHookForItem(HyggeJobContext context, JT jobItem) {
        // 默认什么也不做，给子类提供一个钩子函数
    }

    protected void asynchronousProcess(Collection<JT> batchItemContainer, HyggeJobContext context) {
        CompletableFuture<?>[] all = new CompletableFuture[batchItemContainer.size()];

        int index = 0;
        for (JT item : batchItemContainer) {
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> executeSingleItem(context, item))
                    .exceptionally(throwable -> {
                        ultimateThrowableHook(context, throwable);
                        // 自动转换 Void
                        return null;
                    });
            all[index] = future;
            index = index + 1;
        }

        // 阻塞回整个批次的执行
        CompletableFuture.allOf(all).join();
    }

    protected void batchInitHook(HyggeJobContext context, long batchStartTs, Collection<JT> batchItemContainer) {
        // 批次开始钩子函数，默认什么也不干
    }

    protected void batchCompleteHook(HyggeJobContext context, long batchStartTs, Collection<JT> batchItemContainer) {
        long batchCost = System.currentTimeMillis() - batchStartTs;

        jobReport.addBatchInfo(String.format("batch:%d itemSize:%d cost:%d",
                context.getBatchCount(),
                batchItemContainer.size(),
                batchCost
        ));
    }

    /**
     * 最终的异常处理器，这是最后一道防线，该方法严禁抛出异常。<br/>
     * <p>
     * 默认实现是单纯打印日志。
     */
    protected void ultimateThrowableHook(HyggeJobContext context, Throwable throwable) {
        String logInfo = getJobName() + " unexpected error.";
        log.error(logInfo, throwable);
    }

}
