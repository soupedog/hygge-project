package hygge.commons.template.container;

/**
 * 多个函数间数据交互操作的上下文容器(并不限制对象是否属于同一类型)
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public interface HyggeContext<K> {
    /**
     * 检测容器中是否存在特定的 key
     */
    boolean containsKey(K key);

    /**
     * 向容器中以 key 存入特定对象
     *
     * @return 如果当前 key 已在容器中已存在，那么返回该 key 对应的旧对象
     */
    <T> T saveObject(K key, Object object);

    /**
     * 根据 key 从容器中获取对象
     */
    <T> T getObject(K key);

    /**
     * 根据 key 从容器中获取对象
     *
     * @return key 对应的对象。如果容器中不存在该 key ，则返回 {@code defaultObject}
     */
    <T> T getObjectOfNullable(K key, Object defaultObject);
}
