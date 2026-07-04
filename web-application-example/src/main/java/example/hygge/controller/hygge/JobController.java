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

package example.hygge.controller.hygge;

import example.hygge.job.MockExclusiveJob;
import example.hygge.job.MockJob;
import example.hygge.job.MockJobContext;
import hygge.job.JobStatusEnum;
import hygge.web.template.definition.HyggeController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Xavier
 * @date 2026/7/1
 */
@RestController
@Tag(name = "JobController", description = "演示 BaseHyggeJob 配套模板用法")
public class JobController implements HyggeController<ResponseEntity<?>> {
    private final MockJob mockJob;
    private final MockExclusiveJob mockExclusiveJob;

    public JobController(MockJob mockJob, MockExclusiveJob mockExclusiveJob) {
        this.mockJob = mockJob;
        this.mockExclusiveJob = mockExclusiveJob;
    }

    @GetMapping("/job")
    public ResponseEntity<?> executeJob(@RequestParam(value = "batchSize", required = false, defaultValue = "3") Integer batchSize,
                                        @RequestParam(value = "bachAsynchronousEnable", required = false, defaultValue = "true") Boolean bachAsynchronousEnable) {
        MockJobContext inputContext = mockJob.createContext();
        inputContext.setTitle("会随机模拟抛出异常，可多试几次");
        inputContext.setBatchSize(batchSize);
        inputContext.setBachAsynchronousEnable(bachAsynchronousEnable);
        MockJobContext context = mockJob.execute(inputContext);

        return success(context.getStatus());
    }

    @GetMapping("/exclusiveJob")
    @Operation(summary = "模拟独占 Job，同一时刻仅一个 Job 能被执行", description = "Swagger 页面访问会触发某种防重复请求机制，请把 http://localhost:8080/exclusiveJob 复制到浏览器访问，并按 F5 刷新发起真正的多次请求。")
    public ResponseEntity<?> exclusiveJob() {

        MockJobContext inputContext = mockExclusiveJob.createContext();
        inputContext.setTitle("独占运行的任务，同时刻不允许多个 Job 执行(模拟单次耗时 5 秒)");
        inputContext.setBatchSize(10);
        inputContext.setBachAsynchronousEnable(true);
        MockJobContext context = mockExclusiveJob.execute(inputContext);

        return success(JobStatusEnum.REJECT.equals(context.getStatus()) ? "已存在正在运行的 Job，请稍后再试" : "执行任务成功");
    }
}
