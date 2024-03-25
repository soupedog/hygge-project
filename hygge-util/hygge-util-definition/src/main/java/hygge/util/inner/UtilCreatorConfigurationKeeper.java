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

package hygge.util.inner;

import hygge.commons.template.container.base.AbstractHyggeKeeper;
import hygge.util.UtilCreator;

/**
 * UtilsCreator 配置项容器
 *
 * @author Xavier
 * @date 2022/6/28
 * @since 1.0
 */
public class UtilCreatorConfigurationKeeper extends AbstractHyggeKeeper<String, Object> {
    /**
     * 基于 jackson 的 JsonHelper 默认实现类
     */
    public static final String DEFAULT_JACKSON_JSON_HELPER_CLASS_NAME = "hygge.util.json.jackson.impl.DefaultJsonHelper";
    /**
     * 如果需要修改默认 JsonHelper 实现类，请用新实现类全类名覆盖这个 key 的值
     *
     * @see UtilCreator#getDefaultJacksonJsonHelperPath()
     */
    public static final String KEY_ACTUAL_DEFAULT_JSON_HELPER = "ACTUAL_DEFAULT_JSON_HELPER";

    public UtilCreatorConfigurationKeeper() {
        super();
        saveValue(KEY_ACTUAL_DEFAULT_JSON_HELPER, DEFAULT_JACKSON_JSON_HELPER_CLASS_NAME);
    }
}
