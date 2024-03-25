/*
 * Copyright 2022-2024 the original author or authors.
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
