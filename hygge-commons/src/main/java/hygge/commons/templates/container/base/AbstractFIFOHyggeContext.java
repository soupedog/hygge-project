package hygge.commons.templates.container.base;

import hygge.commons.templates.container.inner.FIFOLinkedHashMap;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 拥有先进先出淘汰策略的 HyggeContext
 *
 * @author Xavier
 * @date 2023/3/14
 * @since 1.0
 */
public abstract class AbstractFIFOHyggeContext<K> extends AbstractHyggeContext<K> {
    protected ReentrantReadWriteLock readWriteLock;
    protected Integer maxSize;

    protected AbstractFIFOHyggeContext(Integer maxSize) {
        this.readWriteLock = new ReentrantReadWriteLock();
        this.maxSize = maxSize;
    }

    protected AbstractFIFOHyggeContext(int initialCapacity, float loadFactor, Integer maxSize) {
        super(initialCapacity, loadFactor);
        this.readWriteLock = new ReentrantReadWriteLock();
        this.maxSize = maxSize;
    }

    @Override
    protected void initContainer(int initialCapacity, float loadFactor) {
        this.container = new FIFOLinkedHashMap<>(initialCapacity, loadFactor, maxSize);
    }

    @Override
    public boolean containsKey(K key) {
        readWriteLock.readLock().lock();
        try {
            return super.containsKey(key);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public <T> T saveObject(K key, Object object) {
        readWriteLock.writeLock().lock();
        try {
            return super.saveObject(key, object);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public <T> T getObject(K key) {
        readWriteLock.readLock().lock();
        try {
            return super.getObject(key);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public <T> T getObjectOfNullable(K key, Object defaultObject) {
        readWriteLock.readLock().lock();
        try {
            return super.getObjectOfNullable(key, defaultObject);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }
}
