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
import hygge.util.UtilCreator;
import hygge.util.definition.RandomHelper;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Xavier
 * @date 2026/7/3
 */
@Getter
@Setter
@Generated
public class MockPage<T extends User> {
    private static final RandomHelper randomHelper = UtilCreator.INSTANCE.getDefaultInstance(RandomHelper.class);
    private List<User> mockDatabase = new ArrayList<>();
    private List<User> content;
    private int batchSize;

    public MockPage(int batchSize) {
        this.batchSize = batchSize;
        int max = batchSize * 8;

        // 模拟数据库全量数据 24 个
        for (int i = 0; i < max; i++) {
            mockDatabase.add(User.builder()
                    .uid(i)
                    .name(randomHelper.randomString(6, StringCategoryEnum.A_Z))
                    .build());
        }
    }

    /**
     * 模拟带条件的数据库查询，翻页查询到一定程度就没有结果了，最多有四页
     */
    public boolean isLast() {
        return mockDatabase.size() <= batchSize * 4;
    }

    public long getTotalElements() {
        return batchSize * 8L;
    }

    /**
     * 模拟也容量是 3
     */
    public MockPage<T> nextPageable() {
        content = new ArrayList<>();
        for (int i = 0; i < batchSize; i++) {
            content.add(mockDatabase.remove(0));
        }
        return this;
    }
}
