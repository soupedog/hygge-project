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

package hygge.util.definition;

import hygge.commons.exception.UtilRuntimeException;
import hygge.commons.template.definition.HyggeConfigurator;

/**
 * json 处理工具类
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public interface JsonHelper<S> extends HyggeUtil {
    /**
     * 该枚举直接作为带 {@link java.util.Properties} 参构造方法入参的 key
     */
    enum ConfigKey {
        /**
         * 是否开启排版缩进<p/>
         * 可选值:<br/>
         * {@link Boolean#TRUE} 开启缩进(方便肉眼观察 json 结构)<br/>
         * {@link Boolean#FALSE} 关闭缩进
         */
        INDENT
    }

    /**
     * 获取默认配置工具
     */
    HyggeConfigurator<S, ?> getConfigurator();

    /**
     * 获取序列化工具实际依赖
     *
     * @return 当前序列化工具的实际依赖
     */
    S getDependence();

    /**
     * 将目标对象转化为 json 字符串，或将 json 字符串进行格式化
     *
     * @param target 目标对象
     * @return 目标对象对应的 json 字符串
     * @throws UtilRuntimeException 如果转化失败时，会抛出该异常
     */
    String formatAsString(Object target);

    /**
     * 将 json 串反序列化为目标类型对象
     *
     * @param jsonString json 串
     * @param tClass     目标类型
     * @return json 串反序列化后的对象
     * @throws UtilRuntimeException 如果转化失败时，会抛出该异常
     */
    <T> T readAsObject(String jsonString, Class<T> tClass);

    /**
     * 将 json 串反序列化为目标类型对象
     *
     * @param jsonString 待反序列化目标
     * @param classInfo  目标类信息 e.g {@link com.fasterxml.jackson.core.type.TypeReference} (基于 JsonHelper 不同具体实现，可能为任何值，JsonHelper 实现类需要自行处理)
     * @return json 串反序列化后的对象
     * @throws UtilRuntimeException 如果转化失败时，会抛出该异常
     */
    Object readAsObjectWithClassInfo(String jsonString, Object classInfo);
}
