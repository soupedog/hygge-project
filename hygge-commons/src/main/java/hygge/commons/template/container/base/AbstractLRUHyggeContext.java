/*
 * Copyright 2022-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hygge.commons.template.container.base;

import hygge.commons.template.container.inner.FIFOLinkedHashMap;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 拥有最近最少使用淘汰策略的 HyggeContext
 *
 * @author Xavier
 * @date 2023/3/14
 * @since 1.0
 */
public abstract class AbstractLRUHyggeContext<K> extends AbstractHyggeContext<K> {
    protected ReentrantReadWriteLock readWriteLock;
    protected Integer maxSize;

    protected AbstractLRUHyggeContext(Integer maxSize) {
        this.readWriteLock = new ReentrantReadWriteLock();
        this.maxSize = maxSize;
    }

    protected AbstractLRUHyggeContext(int initialCapacity, float loadFactor, Integer maxSize) {
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
