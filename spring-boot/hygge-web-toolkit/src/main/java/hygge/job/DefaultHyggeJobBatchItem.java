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

import java.util.Collection;

/**
 * @author Xavier
 * @date 2026/7/3
 */
public class DefaultHyggeJobBatchItem<JI extends BaseHyggeJobItem<?, ?, ?>> extends JobTimeInfo {
    /**
     * 批次执行编号
     */
    protected int batchCount;
    /**
     * 当前批次下的 JobItem
     */
    protected Collection<JI> jobItemCollection;

    /**
     * 当前批次内均成功
     */
    public boolean isSuccess() {
        return this.jobItemCollection.stream().allMatch(BaseHyggeJobItem::isSuccess);
    }

    /**
     * 当前批次至少有一个执行失败
     */
    public boolean hasFailed() {
        return this.jobItemCollection.stream().anyMatch(BaseHyggeJobItem::isFailure);
    }

    public Collection<JI> getJobItemCollection() {
        return jobItemCollection;
    }

    public void setJobItemCollection(Collection<JI> jobItemCollection) {
        this.jobItemCollection = jobItemCollection;
    }

    public int getBatchCount() {
        return batchCount;
    }

    public void setBatchCount(int batchCount) {
        this.batchCount = batchCount;
    }
}
