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

import hygge.commons.template.container.definition.HyggeContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 多个函数间数据交互操作的上下文容器基类(并不限制对象是否属于同一类型)
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
@SuppressWarnings("unchecked")
public abstract class AbstractHyggeContext<K> implements HyggeContext<K> {
    protected Map<K, Object> container;

    protected AbstractHyggeContext() {
        initContainer(16, 0.75F);
    }

    protected AbstractHyggeContext(int initialCapacity, float loadFactor) {
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
    public <T> T saveObject(K key, Object object) {
        return (T) container.put(key, object);
    }

    @Override
    public <T> T getObject(K key) {
        return (T) container.get(key);
    }

    @Override
    public <T> T getObjectOfNullable(K key, Object defaultObject) {
        return (T) container.getOrDefault(key, defaultObject);
    }
}
