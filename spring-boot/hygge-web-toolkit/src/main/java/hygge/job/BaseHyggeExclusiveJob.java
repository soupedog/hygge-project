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
 * 独占类型 Job ，同一时刻只允许其业务逻辑独占运行。
 *
 * @author Xavier
 * @date 2026/7/4
 */
public abstract class BaseHyggeExclusiveJob<
        C extends HyggeJobContext,
        JBI extends HyggeJobBatchItem<JI>,
        JI extends BaseHyggeJobItem<RD, PD, ?>,
        RD,
        PD
        >
        extends BaseHyggeJob<C, JBI, JI, RD, PD> {

    /**
     * 拒绝策略。返回 true 则会拒绝执行 Job ，并返回默认的 Context 标记为 {@link JobStatusEnum#REJECT}。
     */
    protected abstract boolean reject();

    /**
     * 独占任务执行完成后的回调，用于重置 {@link BaseHyggeExclusiveJob#reject()} 的判断依据为未执行。<br/>
     */
    protected abstract void restRunningFlag();

    /**
     * 与非独占 Job 在 title 上进行区分，该函数返回值将作为 Job 的实际 title。
     */
    protected String resetTitle(String rowTitle) {
        return rowTitle + "(独占模式)";
    }

    @Override
    public C execute(String title, int batchSize, boolean bachAsynchronousEnable) {
        C context = null;

        try {
            if (reject()) {
                context = createContext();
                context.setTitle(title);
                context.setBatchSize(batchSize);
                context.setStatus(JobStatusEnum.REJECT);
            } else {
                try {
                    // super.execute 已经进行过异常捕获了，这里无需处理
                    context = super.execute(resetTitle(title), batchSize, bachAsynchronousEnable);
                } finally {
                    restRunningFlag();
                }
            }
        } catch (Throwable throwable) {
            ultimateThrowableHook(context, throwable);
        }

        return context;
    }
}
