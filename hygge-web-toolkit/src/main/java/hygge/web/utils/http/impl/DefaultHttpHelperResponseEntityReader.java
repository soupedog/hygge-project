package hygge.web.utils.http.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import hygge.commons.exception.UtilRuntimeException;
import hygge.utils.UtilsCreator;
import hygge.utils.definitions.JsonHelper;
import hygge.web.utils.http.definitions.HttpHelperResponseEntityReader;

/**
 * 网络请求工具 Response 读取工具 默认实现
 *
 * @author Xavier
 * @date 2022/7/12
 * @since 1.0
 */
public class DefaultHttpHelperResponseEntityReader implements HttpHelperResponseEntityReader {
    private static final JsonHelper<?> jsonHelper = UtilsCreator.INSTANCE.getDefaultJsonHelperInstance(false);

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
