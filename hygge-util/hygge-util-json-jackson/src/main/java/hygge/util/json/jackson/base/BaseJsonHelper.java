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

package hygge.util.json.jackson.base;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hygge.commons.exception.UtilRuntimeException;
import hygge.commons.template.definition.HyggeConfigurator;
import hygge.commons.template.definition.InfoMessageSupplier;
import hygge.util.UtilCreator;
import hygge.util.definition.JsonHelper;
import hygge.util.definition.ParameterHelper;
import hygge.util.json.jackson.impl.HyggeObjectMapperDefaultConfigurator;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

/**
 * 基于 jackson 的 json 工具类基类
 *
 * @author Xavier
 * @date 2022/6/28
 * @since 1.0
 */
public class BaseJsonHelper implements JsonHelper<ObjectMapper>, InfoMessageSupplier {
    protected HyggeObjectMapperDefaultConfigurator configurator;
    protected ParameterHelper parameterHelper;
    protected ObjectMapper objectMapper;

    protected BaseJsonHelper(Properties properties) {
        this.parameterHelper = UtilCreator.INSTANCE.getDefaultInstance(ParameterHelper.class);
        this.objectMapper = new ObjectMapper();
        initConfiguration();
        Properties actualConfiguration = configurator.createDefaultConfig();
        actualConfiguration.putAll(properties);
        configurator.configure(this.objectMapper, actualConfiguration);
    }

    protected BaseJsonHelper(Properties properties, ObjectMapper objectMapper) {
        this.parameterHelper = UtilCreator.INSTANCE.getDefaultInstance(ParameterHelper.class);
        this.objectMapper = objectMapper;
        initConfiguration();
        Properties actualConfiguration = configurator.createDefaultConfig();
        actualConfiguration.putAll(properties);
        configurator.configure(this.objectMapper, actualConfiguration);
    }

    protected BaseJsonHelper(Properties properties, ObjectMapper objectMapper, ParameterHelper parameterHelper) {
        this.parameterHelper = parameterHelper;
        this.objectMapper = objectMapper;
        initConfiguration();
        Properties actualConfiguration = configurator.createDefaultConfig();
        actualConfiguration.putAll(properties);
        configurator.configure(this.objectMapper, actualConfiguration);
    }

    /**
     * 初始化默认配置项工具
     */
    protected void initConfiguration() {
        this.configurator = new HyggeObjectMapperDefaultConfigurator();
    }

    @Override
    public ObjectMapper getDependence() {
        return objectMapper;
    }

    @Override
    public String formatAsString(Object target) {
        try {
            String result;
            if (target instanceof String) {
                if (target.toString().length() < 2) {
                    throw new UtilRuntimeException("JsonHelper fail to format: " + target);
                }
                String firstVal = target.toString().substring(0, 1);
                switch (firstVal) {
                    case "[":
                        result = objectMapper.writeValueAsString(objectMapper.readValue((String) target, List.class));
                        break;
                    case "{":
                        result = objectMapper.writeValueAsString(objectMapper.readValue((String) target, HashMap.class));
                        break;
                    default:
                        throw new UtilRuntimeException("JsonHelper fail to format: " + target);
                }
            } else {
                result = objectMapper.writeValueAsString(target);
            }
            return result;
        } catch (IOException e) {
            throw new UtilRuntimeException("JsonHelper fail to format.", e);
        }
    }

    @Override
    public <T> T readAsObject(String jsonString, Class<T> tClass) {
        try {
            return objectMapper.readValue(jsonString, tClass);
        } catch (JsonProcessingException e) {
            throw new UtilRuntimeException("JsonHelper fail to readValue.", e);
        }
    }

    public <T> T readAsObject(String jsonString, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(jsonString, typeReference);
        } catch (JsonProcessingException e) {
            throw new UtilRuntimeException("JsonHelper fail to readValue.", e);
        }
    }

    @Override
    public Object readAsObjectWithClassInfo(String jsonString, Object classInfo) {
        parameterHelper.objectNotNull("classInfo", classInfo);

        if (classInfo instanceof Class) {
            return readAsObject(jsonString, (Class<?>) classInfo);
        }
        if (!(classInfo instanceof TypeReference)) {
            throw new UtilRuntimeException(unexpectedClass("classInfo", classInfo.getClass(), TypeReference.class));
        }
        return readAsObject(jsonString, (TypeReference<?>) classInfo);
    }

    @Override
    public HyggeConfigurator<ObjectMapper, Properties> getConfigurator() {
        return configurator;
    }
}
