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
 * 不打算扩展 {@link HyggeJobBatchItem} 的语法糖基类。
 *
 * @author Xavier
 * @date 2026/7/3
 */
public abstract class SimpleHyggeJob<C extends HyggeJobContext, JI extends BaseHyggeJobItem<RD, PD, ?>, RD, PD>
        extends BaseHyggeJob<C, HyggeJobBatchItem<JI>, JI, RD, PD> {

    @Override
    protected HyggeJobBatchItem<JI> createJobBatchItem(HyggeJobContext context) {
        return new HyggeJobBatchItem<>();
    }
}
