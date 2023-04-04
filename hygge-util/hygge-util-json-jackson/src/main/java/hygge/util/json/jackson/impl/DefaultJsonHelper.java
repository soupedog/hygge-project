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

package hygge.util.json.jackson.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import hygge.util.definition.ParameterHelper;
import hygge.util.json.jackson.base.BaseJsonHelper;

import java.util.Properties;

/**
 * 基于 jackson 的默认 json 工具类
 *
 * @author Xavier
 * @date 2022/6/28
 * @since 1.0
 */
public class DefaultJsonHelper extends BaseJsonHelper {
    public DefaultJsonHelper(Properties properties) {
        super(properties);
    }

    public DefaultJsonHelper(Properties properties, ObjectMapper objectMapper) {
        super(properties, objectMapper);
    }

    public DefaultJsonHelper(Properties properties, ObjectMapper objectMapper, ParameterHelper parameterHelper) {
        super(properties, objectMapper, parameterHelper);
    }
}
