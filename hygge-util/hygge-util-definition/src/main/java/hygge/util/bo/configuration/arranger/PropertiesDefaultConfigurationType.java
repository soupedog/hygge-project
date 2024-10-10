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

package hygge.util.bo.configuration.arranger;

import hygge.util.definition.analyst.ConfigurationType;

/**
 * 配置项类型默认实现
 *
 * @author Xavier
 * @date 2024/9/25
 * @since 1.0
 */
public enum PropertiesDefaultConfigurationType implements ConfigurationType {

    /**
     * 主要的(最优先展示)
     */
    PRIMARY(0),
    SPRING(100),
    /**
     * 最低展示优先级，也是配置项的默认值
     */
    DEFAULT(1000),
    ;

    private final int order;

    PropertiesDefaultConfigurationType(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return this.order;
    }
}
