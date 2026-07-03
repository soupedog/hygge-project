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

package hygge.util.base;


import hygge.commons.exception.ParameterRuntimeException;
import hygge.util.UtilCreator;
import hygge.util.definition.CollectionHelper;
import hygge.util.definition.ParameterHelper;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Collection 处理工具类基类
 *
 * @author Xavier
 * @date 2022/7/12
 * @since 1.0
 */
public abstract class BaseCollectionHelper implements CollectionHelper {
    protected ParameterHelper parameterHelper = UtilCreator.INSTANCE.getDefaultInstance(ParameterHelper.class);

    @Override
    public void hookUnexpectedEvent(String errorMessage, Throwable throwable) {
        throw new ParameterRuntimeException(errorMessage, throwable);
    }

    @Override
    public void collectionNotEmpty(String targetName, Collection<?> target) {
        if (target == null || target.isEmpty()) {
            hookUnexpectedEvent(String.format("Unexpected %s,it can't be null or empty.", targetName), null);
        }
    }

    @Override
    public void collectionNotEmpty(Collection<?> target, String errorMessage) {
        if (target == null || target.isEmpty()) {
            hookUnexpectedEvent(errorMessage, null);
        }
    }

    @Override
    public <T> ArrayList<T> createCollection(T... targetArray) {
        if (targetArray == null) {
            return new ArrayList<>();
        }

        ArrayList<T> result = new ArrayList<>(targetArray.length);
        Collections.addAll(result, targetArray);
        return result;
    }

    @Override
    public <T> HashSet<T> createUniqueCollection(T... targetArray) {
        if (targetArray == null) {
            return new HashSet<>(0);
        }
        return new HashSet<>(Arrays.asList(targetArray));
    }

    @Override
    public <T, R> ArrayList<R> filterNonemptyItemAsArrayList(boolean enableContainCheck, Collection<T> target, Function<T, R> getItemFunction) {
        if (parameterHelper.isEmpty(target)) {
            return new ArrayList<>();
        }

        Stream<R> stream = target.stream()
                .filter(Objects::nonNull)                     // collectionItem == null 跳过
                .map(getItemFunction)                         // 转换成 R
                .filter(r -> parameterHelper.isNotEmpty(r));  // 过滤空结果

        if (enableContainCheck) {
            stream = stream.distinct();                       // 去重，O(n) 且保持顺序
        }

        return stream.collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public <T, R> HashSet<R> filterNonemptyItemAsHashSet(Collection<T> target, Function<T, R> getItemFunction) {
        if (parameterHelper.isEmpty(target)) {
            return new HashSet<>(0);
        }

        return target.stream()
                .filter(Objects::nonNull)                          // 跳过 null 元素
                .map(getItemFunction)                              // 转换
                .filter(r -> parameterHelper.isNotEmpty(r))        // 过滤空结果
                .collect(Collectors.toCollection(HashSet::new));
    }

    private <T, K, V> void initMapData(Collection<T> target, Function<T, K> kFunction, Function<T, V> vFunction, Map<K, V> result) {
        target.stream()
                .filter(Objects::nonNull)
                .forEach(item -> {
                    K key = kFunction.apply(item);
                    V value = vFunction.apply(item);
                    if (parameterHelper.isNotEmpty(key) && parameterHelper.isNotEmpty(value)) {
                        result.put(key, value);
                    }
                });
    }

    @Override
    public <T, K, V> HashMap<K, V> filterNonemptyItemAsHashMap(Collection<T> target, Function<T, K> kFunction, Function<T, V> vFunction) {
        if (parameterHelper.isEmpty(target)) {
            return new HashMap<>(0);
        }

        HashMap<K, V> result = new HashMap<>(target.size());
        initMapData(target, kFunction, vFunction, result);
        return result;
    }

    @Override
    public <T, K, V> TreeMap<K, V> filterNonemptyItemAsTreeMap(Collection<T> target, Function<T, K> kFunction, Function<T, V> vFunction) {
        return filterNonemptyItemAsTreeMap(target, null, kFunction, vFunction);
    }

    @Override
    public <T, K, V> TreeMap<K, V> filterNonemptyItemAsTreeMap(Collection<T> target, Comparator<K> comparator, Function<T, K> kFunction, Function<T, V> vFunction) {
        if (parameterHelper.isEmpty(target)) {
            return createTreeMap(comparator);
        }

        TreeMap<K, V> result = createTreeMap(comparator);
        initMapData(target, kFunction, vFunction, result);
        return result;
    }

    private <K, V> TreeMap<K, V> createTreeMap(Comparator<K> comparator) {
        if (comparator != null) {
            return new TreeMap<>(comparator);
        }
        return new TreeMap<>(Collator.getInstance());
    }

    @Override
    public <T, K, V> ConcurrentHashMap<K, V> filterNonemptyItemAsConcurrentHashMap(Collection<T> target, Function<T, K> kFunction, Function<T, V> vFunction) {
        if (parameterHelper.isEmpty(target)) {
            return new ConcurrentHashMap<>(0);
        }
        ConcurrentHashMap<K, V> result = new ConcurrentHashMap<>(target.size());
        initMapData(target, kFunction, vFunction, result);
        return result;
    }
}
