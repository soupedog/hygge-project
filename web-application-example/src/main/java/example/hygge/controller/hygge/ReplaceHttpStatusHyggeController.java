/*
 * Copyright 2022-2023 the original author or authors.
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

import hygge.commons.exception.InternalRuntimeException;
import hygge.commons.exception.LightRuntimeException;
import hygge.web.template.definition.HyggeController;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Xavier
 * @date 2023/1/12
 * @since 1.0
 */
@Slf4j
@RestController
@Tag(name = "ReplaceHttpStatusHyggeController", description = "演示修改 Response 的 Http 状态码，请与 '/standard/mockExceptio' 进行比较")
public class ReplaceHttpStatusHyggeController implements HyggeController<ResponseEntity<?>> {
    /**
     * 固定服务端 Response 的 http 状态码为 200
     */
    @Override
    public ResponseEntity.BodyBuilder getBuilder(HttpStatus httpStatus) {
        return HyggeController.super.getBuilder(HttpStatus.OK);
    }

    @GetMapping("/replace/mockException")
    public ResponseEntity<?> mockException() {
        if (ThreadLocalRandom.current().nextBoolean()) {
            throw new LightRuntimeException("模拟轻量型运行时异常");
        } else {
            throw new InternalRuntimeException("模拟服务端内部异常");
        }
    }
}