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
import hygge.job.BaseHyggeJob;
import hygge.job.DefaultHyggeJobBatchItem;
import hygge.job.HyggeJobContext;
import hygge.job.SimpleHyggeJob;
import hygge.util.UtilCreator;
import hygge.util.definition.RandomHelper;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

/**
 * 如果不需要扩展 Context 和 JobBatchItem，直接继承 {@link SimpleHyggeJob} 即可。
 * <p>
 * 大致执行逻辑：<br/>
 * <p>
 * 1.firstFetch、getNextBatch 扫描数据库数据，一次拉取对应了一个批次
 * <p>
 * 2.处理扫描到的数据，如果 fetch 返回了空 List，那么终止任务
 * <p>
 * 3.进行简要的日志打印
 * <p>
 * 得到的特性：<br/>
 * 1.调整 bachAsynchronousEnable 参数值就可便捷实现同批次数据进行异步处理。异步处理时，仅当同批次所有数据处理完成才可能进入下一批次
 *
 * @author Xavier
 * @date 2026/7/2
 */
@Slf4j
public class MockJob extends BaseHyggeJob<MockJobContext, DefaultHyggeJobBatchItem<MockJobItem>, MockJobItem, User, Void> {
    private static final RandomHelper randomHelper = UtilCreator.INSTANCE.getDefaultInstance(RandomHelper.class);

    public MockJob(int defaultBatchSize, boolean bachAsynchronousEnable) {
        super(defaultBatchSize, bachAsynchronousEnable);
    }

    @Override
    protected String getJobName() {
        return this.getClass().getSimpleName();
    }

    /**
     * 直接继承 {@link SimpleHyggeJob} 则该方法非必须，此处演示如何扩展 {@link HyggeJobContext}
     */
    @Override
    protected MockJobContext createContext() {
        MockJobContext context = new MockJobContext();
        context.setTitle("有随机模拟抛出异常，需要多试几次");
        return context;
    }

    /**
     * 直接继承 {@link SimpleHyggeJob} 则该方法非必须，此处演示如何扩展 {@link DefaultHyggeJobBatchItem}
     */
    @Override
    protected DefaultHyggeJobBatchItem<MockJobItem> createJobBatchItem(MockJobContext context) {
        return new DefaultHyggeJobBatchItem<>();
    }

    @Override
    protected List<User> firstFetch(MockJobContext context, DefaultHyggeJobBatchItem<MockJobItem> jobBatchItem) {
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
    protected List<User> getNextBatch(MockJobContext context, DefaultHyggeJobBatchItem<MockJobItem> jobBatchItem) {
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
    protected MockJobItem createJobItem(MockJobContext context, DefaultHyggeJobBatchItem<MockJobItem> jobBatchItem, User rawData) {
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

        if (randomHelper.randomInteger(1, 100) > 98) {
            throw new RuntimeException("随机模拟的业务处理异常:" + randomHelper.randomString(3, StringCategoryEnum.A_Z));
        }

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
    protected void batchCompleteHook(MockJobContext context, DefaultHyggeJobBatchItem<MockJobItem> jobBatchItem, List<User> rawDataCollection, List<Void> processedDataCollection) {
        super.batchCompleteHook(context, jobBatchItem, rawDataCollection, processedDataCollection);
    }
}
