package hygge.commons.template.container;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 特定类型对象的容器基类
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public abstract class AbstractHyggeKeeper<K, V> implements HyggeKeeper<K, V> {
    protected Map<K, V> container;

    protected AbstractHyggeKeeper() {
        initContainer();
    }

    protected void initContainer() {
        this.container = new ConcurrentHashMap<>();
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
