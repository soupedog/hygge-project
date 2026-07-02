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

import hygge.job.BaseHyggeJobItem;
import hygge.job.HyggeJobContext;
import hygge.job.JobReportItem;

/**
 * @author Xavier
 * @date 2026/7/2
 */
public class MockJobItem extends BaseHyggeJobItem<MockJobItemData, String> {
    protected MockJobItem(MockJobItemData source) {
        super(source);
    }

    @Override
    public JobReportItem<String> createReportAfterStop(HyggeJobContext context, long cost) {
        // 成功时不输出日志
        if (exception == null) {
            return null;
        }

        JobReportItem<String> result = new JobReportItem<>();
        result.setFail(true);
        result.setCost(cost);
        result.setUniqueIdentifier(source.getId());
        result.setContent(exception.getMessage());

        return result;
    }
}
