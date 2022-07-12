package hygge.utils.definitions;


import hygge.utils.HyggeUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Collection 处理工具类
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public interface CollectionHelper extends HyggeUtil {
    /**
     * 默认异常处理函数
     *
     * @param errorMessage 不符合限制条件时的异常提示
     * @param throwable    触发非预期事件的异常(可能为空)
     */
    void hookUnexpectedEvent(String errorMessage, Throwable throwable);

    /**
     * 要求目标对象不为 null 且元素不为空
     *
     * @param targetName 集合名称(用于输出默认异常信息)
     * @param target     待验证的入参
     */
    void collectionNotEmpty(String targetName, Collection<?> target);

    /**
     * 要求目标对象不为 null 且元素不为空
     *
     * @param target       待验证的入参
     * @param errorMessage 不符合限制条件时的异常提示
     */
    void collectionNotEmpty(Collection<?> target, String errorMessage);

    /**
     * 便捷创建默认的 Collection 实例
     *
     * @param targetArray 用于构建的元素
     * @param <T>         元素类型
     * @return Collection 实例(ArrayList)
     */
    <T> ArrayList<T> createCollection(T... targetArray);

    /**
     * 便捷创建元素不重复的 Collection 实例
     *
     * @param targetArray 用于构建的元素
     * @param <T>         元素类型
     * @return Collection 实例(HashSet)
     */
    <T> HashSet<T> createUniqueCollection(T... targetArray);

    /**
     * 根据传入规则过滤 Collection ,将目标 Collection 中非空元素转化成新对象组成 ArrayList
     *
     * @param enableContainCheck 是否开启是否已包含检查。开启时，存入元素将先用 {@link ArrayList#contains(Object)} 检查，返回 false 时才真正进行添加
     * @param target             待过滤的 Collection
     * @param getItemFunction    新对象构造函数
     * @param <T>                Collection 元素类型
     * @param <R>                Collection 元素的目标属性类型
     * @return 目标 Collection 中非空元素转化成新对象组成的 ArrayList
     */
    <T, R> ArrayList<R> filterNonemptyItemAsArrayList(boolean enableContainCheck, Collection<T> target, Function<T, R> getItemFunction);

    /**
     * 根据传入规则过滤 Collection ,将目标 Collection 中非空元素转化成新对象组成 HashSet
     *
     * @param target          待过滤的 Collection
     * @param getItemFunction 新对象构造函数
     * @param <T>             Collection 元素类型
     * @param <R>             Collection 元素的目标属性类型
     * @return 目标 Collection 中非空元素转化成新对象组成的 ArrayList
     */
    <T, R> HashSet<R> filterNonemptyItemAsHashSet(Collection<T> target, Function<T, R> getItemFunction);

    /**
     * 根据传入规则过滤 Collection ,将目标 Collection 中非空元素转化成新对象组成 HashMap
     *
     * @param target    待过滤的 Collection
     * @param kFunction 提取 HashMap Key 函数
     * @param vFunction 提取 HashMap Value 函数
     * @param <T>       Collection 元素类型
     * @param <K>       HashMap 的 key 类型
     * @param <V>       HashMap 的 value 类型
     * @return 目标 Collection 中非空元素转化成新对象组成的 HashMap
     */
    <T, K, V> HashMap<K, V> filterNonemptyItemAsHashMap(Collection<T> target, Function<T, K> kFunction, Function<T, V> vFunction);

    /**
     * 根据传入规则过滤 Collection ,将目标 Collection 中非空元素转化成新对象组成 TreeMap
     *
     * @param target    待过滤的 Collection
     * @param kFunction 提取 TreeMap Key 函数
     * @param vFunction 提取 TreeMap Value 函数
     * @param <T>       Collection 元素类型
     * @param <K>       TreeMap 的 key 类型
     * @param <V>       TreeMap 的 value 类型
     * @return 目标 Collection 中非空元素转化成新对象组成的 TreeMap
     */
    <T, K, V> TreeMap<K, V> filterNonemptyItemAsTreeMap(Collection<T> target, Function<T, K> kFunction, Function<T, V> vFunction);

    /**
     * 根据传入规则过滤 Collection ,将目标 Collection 中非空元素转化成新对象组成 TreeMap
     *
     * @param target     待过滤的 Collection
     * @param comparator 目标 Collection 中非空元素转化成新对象组成的 TreeMap 排序规则
     * @param kFunction  提取 TreeMap Key 函数
     * @param vFunction  提取 TreeMap Value 函数
     * @param <T>        Collection 元素类型
     * @param <K>        TreeMap 的 key 类型
     * @param <V>        TreeMap 的 value 类型
     * @return 目标 Collection 中非空元素转化成新对象组成的 TreeMap
     */
    <T, K, V> TreeMap<K, V> filterNonemptyItemAsTreeMap(Collection<T> target, Comparator<K> comparator, Function<T, K> kFunction, Function<T, V> vFunction);

    /**
     * 根据传入规则过滤 Collection ,将目标 Collection 中非空元素转化成新对象组成 ConcurrentHashMap
     *
     * @param target    待过滤的 Collection
     * @param kFunction 提取 ConcurrentHashMap Key 函数
     * @param vFunction 提取 ConcurrentHashMap Value 函数
     * @param <T>       Collection 元素类型
     * @param <K>       ConcurrentHashMap 的 key 类型
     * @param <V>       ConcurrentHashMap 的 value 类型
     * @return 目标 Collection 中非空元素转化成新对象组成的 ConcurrentHashMap
     */
    <T, K, V> ConcurrentHashMap<K, V> filterNonemptyItemAsConcurrentHashMap(Collection<T> target, Function<T, K> kFunction, Function<T, V> vFunction);
}
