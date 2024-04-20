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

package hygge.commons.spring;

import hygge.commons.spring.enums.DeploymentEnvironmentEnum;
import hygge.commons.spring.listener.HyggeSpringContextInitListener;
import hygge.util.UtilCreator;
import hygge.util.definition.UnitConvertHelper;
import org.springframework.boot.SpringBootVersion;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Locale;
import java.util.TimeZone;

/**
 * Hygge Spring 环境下系统上下文
 *
 * @author Xavier
 * @date 2022/6/28
 * @since 1.0
 */
public class HyggeSpringContext {
    /**
     * 当前部署环境
     *
     * @see HyggeSpringContextInitListener
     */
    private static volatile DeploymentEnvironmentEnum deploymentEnvironment;
    /**
     * 应用名称
     *
     * @see HyggeSpringContextInitListener
     */
    private static volatile String appName;
    /**
     * 系统环境变量
     *
     * @see HyggeSpringContextInitListener
     */
    private static ConfigurableEnvironment configurableEnvironment;

    public static DeploymentEnvironmentEnum getDeploymentEnvironment() {
        return deploymentEnvironment;
    }

    public static void setDeploymentEnvironment(DeploymentEnvironmentEnum deploymentEnvironment) {
        HyggeSpringContext.deploymentEnvironment = deploymentEnvironment;
    }

    public static String getAppName() {
        return appName;
    }

    public static void setAppName(String appName) {
        HyggeSpringContext.appName = appName;
    }

    public static ConfigurableEnvironment getConfigurableEnvironment() {
        return configurableEnvironment;
    }

    public static void setConfigurableEnvironment(ConfigurableEnvironment configurableEnvironment) {
        HyggeSpringContext.configurableEnvironment = configurableEnvironment;
    }

    public static String toJsonVale() {
        UnitConvertHelper unitConvertHelper = UtilCreator.INSTANCE.getDefaultInstance(UnitConvertHelper.class);
        Runtime runtime = Runtime.getRuntime();

        return String.format("{\"spring-boot-version\":\"%s\",\"environmentModeEnum\":\"%s\",\"appName\":\"%s\",\"language\":\"%s\",\"timeZone\":\"%s\",\"totalMemory\":\"%s\",\"usedMemory\":\"%s\"}",
                SpringBootVersion.getVersion(),
                deploymentEnvironment,
                appName,
                Locale.getDefault().getLanguage(),
                TimeZone.getDefault().getID(),
                unitConvertHelper.storageSmartFormatAsString(runtime.totalMemory()),
                unitConvertHelper.storageSmartFormatAsString(runtime.totalMemory() - runtime.freeMemory()));
    }

    @Override
    public String toString() {
        return toJsonVale();
    }
}
