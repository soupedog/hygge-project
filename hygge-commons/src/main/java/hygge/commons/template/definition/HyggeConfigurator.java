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

/**
 * 对特定对象进行调整的配置器
 *
 * @author Xavier
 * @date 2023/5/11
 * @since 1.0
 */
public interface HyggeConfigurator<T, C> {
    /**
     * 创建一份默认配置的
     *
     * @return 默认的配置项
     */
    C createDefaultConfig();

    /**
     * 对目标对象进行实际配置
     */
    void configure(T target, C configuration);
}
