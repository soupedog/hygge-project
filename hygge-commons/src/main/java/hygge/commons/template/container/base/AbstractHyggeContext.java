package hygge.commons.template.container.base;

import hygge.commons.template.container.definition.HyggeContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 多个函数间数据交互操作的上下文容器基类(并不限制对象是否属于同一类型)
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
@SuppressWarnings("unchecked")
public abstract class AbstractHyggeContext<K> implements HyggeContext<K> {
    protected Map<K, Object> container;

    protected AbstractHyggeContext() {
        initContainer(16, 0.75F);
    }

    protected AbstractHyggeContext(int initialCapacity, float loadFactor) {
        initContainer(initialCapacity, loadFactor);
    }

    protected void initContainer(int initialCapacity, float loadFactor) {
        this.container = new ConcurrentHashMap<>(initialCapacity, loadFactor);
    }

    @Override
    public boolean containsKey(K key) {
        return container.containsKey(key);
    }

    @Override
    public <T> T saveObject(K key, Object object) {
        return (T) container.put(key, object);
    }

    @Override
    public <T> T getObject(K key) {
        return (T) container.get(key);
    }

    @Override
    public <T> T getObjectOfNullable(K key, Object defaultObject) {
        return (T) container.getOrDefault(key, defaultObject);
    }
}