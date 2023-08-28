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

package hygge.commons.spring.validator.config;

import hygge.commons.spring.config.configuration.definition.HyggeAutoConfiguration;
import hygge.commons.spring.validator.configuration.HyggeSpringValidatorConfiguration;
import hygge.commons.spring.validator.impl.HyggeCodeUniqueValidator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 默认的 HyggeSpringValidator 自动注册器
 *
 * @author Xavier
 * @date 2023/8/8
 * @since 1.0
 */
@AutoConfiguration
@EnableConfigurationProperties(value = HyggeSpringValidatorConfiguration.class)
public class HyggeSpringValidatorAutoConfiguration implements HyggeAutoConfiguration {
    @Bean
    @ConditionalOnExpression("#{environment['hygge.web-toolkit.validator.hygge-code.base-packages'] != null}")
    public HyggeCodeUniqueValidator hyggeCodeUniqueValidator(HyggeSpringValidatorConfiguration configuration) {
        return new HyggeCodeUniqueValidator(configuration);
    }
}
