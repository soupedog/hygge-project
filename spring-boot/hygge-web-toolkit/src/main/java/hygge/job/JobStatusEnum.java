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
 * @author Xavier
 * @date 2026/7/4
 */
public enum JobStatusEnum {
    /**
     * 执行无误。
     */
    SUCCESS,
    /**
     * 如 Job 独占，当前执行被拒绝执行。
     */
    REJECT,
    /**
     * 执行过程中发生了异常
     */
    FAILURE,
}
