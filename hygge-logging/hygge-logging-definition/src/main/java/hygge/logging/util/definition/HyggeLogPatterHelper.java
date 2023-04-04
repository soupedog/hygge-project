/*
 * Copyright 2022-2023 the original author or authors.
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

package hygge.logging.util.definition;

import hygge.logging.config.configuration.base.HyggeLogConfiguration;
import hygge.logging.enums.OutputModeEnum;
import hygge.util.definition.HyggeUtil;

/**
 * 日志模板生成器
 *
 * @author Xavier
 * @date 2022/6/28
 * @since 1.0
 */
public interface HyggeLogPatterHelper extends HyggeUtil {
    /**
     * 创建日志格式模板
     *
     * @param actualHyggeScope     实际的是否为 hygge 范围组件
     * @param actualEnableColorful 实际的是否开启彩色日志
     * @param actualOutputMode     实际的日志输出类型
     * @return 日志格式模板
     */
    String createPatter(HyggeLogConfiguration configuration, boolean actualHyggeScope, boolean actualEnableColorful, OutputModeEnum actualOutputMode);
}
