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

import hygge.commons.constant.enums.StringCategoryEnum;
import hygge.commons.exception.InternalRuntimeException;
import hygge.job.BaseHyggeExclusiveJob;
import hygge.job.BaseHyggeJob;
import hygge.job.HyggeJobBatchItem;
import hygge.job.SimpleHyggeExclusiveJob;
import hygge.job.SimpleHyggeJob;
import hygge.util.UtilCreator;
import hygge.util.definition.RandomHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * 演示基本用法，详情见其最上层父类。<br/>
 * <p>
 * 如果想实现同一时刻独占运行，可以继承 {@link BaseHyggeExclusiveJob}，也有基于 CAS 内存级非分布式的默认实现。{@link SimpleHyggeExclusiveJob}
 *
 * @author Xavier
 * @date 2026/7/2
 * @see BaseHyggeJob
 */
@Slf4j
@Component
public class MockJob extends SimpleHyggeJob<MockJobContext, MockJobItem, User, Void> {
    private static final RandomHelper randomHelper = UtilCreator.INSTANCE.getDefaultInstance(RandomHelper.class);

    @Override
    protected String getJobName() {
        return this.getClass().getSimpleName();
    }

    @Override
    protected List<User> firstFetch(MockJobContext context, HyggeJobBatchItem<MockJobItem> jobBatchItem) {
        MockPage<User> page = new MockPage<>(context.getBatchSize());
        // 模拟 Jpa 某个查询返回了 Page 对象
        page = page.nextPageable();

        // 演示扩展 Context 属性
        context.setPage(page);

        if (page.isLast()) {
            // 演示上下文保存对象
            context.saveObject(MockJobKey.NO_NEXT_PAGE, true);
        } else {
            context.saveObject(MockJobKey.NO_NEXT_PAGE, false);
        }

        return page.getContent();
    }

    @Override
    protected List<User> getNextBatch(MockJobContext context, HyggeJobBatchItem<MockJobItem> jobBatchItem) {
        if (context.getObject(MockJobKey.NO_NEXT_PAGE)) {
            return Collections.emptyList();
        }

        // 演示上下文获取对象
        MockPage<User> page = context.getPage();
        // 模拟翻页并查询得到下一个 page 对象
        page = page.nextPageable();
        context.setPage(page);

        if (page.isLast()) {
            context.saveObject(MockJobKey.NO_NEXT_PAGE, true);
        }
        return page.getContent();
    }

    @Override
    protected MockJobItem createJobItem(MockJobContext context, HyggeJobBatchItem<MockJobItem> jobBatchItem, User rawData) {
        return new MockJobItem(rawData);
    }

    @Override
    protected Void handleSingleItem(MockJobContext context, MockJobItem jobItem) {
        // 模拟业务耗时
        try {
            Thread.sleep(randomHelper.randomInteger(50, 100));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (context.isMockException() && randomHelper.randomInteger(1, 100) > 80) {
            throw new InternalRuntimeException("随机模拟的业务处理异常:" + randomHelper.randomString(3, StringCategoryEnum.A_Z));
        }

        context.getJobReporter()
                .addProcessTrackingInfo(System.currentTimeMillis(), "进行了某种敏感业务操作：" + randomHelper.randomString(4, StringCategoryEnum.a_z, StringCategoryEnum.NUMBER));

        // 此处实例不需要返回值单纯查询操作，如有需要可以返回如 UserDTO
        // 通过重写 batchCompleteHook，可以对 UserDTO 进行使用
        return null;
    }

    /**
     * 该方法非必须，方便批处理操作的钩子函数，此处能获取到 fetch 的原始数据，和当前批次所有的处理后结果。<br/>
     * <p>
     * (演示代码是 Void 泛型，有需要可以自行修改为其他类型)
     */
    @Override
    protected void batchCompleteHook(MockJobContext context, HyggeJobBatchItem<MockJobItem> jobBatchItem, List<User> rawDataList, List<Void> processedDataList) {
        super.batchCompleteHook(context, jobBatchItem, rawDataList, processedDataList);
    }

    @Override
    public MockJobContext createContext() {
        return new MockJobContext();
    }
}
