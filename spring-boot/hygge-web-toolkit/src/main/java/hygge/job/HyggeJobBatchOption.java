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
 * 任务批次操作工具
 *
 * @author Xavier
 * @date 2026/7/3
 */
public class HyggeJobBatchOption {
    /**
     * 如果为 true，下一次执行将跳过
     */
    private boolean noNextFetch = false;

    /**
     * 重置所有标识为初始值
     */
    public void reset() {
        this.noNextFetch = false;
    }

    public boolean isNoNextFetch() {
        return noNextFetch;
    }

    public void setNoNextFetch(boolean noNextFetch) {
        this.noNextFetch = noNextFetch;
    }
}
