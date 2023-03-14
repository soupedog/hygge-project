package hygge.utils.base;


import hygge.commons.exception.ParameterRuntimeException;
import hygge.utils.UtilsCreator;
import hygge.utils.definitions.CollectionHelper;
import hygge.utils.definitions.ParameterHelper;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Collection 处理工具类基类
 *
 * @author Xavier
 * @date 2022/7/12
 * @since 1.0
 */
public abstract class BaseCollectionHelper implements CollectionHelper {
    protected ParameterHelper parameterHelper = UtilsCreator.INSTANCE.getDefaultInstance(ParameterHelper.class);

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
        ArrayList<T> result = new ArrayList<>(targetArray.length);
        Collections.addAll(result, targetArray);
        return result;
    }

    @Override
    public <T> HashSet<T> createUniqueCollection(T... targetArray) {
        HashSet<T> result = new HashSet<>(targetArray.length);
        Collections.addAll(result, targetArray);
        return result;
    }

    @Override
    public <T, R> ArrayList<R> filterNonemptyItemAsArrayList(boolean enableContainCheck, Collection<T> target, Function<T, R> getItemFunction) {
        if (parameterHelper.isEmpty(target)) {
            return new ArrayList<>(0);
        }

        ArrayList<R> result = new ArrayList<>(target.size());
        for (T collectionItem : target) {
            if (collectionItem == null) {
                continue;
            }

            R resultItem = getItemFunction.apply(collectionItem);
            if (parameterHelper.isNotEmpty(resultItem)) {
                if (enableContainCheck) {
                    if (!result.contains(resultItem)) {
                        result.add(resultItem);
                    }
                } else {
                    result.add(resultItem);
                }
            }
        }
        return result;
    }

    @Override
    public <T, R> HashSet<R> filterNonemptyItemAsHashSet(Collection<T> target, Function<T, R> getItemFunction) {
        HashSet<R> result = new HashSet<>();

        if (parameterHelper.isEmpty(target)) {
            return result;
        }

        for (T collectionItem : target) {
            if (collectionItem == null) {
                continue;
            }
            R resultItem = getItemFunction.apply(collectionItem);
            result.add(resultItem);
        }
        return result;
    }

    @Override
    public <T, K, V> HashMap<K, V> filterNonemptyItemAsHashMap(Collection<T> target, Function<T, K> kFunction, Function<T, V> vFunction) {
        if (parameterHelper.isEmpty(target)) {
            return new HashMap<>(0);
        }

        HashMap<K, V> result = new HashMap<>(target.size());
        for (T collectionItem : target) {
            if (collectionItem == null) {
                continue;
            }
            K key = kFunction.apply(collectionItem);
            V value = vFunction.apply(collectionItem);
            result.put(key, value);
        }
        return result;
    }

    @Override
    public <T, K, V> TreeMap<K, V> filterNonemptyItemAsTreeMap(Collection<T> target, Function<T, K> kFunction, Function<T, V> vFunction) {
        return filterNonemptyItemAsTreeMap(target, null, kFunction, vFunction);
    }

    @Override
    public <T, K, V> TreeMap<K, V> filterNonemptyItemAsTreeMap(Collection<T> target, Comparator<K> comparator, Function<T, K> kFunction, Function<T, V> vFunction) {
        TreeMap<K, V> result;
        if (parameterHelper.isNotEmpty(comparator)) {
            result = new TreeMap<>(comparator);
        } else {
            result = new TreeMap<>(Collator.getInstance());
        }
        if (parameterHelper.isEmpty(target)) {
            return result;
        }

        for (T collectionItem : target) {
            if (collectionItem == null) {
                continue;
            }
            K key = kFunction.apply(collectionItem);
            V value = vFunction.apply(collectionItem);
            if (key != null) {
                result.put(key, value);
            }
        }
        return result;
    }

    @Override
    public <T, K, V> ConcurrentHashMap<K, V> filterNonemptyItemAsConcurrentHashMap(Collection<T> target, Function<T, K> kFunction, Function<T, V> vFunction) {
        parameterHelper.objectNotNull("collectionTarget", target);
        ConcurrentHashMap<K, V> result = new ConcurrentHashMap<>(target.size());
        for (T collectionItem : target) {
            if (collectionItem == null) {
                continue;
            }
            K key = kFunction.apply(collectionItem);
            V value = vFunction.apply(collectionItem);
            if (key != null && value != null) {
                result.put(key, value);
            }
        }
        return result;
    }
}
