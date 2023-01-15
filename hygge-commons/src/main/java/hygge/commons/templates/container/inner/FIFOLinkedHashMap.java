package hygge.commons.templates.container.inner;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 基于 先进先出 淘汰策略的 Map 容器。
 * <p>
 * 容器超出容量时，会将早存入的元素进行驱逐。
 *
 * @author Xavier
 * @date 2023/1/15
 */
public class FIFOLinkedHashMap<K, V> extends LinkedHashMap<K, V> {
    private final Integer maxSize;

    public FIFOLinkedHashMap(Integer maxSize) {
        super(16, 0.75f, false);
        this.maxSize = maxSize;
    }

    public FIFOLinkedHashMap(int initialCapacity, float loadFactor, Integer maxSize) {
        super(initialCapacity, loadFactor, false);
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
