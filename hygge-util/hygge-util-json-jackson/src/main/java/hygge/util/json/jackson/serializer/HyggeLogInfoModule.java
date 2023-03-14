package hygge.util.json.jackson.serializer;

import com.fasterxml.jackson.databind.module.SimpleModule;
import hygge.commons.template.definition.HyggeLogInfoObject;

/**
 * 用于支持 {@link HyggeLogInfoObject} 对象序列化
 *
 * @author Xavier
 * @date 2022/7/14
 * @since 1.0
 */
public class HyggeLogInfoModule extends SimpleModule {
    public HyggeLogInfoModule() {
        super();
        this.addSerializer(HyggeLogInfoObject.class, new HyggeLogInfoSerializer());
    }
}
