package hygge.utils.json.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import hygge.commons.templates.core.HyggeLogInfoObject;
import hygge.utils.UtilsCreator;
import org.springframework.util.StringUtils;

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

    static {
        Object defaultMapperTemp = UtilsCreator.INSTANCE.getDefaultJsonHelperInstance(false).getDependence();
        if (defaultMapperTemp instanceof ObjectMapper) {
            mapper.setConfig(((ObjectMapper) defaultMapperTemp).getSerializationConfig());
            mapper.setConfig(((ObjectMapper) defaultMapperTemp).getDeserializationConfig());
        }
        mapper.registerModule(new HyggeLogInfoModule());
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value instanceof HyggeLogInfoObject) {
            String jsonInfo = ((HyggeLogInfoObject) value).toJsonLogInfo();
            if (StringUtils.hasText(jsonInfo)) {
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
