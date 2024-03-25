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

package hygge.commons.spring.validator.configuration;

import hygge.commons.constant.enums.GlobalHyggeCodeEnum;
import hygge.commons.spring.config.configuration.definition.HyggeSpringConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * HyggeSpringValidator 配置项
 *
 * @author Xavier
 * @date 2023/3/28
 * @since 1.0
 */
@ConfigurationProperties(prefix = "hygge.web-toolkit.validator")
public class HyggeSpringValidatorConfiguration implements HyggeSpringConfigurationProperties {
    private HyggeCodeValidatorConfiguration hyggeCode;

    public HyggeCodeValidatorConfiguration getHyggeCode() {
        return hyggeCode;
    }

    public void setHyggeCode(HyggeCodeValidatorConfiguration hyggeCode) {
        this.hyggeCode = hyggeCode;
    }

    /**
     * HyggeCode 校验配置项
     *
     * @author Xavier
     * @date 2023/3/28
     * @since 1.0
     */
    public static class HyggeCodeValidatorConfiguration {
        /**
         * 自定义 HyggeCode 实现类所在包路径。 ({@link GlobalHyggeCodeEnum} 会自动追加，无需手工指定)
         */
        private String basePackages;

        public String getBasePackages() {
            return basePackages;
        }

        public void setBasePackages(String basePackages) {
            this.basePackages = basePackages;
        }
    }
}
