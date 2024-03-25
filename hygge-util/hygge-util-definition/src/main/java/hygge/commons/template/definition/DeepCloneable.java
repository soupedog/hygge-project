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

package hygge.commons.template.definition;


import hygge.util.UtilCreator;
import hygge.util.definition.JsonHelper;

/**
 * 可深度克隆接口
 *
 * @author Xavier
 * @date 2022/6/27
 * @since 1.0
 */
public interface DeepCloneable<T> {

    /**
     * 构造当前对象的一个深度克隆
     * </p>
     * 默认基于 Json 序列化反序列化的来实现深度克，请确保当前对象可被 Json 方式序列化与反序列化。<br/>
     * 如对性能有要求，请自行重写 {@link DeepCloneable#deepClone()}。
     *
     * @return 深度克隆的当前对象
     */
    @SuppressWarnings("unchecked")
    default T deepClone() {
        T target = (T) this;
        JsonHelper<?> jsonHelper = UtilCreator.INSTANCE.getDefaultJsonHelperInstance(false);
        return (T) jsonHelper.readAsObject(jsonHelper.formatAsString(this), target.getClass());
    }
}
