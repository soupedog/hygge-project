package hygge.commons.templates.container.impl;

import hygge.commons.templates.container.base.AbstractHyggeContext;
import hygge.commons.templates.container.inner.FIFOLinkedHashMap;

/**
 * 拥有先进先出淘汰策略的 HyggeContext 的默认实现
 *
 * @author Xavier
 * @date 2023/1/15
 * @since 1.0
 */
public class FIFOHyggeContext<K> extends AbstractHyggeContext<K> {
    private final Integer maxSize;

    public FIFOHyggeContext(Integer maxSize) {
        this.maxSize = maxSize;
    }

    public FIFOHyggeContext(int initialCapacity, float loadFactor, Integer maxSize) {
        super(initialCapacity, loadFactor);
        this.maxSize = maxSize;
    }

    @Override
    protected void initContainer(int initialCapacity, float loadFactor) {
        this.container = new FIFOLinkedHashMap<>(initialCapacity, loadFactor, maxSize);
    }
}
