package hygge.commons.templates.configuration;

import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * hygge 配置项
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
interface HyggeConfiguration {
    /**
     * 获取默认优先级
     */
    int getDefaultOrder();

    /**
     * 以默认优先级 {@link HyggeConfiguration#getDefaultOrder()} 添加一个配置项
     * <p/>
     * 优先级低的配置项会被覆盖<br/>
     * 键相同且优先级相同时,先存入的配置项会被覆盖
     *
     * @param key   键
     * @param value 值
     * @return 赋值冲突时，被覆盖掉的旧键值。(可空)
     */
    HyggeConfigurationItem<?> putItem(String key, Object value);

    /**
     * 以指定的优先级添加一个配置项
     * <p/>
     * 优先级低的配置项会被覆盖<br/>
     * 键相同且优先级相同时,先存入的配置项会被覆盖
     *
     * @param key   键
     * @param value 值
     * @param order 配置项优先级，越小越优先
     * @return 赋值冲突时，原本存在的键值。新键值可能赋值成功也可能失败，原本存在的键值优先级更高的话，新键值会赋值失败。
     */
    HyggeConfigurationItem<?> putItem(String key, Object value, int order);

    /**
     * 所有配置项中是否包含特定的 key
     */
    boolean containsKey(String key);

    /**
     * 尝试用特定键获取配置项
     */
    HyggeConfigurationItem<?> getItem(String key);

    /**
     * 获取全部配置项的 key
     */
    Set<String> getKeys();

    /**
     * 获取全部配置项
     */
    Collection<HyggeConfigurationItem<?>> getItems();

    /**
     * 与另一个 hygge 配置项取并集
     *
     * @return 所有发生冲突而被覆盖的旧配置项
     */
    List<HyggeConfigurationItem<?>> mergeConfiguration(HyggeConfiguration mergeTarget);

    /**
     * 转换成 Properties
     */
    Properties toProperties();
}
