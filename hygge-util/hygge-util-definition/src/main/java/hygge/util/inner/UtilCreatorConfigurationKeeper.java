package hygge.util.inner;

import hygge.commons.template.container.base.AbstractHyggeKeeper;
import hygge.util.UtilCreator;

/**
 * UtilsCreator 配置项容器
 *
 * @author Xavier
 * @date 2022/6/28
 * @since 1.0
 */
public class UtilCreatorConfigurationKeeper extends AbstractHyggeKeeper<String, Object> {
    /**
     * 基于 jackson 的 JsonHelper 默认实现类
     */
    public static final String DEFAULT_JACKSON_JSON_HELPER_CLASS_NAME = "hygge.utils.json.jackson.impl.DefaultJsonHelper";
    /**
     * 如果需要修改默认 JsonHelper 实现类，请用新实现类全类名覆盖这个 key 的值
     *
     * @see UtilCreator#getDefaultJacksonJsonHelperPath()
     */
    public static final String KEY_ACTUAL_DEFAULT_JSON_HELPER = "ACTUAL_DEFAULT_JSON_HELPER";

    public UtilCreatorConfigurationKeeper() {
        super();
        saveValue(KEY_ACTUAL_DEFAULT_JSON_HELPER, DEFAULT_JACKSON_JSON_HELPER_CLASS_NAME);
    }
}
