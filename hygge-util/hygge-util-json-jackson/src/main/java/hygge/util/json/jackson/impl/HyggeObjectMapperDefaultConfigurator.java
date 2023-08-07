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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hygge.commons.exception.UtilRuntimeException;
import hygge.util.UtilCreator;
import hygge.util.definition.JsonHelper;
import hygge.util.definition.ParameterHelper;
import hygge.util.json.jackson.definition.HyggeObjectMapperConfigurator;

import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

/**
 * 对 {@link ObjectMapper} 进行配置的工具的默认实现
 *
 * @author Xavier
 * @date 2023/5/11
 * @since 1.0
 */
public class HyggeObjectMapperDefaultConfigurator implements HyggeObjectMapperConfigurator {
    protected ParameterHelper parameterHelper = UtilCreator.INSTANCE.getDefaultInstance(ParameterHelper.class);

    @Override
    public Properties createDefaultConfig() {
        Properties properties = new Properties();
        // 反序列化出现多余属性时,选择忽略不抛出异常
        properties.put(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 不进行排版缩进
        properties.put(SerializationFeature.INDENT_OUTPUT, false);
        // 开启允许数字以 0 开头
        properties.put(JsonReadFeature.ALLOW_LEADING_ZEROS_FOR_NUMBERS.mappedFeature(), true);
        return properties;
    }

    @Override
    public void configure(ObjectMapper objectMapper, Properties properties) {
        parameterHelper.objectNotNull("jsonHelper-objectMapper", objectMapper);

        Properties inputProperties = parameterHelper.parseObjectOfNullable("jsonHelper-properties", properties, new Properties());
        if (inputProperties.containsKey(JsonHelper.ConfigKey.INDENT)) {
            // 是否进行排版缩进
            inputProperties.put(SerializationFeature.INDENT_OUTPUT, parameterHelper.booleanFormatNotEmpty(JsonHelper.ConfigKey.INDENT.toString(), inputProperties.get(JsonHelper.ConfigKey.INDENT)));
            inputProperties.remove(JsonHelper.ConfigKey.INDENT);
        }
        Properties finalProperties = createDefaultConfig();
        finalProperties.putAll(inputProperties);
        for (Map.Entry<Object, Object> entry : finalProperties.entrySet()) {
            Object key = entry.getKey();
            parameterHelper.objectNotNull("jsonHelper-config-key", key);
            boolean value = parameterHelper.booleanFormatNotEmpty("jsonHelper-config-value", entry.getValue());
            if (key instanceof SerializationFeature) {
                objectMapper.configure((SerializationFeature) key, value);
            } else if (key instanceof DeserializationFeature) {
                objectMapper.configure((DeserializationFeature) key, value);
            } else if (key instanceof JsonParser.Feature) {
                objectMapper.configure((JsonParser.Feature) key, value);
            } else if (key instanceof JsonGenerator.Feature) {
                objectMapper.configure((JsonGenerator.Feature) key, value);
            } else {
                throw new UtilRuntimeException("Unexpected config of JsonHelper<ObjectMapper>,they should come from MapperFeature,SerializationFeature,DeserializationFeature,JsonParser.Feature,JsonGenerator.Feature.");
            }
        }
        // 时间戳处理时区默认为系统时区
        objectMapper.setTimeZone(TimeZone.getDefault());
        // 为 null 的属性默认不参与序列化
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 注册默认的时间对象处理机制
        objectMapper.registerModule(new JavaTimeModule());
        // 不使用数字类型时间戳
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }
}
