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

/**
 * 任务执行时，最小单元数据的包装类
 *
 * @author Xavier
 * @date 2026/7/2
 */
public abstract class BaseHyggeJobItem<T, UI> {
    /**
     * 该最小单元执行的开始时间
     */
    protected long startTs;
    /**
     * 原始数据
     */
    protected T source;
    /**
     * 当前原始数据处理过程中遇到的异常
     */
    protected Exception exception;
    /**
     * 当前数据处理耗时(毫秒)
     */
    protected long cost;

    protected BaseHyggeJobItem(T source) {
        this.source = source;
    }

    /**
     * 如果返回为 null，讲不会汇总到 Job 的 jobReport 中
     *
     * @param cost 当前最小单元执行的耗时(毫秒)
     */
    public abstract JobReportItem<UI> createReportAfterStop(HyggeJobContext context, long cost);

    public long getStartTs() {
        return startTs;
    }

    public void setStartTs(long startTs) {
        this.startTs = startTs;
    }

    public T getSource() {
        return source;
    }

    public void setSource(T source) {
        this.source = source;
    }


    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }
}
