package hygge.commons.templates.container;

/**
 * 特定类型对象的容器
 *
 * @author Xavier
 * @date 2022/6/25
 * @see HyggeContext 他们之间有差别，但差别其实不太大
 * @since 1.0
 */
public interface HyggeKeeper<K, V> {
    /**
     * 检测容器中是否存在特定的 key
     */
    boolean containsKey(K key);

    /**
     * 向容器中以 key 存入特定对象
     *
     * @return 如果当前 key 已在容器中已存在，那么返回该 key 对应的旧对象
     */
    V saveValue(K key, V value);

    /**
     * 根据 key 从容器中获取对象
     */
    V getValue(K key);

    /**
     * 根据 key 从容器中获取对象
     *
     * @return key 对应的对象。如果容器中不存在该 key ，则返回 defaultValue
     */
    V getValueOfNullable(K key, V defaultValue);
}
