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
 * 任务时间信息基类
 *
 * @author Xavier
 * @date 2026/7/3
 */
public abstract class JobTimeInfo {
    /**
     * 当前单元执行的开始时间(UTC 毫秒级时间戳)
     */
    protected long startTs;
    /**
     * 当前数据处理耗时(毫秒)
     */
    protected long cost;

    /**
     * 开始计时
     */
    public void initStartTs() {
        this.startTs = System.currentTimeMillis();
    }

    /**
     * 计时结束
     */
    public void stop() {
        this.cost = System.currentTimeMillis() - this.startTs;
    }

    public long getStartTs() {
        return startTs;
    }

    public void setStartTs(long startTs) {
        this.startTs = startTs;
    }

    public long getCost() {
        return cost;
    }

    public void setCost(long cost) {
        this.cost = cost;
    }
}
