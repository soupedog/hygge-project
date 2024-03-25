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

package hygge.commons.template.container.inner;

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
