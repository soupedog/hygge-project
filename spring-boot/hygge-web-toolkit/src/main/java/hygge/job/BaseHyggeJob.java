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
import org.springframework.util.LinkedMultiValueMap;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Xavier
 * @date 2026/7/2
 */
public abstract class BaseHyggeJob<JT extends BaseHyggeJobItem<S, IUI>, S, IUI> {
    private static final Logger log = LoggerFactory.getLogger(BaseHyggeJob.class);
    private static final JsonHelper<ObjectMapper> jsonHelper = UtilCreator.INSTANCE.getDefaultJsonHelperInstance(false);
    /**
     * 批次编号，从 1 开始
     */
    protected int batchCount = 1;
    protected int batchSize;
    protected AtomicInteger count = new AtomicInteger(0);
    protected JobReport<IUI> jobReport;
    /**
     * 为 true 时，单批次内的最小单元互相会异步执行，单批次内所有最小单元执行完成后才可能进入下一个批次
     */
    protected boolean bachAsynchronousEnable;

    protected BaseHyggeJob(int batchSize, boolean bachAsynchronousEnable) {
        this.batchSize = batchSize;
        this.bachAsynchronousEnable = bachAsynchronousEnable;

        jobReport = new JobReport<>();
    }

    /**
     * 获取名称，用于自动日志记录
     */
    protected abstract String getJobName();

    public void execute() {
        mainProcess();
    }

    protected void mainProcess() {
        HyggeJobContext context = new HyggeJobContext();

        try {
            initHook(context);

            Collection<JT> batchItemContainer = firstFetch(context);
            long batchStartTs;

            while (batchItemContainer != null && !batchItemContainer.isEmpty()) {
                batchStartTs = System.currentTimeMillis();
                if (bachAsynchronousEnable) {
                    asynchronousProcess(batchItemContainer, context);
                } else {
                    for (JT item : batchItemContainer) {
                        executeSingleItem(context, item);
                    }
                }

                batchCompleteHook(context, batchStartTs, batchItemContainer);

                context.batchIncrease();
                batchItemContainer = getNextBatch(context);
            }
        } catch (Throwable throwable) {
            handleThrowable(context, throwable);
        } finally {
            finallyHook(context);
            printLog(context);
        }
    }

    protected void initHook(HyggeJobContext context) {
        context.setBatchCount(batchCount);
        context.setBatchSize(batchSize);
    }

    /**
     * 全局中发生异常的处理机制
     *
     * @param throwable 运行过程中抛出的异常
     */
    protected void handleThrowable(HyggeJobContext context, Throwable throwable) {
        String loginInfo = " unexpected exception.";
        log.error(loginInfo, throwable);
    }

    /**
     * 无论成功与否，必然会在最后执行的钩子函数，默认什么也不做。
     */
    protected void finallyHook(HyggeJobContext context) {
        // 默认什么也不做，给子类提供一个钩子函数
    }

    protected void printLog(HyggeJobContext context) {
        LinkedMultiValueMap<Integer, JobReportItem<IUI>> reportInfo = new LinkedMultiValueMap<>();

        AtomicBoolean noFail = new AtomicBoolean(true);

        jobReport.queue.forEach(item -> {
            reportInfo.add(item.batchCount, item);
            if (noFail.get() && item.isFail != null && item.isFail) {
                noFail.set(false);
            }
        });

        LinkedHashMap<String, Object> logInfo = new LinkedHashMap<>();

        boolean hasFail = !noFail.get();

        if (!hasFail) {
            logInfo.put("total", count.get());
        }

        if (!reportInfo.isEmpty()) {
            logInfo.put("report", reportInfo);
        }

        long cost = System.currentTimeMillis() - context.startTs;

        logInfo.put("cost", cost);

        String jsonInfo = jsonHelper.formatAsString(logInfo);

        if (hasFail) {
            log.warn("{} execute fail:{}", getJobName(), jsonInfo);
        } else {
            log.info("{} execute success:{}", getJobName(), jsonInfo);
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
     * @param throwable 运行过程中抛出的异常
     */
    protected void handleThrowableForItem(HyggeJobContext context, JT jobItem, Throwable throwable) {
        // 默认用户会在 JobItem 的 createReportAfterStop 中输出日志，该方法不会有额外操作
    }

    protected void executeSingleItem(HyggeJobContext context, JT jobItem) {
        try {
            long startTs = System.currentTimeMillis();
            jobItem.setStartTs(startTs);

            handleSingleItem(context, jobItem);
            count.incrementAndGet();
        } catch (Throwable throwable) {
            jobItem.setThrowable(throwable);
            handleThrowableForItem(context, jobItem, throwable);
        } finally {
            finallyHookForItem(context, jobItem);

            jobItem.stop();
            JobReportItem<IUI> reportItem = jobItem.createReportAfterStop(context);
            if (reportItem != null) {
                reportItem.setBatchCount(context.getBatchCount());
                jobReport.add(reportItem);
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
            CompletableFuture<Void> future = CompletableFuture.runAsync(() -> executeSingleItem(context, item));
            all[index] = future;
            index = index + 1;
        }

        // 阻塞回整个批次的执行
        CompletableFuture.allOf(all).join();
    }

    protected void batchCompleteHook(HyggeJobContext context, long batchStartTs, Collection<JT> batchItemContainer) {
        long batchCost = System.currentTimeMillis() - batchStartTs;

        log.info("{} batch:{} itemSize:{} cost:{}",
                getJobName(),
                context.getBatchCount(),
                batchItemContainer.size(),
                batchCost);
    }

}
