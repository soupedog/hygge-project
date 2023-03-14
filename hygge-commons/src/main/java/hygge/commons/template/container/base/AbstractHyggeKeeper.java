package hygge.commons.template.container.base;

import hygge.commons.template.container.definition.HyggeKeeper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 特定类型对象的容器基类，常作为缓存容器
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public abstract class AbstractHyggeKeeper<K, V> implements HyggeKeeper<K, V> {
    protected Map<K, V> container;

    protected AbstractHyggeKeeper() {
        initContainer(16, 0.75F);
    }

    protected AbstractHyggeKeeper(int initialCapacity, float loadFactor) {
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
    public V saveValue(K key, V value) {
        return container.put(key, value);
    }

    @Override
    public V getValue(K key) {
        return container.get(key);
    }

    @Override
    public V getValueOfNullable(K key, V defaultValue) {
        return container.getOrDefault(key, defaultValue);
    }
}
