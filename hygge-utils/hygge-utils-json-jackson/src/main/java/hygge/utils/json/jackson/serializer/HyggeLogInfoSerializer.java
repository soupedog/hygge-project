package hygge.utils.json.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import hygge.commons.templates.core.HyggeLogInfoObject;

import java.io.IOException;

/**
 * Hygge 日志相关对象序列化器
 *
 * @author Xavier
 * @date 2022/7/12
 * @since 1.0
 */
public class HyggeLogInfoSerializer extends JsonSerializer<Object> {
    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value instanceof HyggeLogInfoObject) {
            gen.writeRaw(((HyggeLogInfoObject) value).toJsonLogInfo());
        } else {
            gen.writeObject(value);
        }
    }
}
