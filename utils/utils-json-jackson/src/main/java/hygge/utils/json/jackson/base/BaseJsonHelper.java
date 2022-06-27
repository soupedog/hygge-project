package hygge.utils.json.jackson.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import hygge.commons.exceptions.UtilRuntimeException;
import hygge.utils.InfoMessageSupplier;
import hygge.utils.UtilsCreator;
import hygge.utils.definitions.JsonHelper;
import hygge.utils.definitions.ParameterHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 基于 jackson 的 json 工具类基类
 *
 * @author Xavier
 * @date 2022/6/28
 * @since 1.0
 */
public abstract class BaseJsonHelper implements JsonHelper<ObjectMapper>, InfoMessageSupplier {
    protected ParameterHelper parameterHelper;
    protected ObjectMapper objectMapper;

    protected BaseJsonHelper(Properties properties) {
        this.parameterHelper = UtilsCreator.INSTANCE.getDefaultInstance(ParameterHelper.class);
        initConfig(properties, new ObjectMapper());
    }

    protected BaseJsonHelper(Properties properties, ObjectMapper objectMapper) {
        this.parameterHelper = UtilsCreator.INSTANCE.getDefaultInstance(ParameterHelper.class);
        initConfig(properties, objectMapper);
    }

    protected BaseJsonHelper(Properties properties, ObjectMapper objectMapper, ParameterHelper parameterHelper) {
        this.parameterHelper = parameterHelper;
        initConfig(properties, objectMapper);
    }

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

    protected void initConfig(Properties properties, ObjectMapper objectMapper) {
        parameterHelper.objectNotNull("jsonHelper-objectMapper", objectMapper);

        Properties inputProperties = parameterHelper.parseObjectOfNullable("jsonHelper-properties", properties, new Properties());
        if (inputProperties.containsKey(ConfigKey.INDENT)) {
            // 是否进行排版缩进
            inputProperties.put(SerializationFeature.INDENT_OUTPUT, parameterHelper.booleanFormatNotEmpty(ConfigKey.INDENT.toString(), inputProperties.get(ConfigKey.INDENT)));
            inputProperties.remove(ConfigKey.INDENT);
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
        // 为 null 的属性默认不参与序列化
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 注册默认的时间对象处理机制
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_WITH_ZONE_ID, false);
        this.objectMapper = objectMapper;
    }
}
