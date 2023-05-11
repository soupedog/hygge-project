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

package hygge.util.json.jackson.definition;

import com.fasterxml.jackson.databind.ObjectMapper;
import hygge.commons.template.definition.HyggeConfigurator;

import java.util.Properties;

/**
 * 对 {@link ObjectMapper} 进行配置的工具
 *
 * @author Xavier
 * @date 2023/5/11
 * @since 1.0
 */
public interface HyggeObjectMapperConfigurator extends HyggeConfigurator<ObjectMapper, Properties> {

    /**
     * 创建一份默认配置的
     *
     * @return 默认的配置项
     */
    Properties createDefaultConfig();

    /**
     * 对目标 {@link ObjectMapper} 进行实际配置
     */
    @Override
    void configure(ObjectMapper objectMapper, Properties properties);
}
