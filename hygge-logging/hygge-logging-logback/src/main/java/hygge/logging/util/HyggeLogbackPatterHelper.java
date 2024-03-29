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

package hygge.logging.util;

import hygge.commons.exception.UtilRuntimeException;
import hygge.logging.config.configuration.base.HyggeLogConfiguration;
import hygge.logging.enums.OutputModeEnum;
import hygge.logging.util.definition.HyggeLogPatterHelper;

import java.lang.management.ManagementFactory;

/**
 * 基于 logback 的 LogPatterHelper 默认实现
 *
 * @author Xavier
 * @date 2022/6/28
 * @since 1.0
 */
public class HyggeLogbackPatterHelper implements HyggeLogPatterHelper {
    /**
     * 默认的 hygge 日志模板
     */
    public static final String HYGGE_DEFAULT_PATTERN = "%d{yyyy-MM-dd HH:mm:ss.SSS} %5p %pid --- [%15.15t] %-40.40logger{39} : %m%n%wEx";
    /**
     * 默认的 ROOT 日志模板
     */
    public static final String ROOT_DEFAULT_PATTERN = HYGGE_DEFAULT_PATTERN;
    /**
     * 默认的 hygge 彩色日志模板
     */
    public static final String HYGGE_DEFAULT_COLORFUL_PATTERN = "%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(%5p) %clr(%pid){cyan} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){magenta} %clr(:){faint} %m%n%wEx";
    /**
     * 默认的 ROOT 彩色日志模板
     */
    public static final String ROOT_DEFAULT_COLORFUL_PATTERN = "%clr(%d{yyyy-MM-dd HH:mm:ss.SSS}){faint} %clr(%5p) %clr(%pid){magenta} %clr(---){faint} %clr([%15.15t]){faint} %clr(%-40.40logger{39}){cyan} %clr(:){faint} %m%n%wEx";

    @Override
    public String createPatter(HyggeLogConfiguration configuration, boolean actualHyggeScope, boolean actualEnableColorful, OutputModeEnum actualOutputMode) {
        if (actualOutputMode.equals(OutputModeEnum.CONSOLE_AND_FILE)) {
            // 生成阶段不允许指定组合模式
            throw new UtilRuntimeException(String.format("OutputMode(%s) is not allowed in the final creation phase.", actualOutputMode));
        }

        String finalPatter;

        if (configuration.getEnableJsonMode()) {
            String type = actualHyggeScope ? "hygge" : "root";

            HyggeLogbackJsonPatterHelper hyggeLogbackJsonPatterHelper = new HyggeLogbackJsonPatterHelper(
                    type,
                    configuration.getProjectName(),
                    configuration.getAppName(),
                    configuration.getVersion()
            );
            finalPatter = hyggeLogbackJsonPatterHelper.create(actualEnableColorful, configuration.getConverterMode()) + "%n";
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

    public String pidConvert(String finalPatter) {
        // 替换成实际进程号
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.split("@")[0];
        finalPatter = finalPatter.replace("%pid", pid);
        return finalPatter;
    }
}
