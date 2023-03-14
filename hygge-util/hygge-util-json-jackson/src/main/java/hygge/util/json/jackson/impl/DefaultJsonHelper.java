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
