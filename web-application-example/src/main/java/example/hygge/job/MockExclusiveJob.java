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

package example.hygge.job;

import hygge.job.HyggeJobBatchItem;
import hygge.job.SimpleHyggeExclusiveJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 单例模式下 execute 是独占的。
 *
 * @author Xavier
 * @date 2026/7/4
 */
@Slf4j
@Component
public class MockExclusiveJob extends SimpleHyggeExclusiveJob<MockJobContext, MockJobItem, User, Void> {

    @Override
    protected String getJobName() {
        return this.getClass().getSimpleName();
    }

    @Override
    protected List<User> firstFetch(MockJobContext context, HyggeJobBatchItem<MockJobItem> jobBatchItem) {
        return Collections.emptyList();
    }

    @Override
    protected List<User> getNextBatch(MockJobContext context, HyggeJobBatchItem<MockJobItem> jobBatchItem) {
        return Collections.emptyList();
    }

    @Override
    protected MockJobItem createJobItem(MockJobContext context, HyggeJobBatchItem<MockJobItem> jobBatchItem, User rawData) {
        return new MockJobItem(rawData);
    }

    @Override
    protected Void handleSingleItem(MockJobContext context, MockJobItem jobItem) {
        return null;
    }

    @Override
    protected void finallyHook(MockJobContext context) {
        try {
            // 模拟业务耗时，方便触发独占运行拒绝策略
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MockJobContext createContext() {
        return new MockJobContext();
    }
}
