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

package hygge.commons.template.container.definition;

/**
 * 非特定类型对象的容器。不与 {@link HyggeKeeper} 合并成同一个接口的原因是，该接口对外暴露的方法不对容器内元素类型强制限定，而 {@link HyggeKeeper} 对外暴露方法限定容器内元素类型，这便是他们之间的区别。
 * <p>
 * 语义上该接口实例作为多个函数间数据交互操作的上下文容器
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public interface HyggeContext<K> {
    /**
     * 检测容器中是否存在特定的 key
     */
    boolean containsKey(K key);

    /**
     * 向容器中以 key 存入特定对象
     *
     * @return 如果当前 key 已在容器中已存在，那么返回该 key 对应的旧对象
     */
    <T> T saveObject(K key, Object object);

    /**
     * 根据 key 从容器中获取对象
     */
    <T> T getObject(K key);

    /**
     * 根据 key 从容器中获取对象
     *
     * @return key 对应的对象。如果容器中不存在该 key ，则返回 defaultObject
     */
    <T> T getObjectOfNullable(K key, Object defaultObject);
}
