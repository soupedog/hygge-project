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

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Xavier
 * @date 2026/7/2
 */
public class JobReportItem<UI> {
    protected Boolean isFail;
    protected UI uniqueIdentifier;
    protected int batchCount;
    protected long cost;
    protected String content;

    public Boolean getFail() {
        return isFail;
    }

    public void setFail(Boolean fail) {
        isFail = fail;
    }

    public UI getUniqueIdentifier() {
        return uniqueIdentifier;
    }

    public void setUniqueIdentifier(UI uniqueIdentifier) {
        this.uniqueIdentifier = uniqueIdentifier;
    }

    @JsonIgnore
    public int getBatchCount() {
        return batchCount;
    }

    public void setBatchCount(int batchCount) {
        this.batchCount = batchCount;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
