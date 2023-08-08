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

package example.hygge.validate;

import com.fasterxml.jackson.databind.ObjectMapper;
import hygge.commons.spring.validator.definition.HyggeSpringValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 自定义 HyggeSpringValidator 的示例
 *
 * @author Xavier
 * @date 2023/8/8
 * @since 1.0
 */
@Slf4j
@Component
public class CustomValidate implements HyggeSpringValidator {
    /**
     * 无实际含义，仅演示 {@link HyggeSpringValidator} 实例中可正常使用 {@link Autowired} 来获取其他对象
     */
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void validate() {
        log.info("应用启动初期，执行一些自定义的校验，这是一个 @Autowired 获取的对象 {}.", objectMapper.getClass().getSimpleName());
    }

    @Override
    public int getOrder() {
        return HyggeSpringValidator.super.getOrder() + 1;
    }
}

