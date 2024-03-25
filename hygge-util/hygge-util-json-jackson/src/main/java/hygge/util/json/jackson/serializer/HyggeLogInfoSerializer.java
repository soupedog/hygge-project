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

package hygge.util.json.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import hygge.commons.exception.UtilRuntimeException;
import hygge.commons.template.definition.HyggeLogInfoObject;
import hygge.util.UtilCreator;
import hygge.util.definition.JsonHelper;
import hygge.util.definition.ParameterHelper;
import hygge.util.json.jackson.impl.HyggeObjectMapperDefaultConfigurator;

import java.io.IOException;

/**
 * Hygge 日志相关对象序列化器
 *
 * @author Xavier
 * @date 2022/7/12
 * @since 1.0
 */
public class HyggeLogInfoSerializer extends JsonSerializer<Object> {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final ParameterHelper parameterHelper = UtilCreator.INSTANCE.getDefaultInstance(ParameterHelper.class);

    static {
        JsonHelper<?> jsonHelper = UtilCreator.INSTANCE.getDefaultJsonHelperInstance(false);
        Object defaultMapperTemp = jsonHelper.getDependence();
        if (!(defaultMapperTemp instanceof ObjectMapper)) {
            throw new UtilRuntimeException("JsonHelper must be implemented by Jackson, but currently it is " + defaultMapperTemp.getClass().getName() + ".");
        }

        HyggeObjectMapperDefaultConfigurator configurator = (HyggeObjectMapperDefaultConfigurator) jsonHelper.getConfigurator();
        configurator.configure(mapper, configurator.createDefaultConfig());
        mapper.registerModule(new HyggeLogInfoModule());
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value instanceof HyggeLogInfoObject) {
            String jsonInfo = ((HyggeLogInfoObject) value).toJsonLogInfo();
            if (parameterHelper.isNotEmpty(jsonInfo)) {
                gen.writeRawValue(jsonInfo);
            } else {
                // "JsonInclude.Include.NON_NULL" 之类的配置项，影响到的是此处 value 不为 null 的才会进入这个 JsonSerializer
                // value 是真的不为 null 导致没法把 key 抹除了(gen 不写入会报错)
                gen.writeNull();
            }
        } else {
            mapper.writeValue(gen, value);
        }
    }
}
