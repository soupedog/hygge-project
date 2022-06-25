package hygge.utils.definitions;

import hygge.utils.HyggeUtil;

import java.util.Properties;

/**
 * json 处理工具类
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public interface JsonHelper<S> extends HyggeUtil {
    /**
     * 该枚举直接作为带 {@link java.util.Properties} 参构造方法入参的 key
     */
    enum ConfigKey {
        /**
         * 是否开启排版缩进<p/>
         * 值: {@link Boolean#TRUE} / {@link Boolean#FALSE}
         */
        INDENT
    }

    /**
     * 创建一份默认配置的
     *
     * @return 默认的配置项
     */
    Properties createDefaultConfig();

    /**
     * 获取序列化工具
     *
     * @return 返回当前 序列化工具依赖
     */
    S getDependence();

    /**
     * 格式化对象或者字符串
     *
     * @param target 待格式化为 json 的目标
     * @return json 格式化的字符串
     */
    String formatAsString(Object target);

    /**
     * 将 json 串 反序列化为对象
     *
     * @param jsonString 待反序列化目标
     * @param tClass     目标类
     * @param <T>        目标类泛型
     * @return json 串反序列化后的目标
     */
    <T> T readAsObject(String jsonString, Class<T> tClass);

    /**
     * 将 json 串 反序列化为对象
     *
     * @param jsonString 待反序列化目标
     * @param classInfo  目标类信息 e.g {@link com.fasterxml.jackson.core.type.TypeReference} (基于 JsonHelper 不同具体实现，可能为任何值，JsonHelper 实现类需要自行处理)
     * @return json 串反序列化后的目标
     */
    Object readAsObjectWithClassInfo(String jsonString, Object classInfo);
}