package hygge.commons.templates.container.impl;

import hygge.commons.templates.container.base.AbstractHyggeContext;
import hygge.commons.templates.container.inner.FIFOLinkedHashMap;

/**
 * 拥有最近最少使用淘汰策略的 HyggeContext 的默认实现
 *
 * @author Xavier
 * @date 2023/1/15
 * @since 1.0
 */
public class LRUHyggeContext<K> extends AbstractHyggeContext<K> {
    private final Integer maxSize;

    public LRUHyggeContext(Integer maxSize) {
        this.maxSize = maxSize;
    }

    public LRUHyggeContext(int initialCapacity, float loadFactor, Integer maxSize) {
        super(initialCapacity, loadFactor);
        this.maxSize = maxSize;
    }

    @Override
    protected void initContainer(int initialCapacity, float loadFactor) {
        this.container = new FIFOLinkedHashMap<>(initialCapacity, loadFactor, maxSize);
    }
}
