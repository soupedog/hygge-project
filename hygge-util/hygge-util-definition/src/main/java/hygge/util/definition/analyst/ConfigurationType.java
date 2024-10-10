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

/**
 * 配置项类型
 *
 * @author Xavier
 * @date 2024/9/25
 * @since 1.0
 */
public interface ConfigurationType {
    /**
     * 获取优先级，越小越优先展示(有效值不应小于 0)
     */
    int getOrder();
}
