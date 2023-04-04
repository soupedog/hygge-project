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

package hygge.web.util.http.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hygge.commons.exception.UtilRuntimeException;
import hygge.util.UtilCreator;
import hygge.util.definition.JsonHelper;
import hygge.web.util.http.definition.HttpHelperResponseEntityReader;

/**
 * 网络请求工具 Response 读取工具 默认实现
 *
 * @author Xavier
 * @date 2022/7/12
 * @since 1.0
 */
public class DefaultHttpHelperResponseEntityReader implements HttpHelperResponseEntityReader {
    private static final JsonHelper<?> jsonHelper = UtilCreator.INSTANCE.getDefaultJsonHelperInstance(false);

    @Override
    public <T> T readAsObject(String originalResponse, Class<T> tClass) {
        return jsonHelper.readAsObject(originalResponse, tClass);
    }

    public <T> T readAsObject(String originalResponse, TypeReference<T> typeReference) {
        return (T) jsonHelper.readAsObjectWithClassInfo(originalResponse, typeReference);
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <E> Object readAsObjectSmart(E originalResponse, Object dataClassInfo) throws JsonProcessingException {
        if (!(originalResponse instanceof String)) {
            return originalResponse;
        }

        ObjectMapper objectMapper = (ObjectMapper) jsonHelper.getDependence();
        if (dataClassInfo instanceof Class) {
            if (dataClassInfo.equals(String.class)) {
                return originalResponse;
            }
            return objectMapper.readValue((String) originalResponse, (Class) dataClassInfo);
        } else if (dataClassInfo instanceof TypeReference) {
            return objectMapper.readValue((String) originalResponse, (TypeReference) dataClassInfo);
        } else {
            throw new UtilRuntimeException("Unsupported class info:" + dataClassInfo + ".");
        }
    }
}
