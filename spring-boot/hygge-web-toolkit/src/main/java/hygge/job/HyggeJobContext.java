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

import hygge.commons.exception.InternalRuntimeException;
import hygge.commons.template.container.base.AbstractInterfaceKeyHyggeContext;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Xavier
 * @date 2026/7/2
 */
public class HyggeJobContext extends AbstractInterfaceKeyHyggeContext<Enum<?>, HyggeJobContextKey> {
    protected JobStatusEnum status = JobStatusEnum.SUCCESS;
    protected final Long startTs = System.currentTimeMillis();
    /**
     * 当前 Job 执行报告的标题
     */
    protected String title;
    /**
     * 当前 Job 执行的单批次最小执行单元数量
     */
    protected int batchSize;
    /**
     * 单批次内所有最小执行单元是否异步执行
     */
    protected boolean bachAsynchronousEnable;
    /**
     * 批次编号，每个批次结束后会进行自增
     */
    protected int batchCount;
    /**
     * 最小执行单元处理数
     */
    protected AtomicInteger itemCount = new AtomicInteger(0);
    protected HyggeJobReporter jobReporter;

    /**
     * 执行成功时，最后出循环会额外 +1，需要 -1 补正。
     */
    public int getActualTotalBatch() {
        if (JobStatusEnum.SUCCESS.equals(status)) {
            return batchCount - 1;
        } else {
            return batchCount;
        }
    }

    public void initConfigurationCheck() {
        if (title == null || title.trim().isEmpty()) {
            throw new InternalRuntimeException("HyggeJobContext: title can't be empty.");
        }
        if (batchSize < 1) {
            throw new InternalRuntimeException("HyggeJobContext: batchSize can't less than 1.");
        }
    }

    public void batchContIncrease() {
        this.batchCount = this.batchCount + 1;
    }

    public JobStatusEnum getStatus() {
        return status;
    }

    public void setStatus(JobStatusEnum status) {
        this.status = status;
    }

    public Long getStartTs() {
        return startTs;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AtomicInteger getItemCount() {
        return itemCount;
    }

    public void setItemCount(AtomicInteger itemCount) {
        this.itemCount = itemCount;
    }

    public void itemCountIncrease() {
        this.itemCount.incrementAndGet();
    }

    public int getBatchCount() {
        return batchCount;
    }

    public void setBatchCount(int batchCount) {
        this.batchCount = batchCount;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public boolean isBachAsynchronousEnable() {
        return bachAsynchronousEnable;
    }

    public void setBachAsynchronousEnable(boolean bachAsynchronousEnable) {
        this.bachAsynchronousEnable = bachAsynchronousEnable;
    }

    public HyggeJobReporter getJobReporter() {
        return jobReporter;
    }

    public void setJobReporter(HyggeJobReporter jobReporter) {
        this.jobReporter = jobReporter;
    }
}
