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

import hygge.logging.enums.ConverterModeEnum;
import hygge.logging.util.base.HyggeLogJsonPatterHelper;

/**
 * log4j json 日志模板生成工具
 *
 * @author Xavier
 * @date 2022/7/2
 * @since 1.0
 */
@SuppressWarnings("java:S1192")
public class HyggeLog4jJsonPatterHelper extends HyggeLogJsonPatterHelper {
    public HyggeLog4jJsonPatterHelper(String type, String projectName, String appName, String version) {
        super(type, projectName, appName, version);
    }

    @Override
    public String getLevel(boolean enableColorful, ConverterModeEnum converterMode) {
        return enableColorful ? "%clr{%p}" : "%p";
    }

    @Override
    public String getType(boolean enableColorful, ConverterModeEnum converterMode) {
        return getType();
    }

    @Override
    public String getProjectName(boolean enableColorful, ConverterModeEnum converterMode) {
        return getProjectName();
    }

    @Override
    public String getAppName(boolean enableColorful, ConverterModeEnum converterMode) {
        return getAppName();
    }

    @Override
    public String getVersion(boolean enableColorful, ConverterModeEnum converterMode) {
        return getVersion();
    }

    @Override
    public String getHost(boolean enableColorful, ConverterModeEnum converterMode) {
        return "${hostName}";
    }

    @Override
    public String getPid(boolean enableColorful, ConverterModeEnum converterMode) {
        return enableColorful ? "%clr{%pid}{magenta}" : "%pid";
    }

    @Override
    public String getThread(boolean enableColorful, ConverterModeEnum converterMode) {
        return "%t";
    }

    @Override
    public String getClassPath(boolean enableColorful, ConverterModeEnum converterMode) {
        return enableColorful ? "%clr{%c{1.}}{blue}" : "%c{1.}";
    }

    @Override
    public String getTs(boolean enableColorful, ConverterModeEnum converterMode) {
        return "%d{UNIX_MILLIS}{UTC}";
    }

    @Override
    public String getMsg(boolean enableColorful, ConverterModeEnum converterMode) {
        if (enableColorful) {
            switch (converterMode) {
                case ESCAPE:
                    return "%clr{%" + ConverterModeEnum.ESCAPE.getConverterKey() + "{%m}}{cyan}";
                case JSON_FRIENDLY:
                    return "%clr{%" + ConverterModeEnum.JSON_FRIENDLY.getConverterKey() + "{%m}}{cyan}";
                default:
                    return "%clr{%m}{cyan}";
            }
        } else {
            switch (converterMode) {
                case ESCAPE:
                    return "%" + ConverterModeEnum.ESCAPE.getConverterKey() + "{%m}";
                case JSON_FRIENDLY:
                    return "%" + ConverterModeEnum.JSON_FRIENDLY.getConverterKey() + "{%m}";
                default:
                    return "%m";
            }
        }
    }

    @Override
    public String getThrown(boolean enableColorful, ConverterModeEnum converterMode) {
        if (enableColorful) {
            switch (converterMode) {
                case ESCAPE:
                    return "%clr{%" + ConverterModeEnum.ESCAPE.getConverterKey() + "{%xwEx}}{red}";
                case JSON_FRIENDLY:
                    return "%clr{%" + ConverterModeEnum.JSON_FRIENDLY.getConverterKey() + "{%xwEx}}{red}";
                default:
                    return "%clr{%xwEx}{red}";
            }
        } else {
            switch (converterMode) {
                case ESCAPE:
                    return "%" + ConverterModeEnum.ESCAPE.getConverterKey() + "{%xwEx}";
                case JSON_FRIENDLY:
                    return "%" + ConverterModeEnum.JSON_FRIENDLY.getConverterKey() + "{%xwEx}";
                default:
                    return "%xwEx";
            }
        }
    }
}
