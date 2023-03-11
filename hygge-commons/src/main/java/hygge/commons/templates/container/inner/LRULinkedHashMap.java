package hygge.commons.templates.container.inner;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 基于 最近最少使用 淘汰策略的 Map 容器。
 * <p>
 * 容器超出容量时，会将最久未使用的元素进行驱逐。
 *
 * @author Xavier
 * @date 2023/1/15
 * @since 1.0
 */
public class LRULinkedHashMap<K, V> extends LinkedHashMap<K, V> {
    private final Integer maxSize;

    public LRULinkedHashMap(Integer maxSize) {
        super(16, 0.75f, true);
        this.maxSize = maxSize;
    }

    public LRULinkedHashMap(int initialCapacity, float loadFactor, Integer maxSize) {
        super(initialCapacity, loadFactor, true);
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxSize;
    }

    public Integer getMaxSize() {
        return maxSize;
    }
}
