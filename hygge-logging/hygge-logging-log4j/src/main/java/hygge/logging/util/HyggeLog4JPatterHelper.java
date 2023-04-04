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

package hygge.logging.util;

import hygge.commons.exception.UtilRuntimeException;
import hygge.logging.config.configuration.base.HyggeLogConfiguration;
import hygge.logging.enums.OutputModeEnum;
import hygge.logging.util.definition.HyggeLogPatterHelper;

/**
 * 基于 log4j 的 LogPatterHelper 默认实现
 *
 * @author Xavier
 * @date 2022/6/28
 * @since 1.0
 */
public class HyggeLog4JPatterHelper implements HyggeLogPatterHelper {
    /**
     * 默认的 hygge 日志模板
     */
    public static final String HYGGE_DEFAULT_PATTERN = "%d{yyyy-MM-dd HH:mm:ss.SSS} %5p %pid --- [%15.15t] %-40.40c{1.} : %m%n%xwEx";
    /**
     * 默认的 ROOT 日志模板
     */
    public static final String ROOT_DEFAULT_PATTERN = HYGGE_DEFAULT_PATTERN;
    /**
     * 默认的 hygge 彩色日志模板
     */
    public static final String HYGGE_DEFAULT_COLORFUL_PATTERN = "%clr{%d{yyyy-MM-dd HH:mm:ss.SSS}}{faint} %clr{%5p} %clr{%pid}{cyan} %clr{---}{faint} %clr{[%15.15t]}{faint} %clr{%-40.40c{1.}}{magenta} %clr{:}{faint} %m%n%xwEx";
    /**
     * 默认的 ROOT 彩色日志模板
     */
    public static final String ROOT_DEFAULT_COLORFUL_PATTERN = "%clr{%d{yyyy-MM-dd HH:mm:ss.SSS}}{faint} %clr{%5p} %clr{%pid}{magenta} %clr{---}{faint} %clr{[%15.15t]}{faint} %clr{%-40.40c{1.}}{cyan} %clr{:}{faint} %m%n%xwEx";

    @Override
    public String createPatter(HyggeLogConfiguration configuration, boolean actualHyggeScope, boolean actualEnableColorful, OutputModeEnum actualOutputMode) {
        if (actualOutputMode.equals(OutputModeEnum.CONSOLE_AND_FILE)) {
            // 生成阶段不允许指定组合模式
            throw new UtilRuntimeException(String.format("OutputMode(%s) is not allowed in the final creation phase.", actualOutputMode));
        }

        String finalPatter;

        if (configuration.isEnableJsonType()) {
            String type = actualHyggeScope ? "hygge" : "root";

            HyggeLog4jJsonPatterHelper hyggeLog4jJsonPatterHelper = new HyggeLog4jJsonPatterHelper(
                    type,
                    configuration.getProjectName(),
                    configuration.getAppName(),
                    configuration.getVersion()
            );
            finalPatter = hyggeLog4jJsonPatterHelper.create(actualEnableColorful, configuration.getConverterMode()) + "%n";
        } else {
            // 非 json 类型
            if (actualEnableColorful) {
                finalPatter = actualHyggeScope ? HYGGE_DEFAULT_COLORFUL_PATTERN : ROOT_DEFAULT_COLORFUL_PATTERN;
            } else {
                finalPatter = actualHyggeScope ? HYGGE_DEFAULT_PATTERN : ROOT_DEFAULT_PATTERN;
            }
        }

        return finalPatter;
    }
}
