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

import hygge.logging.enums.ConverterModeEnum;
import hygge.logging.util.base.HyggeLogJsonPatterHelper;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * logback json 日志模板生成工具
 *
 * @author Xavier
 * @date 2022/7/2
 * @since 1.0
 */
@SuppressWarnings("java:S1192")
public class HyggeLogbackJsonPatterHelper extends HyggeLogJsonPatterHelper {
    public HyggeLogbackJsonPatterHelper(String type, String projectName, String appName, String version) {
        super(type, projectName, appName, version);
    }

    @Override
    public String getLevel(boolean enableColorful, ConverterModeEnum converterMode) {
        return enableColorful ? "%clr(%p)" : "%p";
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
        String hostName;
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ignored) {
            hostName = "unknown";
        }
        return hostName;
    }

    @Override
    public String getPid(boolean enableColorful, ConverterModeEnum converterMode) {
        return enableColorful ? "%clr(%pid){magenta}" : "%pid";
    }

    @Override
    public String getThread(boolean enableColorful, ConverterModeEnum converterMode) {
        return "%t";
    }

    @Override
    public String getClassPath(boolean enableColorful, ConverterModeEnum converterMode) {
        return enableColorful ? "%clr(%-40.40logger{39}){cyan}" : "%logger{39}";
    }

    @Override
    public String getTs(boolean enableColorful, ConverterModeEnum converterMode) {
        return "%d";
    }

    @Override
    public String getMsg(boolean enableColorful, ConverterModeEnum converterMode) {
        return "%m";
    }

    @Override
    public String getThrown(boolean enableColorful, ConverterModeEnum converterMode) {
        return enableColorful ? "%clr(%ex){red}" : "%ex";
    }
}
