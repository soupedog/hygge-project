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

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

/**
 * @author Xavier
 * @date 2026/7/2
 */
public class DefaultHyggeJobReporter implements HyggeJobReporter {
    /**
     * 批次的报告信息，列表信息的每个元素代表一个批次的汇总的摘要信息。(批次之间是相互同步执行的)
     */
    protected final List<String> jobBatchInfoList;
    /**
     * 流程追踪信息。(最小执行单元可能存在并发，必须确保多线程并发写操作不丢数据)
     */
    protected final ConcurrentLinkedQueue<TrackingItem> trackingInfoQueue;
    /**
     * 异常信息。(最小执行单元可能存在并发，必须确保多线程并发写操作不丢数据)
     */
    protected final ConcurrentLinkedQueue<Object> failedInfoQueue;

    public DefaultHyggeJobReporter(List<String> jobBatchInfoList, ConcurrentLinkedQueue<TrackingItem> trackingInfoQueue, ConcurrentLinkedQueue<Object> failedInfoQueue) {
        this.failedInfoQueue = failedInfoQueue;
        this.trackingInfoQueue = trackingInfoQueue;
        this.jobBatchInfoList = jobBatchInfoList;
    }

    @Override
    public Map<String, Object> createReportInfo(HyggeJobContext context) {
        LinkedHashMap<String, Object> logInfo = new LinkedHashMap<>();

        String title = context.getTitle();

        if (title != null && !title.isEmpty()) {
            logInfo.put("title", title);
        }

        logInfo.put("batchInfo", jobBatchInfoList);

        if (!trackingInfoQueue.isEmpty()) {
            // 按时间戳从早到晚排序
            List<TrackingItem> trackingInfo = trackingInfoQueue.stream()
                    .sorted(Comparator.comparingLong(TrackingItem::getTs))
                    .collect(Collectors.toList());

            logInfo.put("trackingInfo", trackingInfo);
        }

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
    public void addProcessTrackingInfo(long ts, String trackingInfo) {
        trackingInfoQueue.add(new TrackingItem(ts, trackingInfo));
    }

    @Override
    public void addFailureInfo(Object failedInfo) {
        failedInfoQueue.add(failedInfo);
    }

    public static class TrackingItem {
        private long ts;
        private String info;

        public TrackingItem(long ts, String info) {
            this.info = info;
            this.ts = ts;
        }

        public long getTs() {
            return ts;
        }

        public void setTs(long ts) {
            this.ts = ts;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }
}
