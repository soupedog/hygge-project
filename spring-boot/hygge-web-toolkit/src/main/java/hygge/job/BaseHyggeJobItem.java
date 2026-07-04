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

/**
 * 任务执行的最小单元
 *
 * @author Xavier
 * @date 2026/7/2
 */
public abstract class BaseHyggeJobItem<RD, PD, UI> extends JobTimeInfo {
    /**
     * 所属的批次执行编号，用于和 {@link HyggeJobBatchItem} 实例建立关联关系。
     */
    protected int batchCount;
    /**
     * 原始数据。
     */
    protected RD rawData;
    /**
     * 原始数据加工后的结果。
     */
    protected PD processedData;
    /**
     * 当前原始数据处理过程中遇到的异常。
     */
    protected Exception exception;

    public BaseHyggeJobItem(RD rawData) {
        this.rawData = rawData;
    }

    /**
     * 获取唯一标识，用于日志打印等环节，请确保它能被序列化。
     */
    public abstract UI getUniqueIdentifier();

    public Object getErrorInfo() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<>();
        // 最小执行单元之内
        map.put("scope", "unit");
        map.put("identifier", getUniqueIdentifier());
        map.put("batchCount", batchCount);
        if (exception == null) {
            map.put("message", "unknown(exception empty)");
        } else {
            map.put("message", exception.getMessage());
        }
        return map;
    }

    public boolean isFailure() {
        return exception != null;
    }

    public boolean isSuccess() {
        return !isFailure();
    }

    public int getBatchCount() {
        return batchCount;
    }

    public void setBatchCount(int batchCount) {
        this.batchCount = batchCount;
    }

    public RD getRawData() {
        return rawData;
    }

    public void setRawData(RD rawData) {
        this.rawData = rawData;
    }

    public PD getProcessedData() {
        return processedData;
    }

    public void setProcessedData(PD processedData) {
        this.processedData = processedData;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }
}
