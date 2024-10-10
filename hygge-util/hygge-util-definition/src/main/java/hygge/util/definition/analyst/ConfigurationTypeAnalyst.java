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

package hygge.util.definition.analyst;

import hygge.util.bo.configuration.arranger.PropertiesArrangerConfiguration;

/**
 * 配置项 Type 分析器
 *
 * @author Xavier
 * @date 2024/9/25
 * @since 1.0
 */
public interface ConfigurationTypeAnalyst {
    /**
     * 该分析器标记配置项的优先级，越小越优先。
     * <p>
     * 多个分析器 A/B 且 A 返回的 ConfigurationType 优先级高于 B，他们均对配置项 X 进行了标记时，B 标记结果被 A 分析器覆盖
     */
    ConfigurationType typeResult();

    /**
     * 对目标进行分析，判断键值是否需要被标记为 {@link ConfigurationTypeAnalyst#typeResult()} 配置项类型
     */
    boolean isMatched(PropertiesArrangerConfiguration configuration, String key);
}
