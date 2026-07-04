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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Xavier
 * @date 2026/7/2
 */
public class DefaultHyggeJobReporter implements HyggeJobReporter {
    protected final List<String> jobBatchInfoList;
    protected final ConcurrentLinkedQueue<Object> failedInfoQueue;

    public DefaultHyggeJobReporter(List<String> jobBatchInfoList, ConcurrentLinkedQueue<Object> failedInfoQueue) {
        this.jobBatchInfoList = jobBatchInfoList;
        this.failedInfoQueue = failedInfoQueue;
    }

    @Override
    public Map<String, Object> createReportInfo(HyggeJobContext context) {
        LinkedHashMap<String, Object> logInfo = new LinkedHashMap<>();

        String title = context.getTitle();

        if (title != null && !title.isEmpty()) {
            logInfo.put("title", title);
        }

        logInfo.put("batchInfo", jobBatchInfoList);

        if (!failedInfoQueue.isEmpty()) {
            logInfo.put("failedInfos", failedInfoQueue);
        } else {
            logInfo.put("totalItem", context.getItemCount().get());
        }

        logInfo.put("totalBatch", context.getActualTotalBatch());

        long cost = System.currentTimeMillis() - context.getStartTs();

        logInfo.put("cost", cost);

        return logInfo;
    }

    @Override
    public void addBatchInfo(String batchInfo) {
        jobBatchInfoList.add(batchInfo);
    }

    @Override
    public void addFailedInfo(Object failedInfo) {
        failedInfoQueue.add(failedInfo);
    }
}
