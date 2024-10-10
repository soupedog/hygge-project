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

package hygge.util.bo.configuration.arranger.analyst;


import hygge.util.bo.configuration.arranger.PropertiesArrangerConfiguration;
import hygge.util.bo.configuration.arranger.PropertiesDefaultConfigurationType;
import hygge.util.definition.analyst.ConfigurationType;
import hygge.util.definition.analyst.ConfigurationTypeAnalyst;

/**
 * Spring 相关键值类型分析器
 *
 * @author Xavier
 * @date 2024/9/25
 * @since 1.0
 */
public class PropertiesSpringAnalyst implements ConfigurationTypeAnalyst {
    @Override
    public ConfigurationType typeResult() {
        return PropertiesDefaultConfigurationType.SPRING;
    }

    @Override
    public boolean isMatched(PropertiesArrangerConfiguration configuration, String key) {
        return (key != null && key.startsWith("spring."));
    }
}
