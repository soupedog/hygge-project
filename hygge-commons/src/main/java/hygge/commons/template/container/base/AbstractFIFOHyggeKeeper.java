package hygge.commons.template.container.base;

import hygge.commons.template.container.inner.FIFOLinkedHashMap;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 拥有先进先出淘汰策略的 HyggeKeeper
 *
 * @author Xavier
 * @date 2023/3/14
 * @since 1.0
 */
public abstract class AbstractFIFOHyggeKeeper<K, V> extends AbstractHyggeKeeper<K, V> {
    protected ReentrantReadWriteLock readWriteLock;
    protected Integer maxSize;

    protected AbstractFIFOHyggeKeeper(Integer maxSize) {
        this.readWriteLock = new ReentrantReadWriteLock();
        this.maxSize = maxSize;
    }

    protected AbstractFIFOHyggeKeeper(int initialCapacity, float loadFactor, Integer maxSize) {
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
    public V saveValue(K key, V value) {
        readWriteLock.writeLock().lock();
        try {
            return super.saveValue(key, value);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public V getValue(K key) {
        readWriteLock.readLock().lock();
        try {
            return super.getValue(key);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public V getValueOfNullable(K key, V defaultValue) {
        readWriteLock.readLock().lock();
        try {
            return super.getValueOfNullable(key, defaultValue);
        } finally {
            readWriteLock.readLock().unlock();
        }
    }
}
