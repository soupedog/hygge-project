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

import hygge.commons.constant.enums.GlobalHyggeCodeEnum;
import example.hygge.domain.ControllerResponse;
import example.hygge.domain.CustomSystemCode;
import example.hygge.domain.User;
import hygge.web.template.definition.HyggeController;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 演示替换 response 默认结构用的 Controller
 *
 * @author Xavier
 * @date 2022/7/7
 * @since 1.0
 */
@Slf4j
@RestController
@Tag(name = "ReplaceControllerResponseController", description = "演示自定义数据结构相关操作，请与 '/logConfig/user/standard' 进行比较")
public class ReplaceControllerResponseController implements HyggeController<ResponseEntity<?>> {

    /**
     * 如果你的 customResponseWrapper 会使用到 HyggeCode 的 code/extraInfo 属性，需要对自定义 HyggeCode 进行额外处理，见 {@link CustomSystemCode}
     */
    private static final HyggeControllerResponseWrapper<ResponseEntity<?>> customResponseWrapper = (httpStatus, headers, hyggeCode, msg, entity, throwable) -> {
        // "status(HttpStatus status)" 方法在 Spring Boot 3.x 环境中不存在了，此处用 int 型重载进行兼容性优化
        ResponseEntity.BodyBuilder result = ResponseEntity.status(httpStatus.value());
        return result.body(entity);
    };

    /**
     * 这是示例：演示自定义输出内容，此处是自定义为去掉 {@link ControllerResponse} 的主协议封装
     */
    @GetMapping("/replace/customResponse")
    public ResponseEntity<?> customResponse() {
        return success(HttpStatus.OK, null, GlobalHyggeCodeEnum.SUCCESS, null, new User("customResponse", "customResponse", "customResponse"), customResponseWrapper);
    }
}
