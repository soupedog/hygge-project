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

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 基于本地内存 CAS 非分布式的独占任务实现基类。
 * <p>
 * 该模版仅能确保单实例，调用 execute 方法有独占机制，多实例间不会互斥。<br/>
 * <p>
 * 如需多实例间互斥、分布式级别互斥，请放弃当前基类，转而自行扩展 {@link BaseHyggeExclusiveJob} 。
 *
 * @author Xavier
 * @date 2026/7/4
 */
public abstract class SimpleHyggeExclusiveJob<C extends HyggeJobContext, JI extends BaseHyggeJobItem<RD, PD, ?>, RD, PD>
        extends BaseHyggeExclusiveJob<C, HyggeJobBatchItem<JI>, JI, RD, PD> {
    protected final AtomicBoolean IS_RUNNING = new AtomicBoolean(false);

    @Override
    protected boolean reject(C context) {
        return !IS_RUNNING.compareAndSet(false, true);
    }

    @Override
    protected void restRunningFlag() {
        IS_RUNNING.set(false);
    }

    @Override
    protected HyggeJobBatchItem<JI> createJobBatchItem(C context) {
        return new HyggeJobBatchItem<>();
    }
}
