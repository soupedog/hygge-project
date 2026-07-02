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
import hygge.commons.exception.LightRuntimeException;
import hygge.job.BaseHyggeJob;
import hygge.job.HyggeJobContext;
import hygge.util.UtilCreator;
import hygge.util.definition.RandomHelper;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Xavier
 * @date 2026/7/2
 */
@Slf4j
public class MockJob extends BaseHyggeJob<MockJobItem, MockJobItemData, String> {
    private static final RandomHelper randomHelper = UtilCreator.INSTANCE.getDefaultInstance(RandomHelper.class);
    private final List<MockJobItem> mockDataFromDB;

    public MockJob(int batchSize, boolean bachAsynchronousEnable) {
        super(batchSize, bachAsynchronousEnable);
        this.mockDataFromDB = new ArrayList<>();

        // 模拟从数据库拉取的数据
        for (int i = 0; i < 24; i++) {
            MockJobItemData data = MockJobItemData.builder()
                    .id(randomHelper.randomUUID(true))
                    .someData(randomHelper.randomInteger(-50, 50))
                    .build();

            mockDataFromDB.add(
                    new MockJobItem(data, data.getId())
            );
        }
    }

    @Override
    protected String getJobName() {
        return this.getClass().getSimpleName();
    }

    @Override
    protected Collection<MockJobItem> firstFetch(HyggeJobContext context) {
        context.saveObject(MockJobKey.SUCCESS_DIRECT, ThreadLocalRandom.current().nextBoolean());

        Collection<MockJobItem> result = new ArrayList<>();

        for (int i = 0; i < batchSize; i++) {
            if (!mockDataFromDB.isEmpty()) {
                result.add(mockDataFromDB.remove(0));
            }
        }

        return result;
    }

    @Override
    protected Collection<MockJobItem> getNextBatch(HyggeJobContext context) {
        Collection<MockJobItem> result = new ArrayList<>();

        for (int i = 0; i < batchSize; i++) {
            if (!mockDataFromDB.isEmpty()) {
                result.add(mockDataFromDB.remove(0));
            }
        }

        return result;
    }

    @Override
    protected void handleSingleItem(HyggeJobContext context, MockJobItem jobItem) {
        try {
            // 模拟处理业务耗时
            Thread.sleep(randomHelper.randomInteger(50, 200));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (!(boolean) context.getObject(MockJobKey.SUCCESS_DIRECT) && jobItem.getSource().getSomeData() > 0) {
            throw new LightRuntimeException("模拟的异常" + randomHelper.randomString(5, StringCategoryEnum.A_Z, StringCategoryEnum.NUMBER));
        }
    }
}
