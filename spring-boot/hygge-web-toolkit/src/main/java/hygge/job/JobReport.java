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

import org.springframework.util.LinkedMultiValueMap;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Xavier
 * @date 2026/7/2
 */
public class JobReport<IUI> {
    protected final String title;
    protected final ConcurrentLinkedQueue<String> batchInfoQueue;
    protected final ConcurrentLinkedQueue<JobReportItem<IUI>> reportItemQueue;

    public JobReport(String title, ConcurrentLinkedQueue<JobReportItem<IUI>> reportItemQueue) {
        this.title = title;
        this.reportItemQueue = reportItemQueue;
        batchInfoQueue = new ConcurrentLinkedQueue<>();
    }

    public Map<String, Object> createReportInfo(HyggeJobContext context) {
        LinkedMultiValueMap<Integer, JobReportItem<IUI>> reportInfo = new LinkedMultiValueMap<>();

        AtomicBoolean noFail = new AtomicBoolean(true);

        reportItemQueue.forEach(item -> {
            reportInfo.add(item.batchCount, item);
            if (noFail.get() && item.isFail != null && item.isFail) {
                noFail.set(false);
            }
        });

        LinkedHashMap<String, Object> logInfo = new LinkedHashMap<>();

        if (title != null && !title.isEmpty()) {
            logInfo.put("title", title);
        }

        logInfo.put("batchInfo", batchInfoQueue);

        if (!reportInfo.isEmpty()) {
            logInfo.put("detail", reportInfo);
        }

        // 批次出循环时默认会 +1 此处用来抵消
        logInfo.put("totalBatch", context.batchCount - 1);

        if (noFail.get()) {
            logInfo.put("totalItem", context.getItemCount().get());
        }

        long cost = System.currentTimeMillis() - context.startTs;

        logInfo.put("cost", cost);

        return logInfo;
    }

    public String getTitle() {
        return title;
    }

    public void addBatchInfo(String info) {
        batchInfoQueue.add(info);
    }

    public void addReportItem(JobReportItem<IUI> item) {
        reportItemQueue.add(item);
    }

    public ConcurrentLinkedQueue<String> getBatchInfoQueue() {
        return batchInfoQueue;
    }

    public ConcurrentLinkedQueue<JobReportItem<IUI>> getReportItemQueue() {
        return reportItemQueue;
    }
}
