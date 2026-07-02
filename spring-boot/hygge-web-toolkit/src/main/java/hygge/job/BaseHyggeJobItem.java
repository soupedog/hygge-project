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
     * 原始数据唯一标识
     */
    protected UI uniqueIdentifier;
    /**
     * 当前原始数据处理过程中遇到的异常
     */
    protected Throwable throwable;
    /**
     * 当前数据处理耗时(毫秒)
     */
    protected long cost;

    public void setStartTs(long startTs) {
        this.startTs = startTs;
    }

    protected BaseHyggeJobItem(T source, UI uniqueIdentifier) {
        this.source = source;
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public void stop() {
        this.cost = System.currentTimeMillis() - startTs;
    }

    /**
     * 如果返回为 null，讲不会汇总到 Job 的 jobReport 中
     */
    public abstract JobReportItem<UI> createReportAfterStop(HyggeJobContext context);

    public T getSource() {
        return source;
    }

    public void setSource(T source) {
        this.source = source;
    }

    public UI getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public void setUniqueIdentifier(UI uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }
}
