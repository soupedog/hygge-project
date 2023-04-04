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

package hygge.logging.config.configuration.base;

import hygge.commons.spring.HyggeSpringContext;
import hygge.commons.spring.enums.DeploymentEnvironmentEnum;
import hygge.logging.enums.ConverterModeEnum;
import hygge.logging.enums.OutputModeEnum;
import hygge.util.UtilCreator;
import hygge.util.definition.ParameterHelper;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Collections;
import java.util.Map;

/**
 * Hygge 日志配置项
 *
 * @author Xavier
 * @date 2022/6/28
 * @since 1.0
 */
public abstract class HyggeLogConfiguration {
    protected static final ParameterHelper parameterHelper = UtilCreator.INSTANCE.getDefaultInstance(ParameterHelper.class);
    /**
     * hygge 包路径范围配置项映射获取工具
     */
    protected static final ConfigurationPropertyName HYGGE_SCOPE_PATH_PREFIX = ConfigurationPropertyName.of(ConfigKey.HYGGE_SCOPE_PATHS.getKey());
    protected static final Bindable<Map<String, Boolean>> HYGGE_SCOPE_PATH_MAPPING = Bindable.mapOf(String.class, Boolean.class);

    /**
     * 项目名称<br/>
     * 默认为 "hygge"
     */
    protected String projectName;
    /**
     * 应用名称<br/>
     * 默认为 {@link HyggeSpringContext#getAppName()}
     */
    protected String appName;
    /**
     * 应用版本号<br/>
     * 默认为 "1.0.0"
     */
    protected String version;
    /**
     * HyggeLog 是否启动
     */
    protected Boolean enable;
    /**
     * hygge 包路径范围标记<br/>
     * key : path<br/>
     * value : 是否属于 hygge 范围内
     */
    protected Map<String, Boolean> hyggeScopePaths;
    /**
     * 是否覆写 Root Logger
     */
    protected Boolean enableRootOverride;
    /**
     * 每条日志整体作为 json 字符串<br/>
     * 显式指定 {@link HyggeLogConfiguration#rootPattern} / {@link HyggeLogConfiguration#hyggePattern} 时，该参数无意义
     */
    protected Boolean enableJsonType;
    /**
     * 控制台日志模板是否启用彩色
     */
    protected Boolean enableColorfulConsole;
    /**
     * 文件日志模板是否启用彩色
     */
    protected Boolean enableColorfulFile;
    /**
     * root pattern
     */
    protected String rootPattern;
    /**
     * hygge pattern
     */
    protected String hyggePattern;
    /**
     * 日志输出的模式
     */
    protected OutputModeEnum outputMode;
    /**
     * 日志内容转换模式
     */
    protected ConverterModeEnum converterMode;

    protected HyggeLogConfiguration() {
        ConfigurableEnvironment configurableEnvironment = HyggeSpringContext.getConfigurableEnvironment();

        sharedRead(configurableEnvironment);

        exclusiveRead(configurableEnvironment);

        sharedCheckAndInit();

        exclusiveCheckAndInit();
    }

    /**
     * 从环境变量中读取独占属性
     */
    protected abstract void exclusiveRead(ConfigurableEnvironment configurableEnvironment);

    /**
     * 独占属性的检查与默认值配置
     */
    protected abstract void exclusiveCheckAndInit();

    /**
     * 共享属性的读取
     */
    protected void sharedRead(ConfigurableEnvironment configurableEnvironment) {
        this.projectName = configurableEnvironment.getProperty(ConfigKey.PROJECT_NAME.getKey(), "hygge");
        this.appName = configurableEnvironment.getProperty(ConfigKey.APP_NAME.getKey(), HyggeSpringContext.getAppName());
        this.version = configurableEnvironment.getProperty(ConfigKey.VERSION.getKey());

        this.enable = parameterHelper.booleanFormat(ConfigKey.ENABLE.getKey(), configurableEnvironment.getProperty(ConfigKey.ENABLE.getKey()));

        Binder binder = Binder.get(configurableEnvironment);
        this.hyggeScopePaths = binder.bind(HYGGE_SCOPE_PATH_PREFIX, HYGGE_SCOPE_PATH_MAPPING).orElseGet(Collections::emptyMap);

        this.enableRootOverride = parameterHelper.booleanFormat(ConfigKey.ROOT_OVERRIDE.getKey(), configurableEnvironment.getProperty(ConfigKey.ROOT_OVERRIDE.getKey()));
        this.enableJsonType = parameterHelper.booleanFormat(ConfigKey.ENABLE_JSON_TYPE.getKey(), configurableEnvironment.getProperty(ConfigKey.ENABLE_JSON_TYPE.getKey()));
        this.enableColorfulConsole = parameterHelper.booleanFormat(ConfigKey.ENABLE_COLORFUL_CONSOLE.getKey(), configurableEnvironment.getProperty(ConfigKey.ENABLE_COLORFUL_CONSOLE.getKey()));
        this.rootPattern = configurableEnvironment.getProperty(ConfigKey.ROOT_PATTERN.getKey());
        this.hyggePattern = configurableEnvironment.getProperty(ConfigKey.HYGGE_PATTERN.getKey());

        if (parameterHelper.isNotEmpty(configurableEnvironment.getProperty(ConfigKey.OUTPUT_MODE.getKey()))) {
            this.outputMode = OutputModeEnum.valueOf(configurableEnvironment.getProperty(ConfigKey.OUTPUT_MODE.getKey()));
        }

        if (parameterHelper.isNotEmpty(configurableEnvironment.getProperty(ConfigKey.CONVERTER_MODE.getKey()))) {
            this.converterMode = ConverterModeEnum.valueOf(configurableEnvironment.getProperty(ConfigKey.CONVERTER_MODE.getKey()));
        }
    }

    /**
     * 共享属性的检查与默认值配置
     */
    protected void sharedCheckAndInit() {
        this.projectName = parameterHelper.stringOfNullable(this.projectName, "hygge");
        this.appName = HyggeSpringContext.getAppName();
        this.version = parameterHelper.stringOfNullable(this.version, "1.0.0");

        this.enable = parameterHelper.booleanFormatOfNullable(ConfigKey.ENABLE.getKey(), this.enable, true);

        this.enableRootOverride = parameterHelper.booleanFormatOfNullable(ConfigKey.ROOT_OVERRIDE.getKey(), this.enableRootOverride, true);
        this.enableJsonType = parameterHelper.booleanFormatOfNullable(ConfigKey.ENABLE_JSON_TYPE.getKey(), this.enableJsonType, false);

        // 非 DEV 环境默认值
        if (HyggeSpringContext.getDeploymentEnvironment().getPrivilegeLevel() > DeploymentEnvironmentEnum.DEV.getPrivilegeLevel()) {
            this.outputMode = parameterHelper.parseObjectOfNullable(ConfigKey.OUTPUT_MODE.getKey(), this.outputMode, OutputModeEnum.FILE);
            this.converterMode = parameterHelper.parseObjectOfNullable(ConfigKey.OUTPUT_MODE.getKey(), this.converterMode, ConverterModeEnum.JSON_FRIENDLY);
            // 默认关闭控制台彩色日志
            this.enableColorfulConsole = parameterHelper.booleanFormatOfNullable(ConfigKey.ENABLE_COLORFUL_CONSOLE.getKey(), this.enableColorfulConsole, false);
        } else {
            // DEV 环境默认值
            this.outputMode = parameterHelper.parseObjectOfNullable(ConfigKey.OUTPUT_MODE.getKey(), this.outputMode, OutputModeEnum.CONSOLE);
            this.converterMode = parameterHelper.parseObjectOfNullable(ConfigKey.OUTPUT_MODE.getKey(), this.converterMode, ConverterModeEnum.DEFAULT);
            // DEV 默认开启控制台彩色日志
            this.enableColorfulConsole = parameterHelper.booleanFormatOfNullable(ConfigKey.ENABLE_COLORFUL_CONSOLE.getKey(), this.enableColorfulConsole, true);
        }

        // 全环境默认关闭文件彩色日志
        this.enableColorfulFile = parameterHelper.booleanFormatOfNullable(ConfigKey.ENABLE_COLORFUL_FILE.getKey(), this.enableColorfulFile, false);
    }

    public enum ConfigKey {
        /**
         * 是否启动
         */
        ENABLE("hygge.logging.enable"),
        /**
         * 项目名称
         */
        PROJECT_NAME("hygge.logging.projectName"),
        /**
         * 应用名称(默认会使用 spring.application.name)
         */
        APP_NAME("hygge.logging.appName"),
        /**
         * 应用版本号
         */
        VERSION("hygge.logging.version"),
        /**
         * 是否覆写 {@link org.slf4j.Logger.ROOT_LOGGER_NAME} 对应的 Logger
         */
        ROOT_OVERRIDE("hygge.logging.rootOverride.enable"),
        /**
         * hygge 包路径范围
         */
        HYGGE_SCOPE_PATHS("hygge.logging.scope.paths"),
        /**
         * 日志模板是否启用彩色
         */
        ENABLE_COLORFUL_CONSOLE("hygge.logging.pattern.colorful.console.enable"),
        /**
         * 日志模板是否启用彩色
         */
        ENABLE_COLORFUL_FILE("hygge.logging.pattern.colorful.file.enable"),
        /**
         * Root Pattern
         */
        ROOT_PATTERN("hygge.logging.pattern.root"),
        /**
         * Hygge Pattern
         */
        HYGGE_PATTERN("hygge.logging.pattern.hygge"),
        /**
         * 每条日志整体作为 json 字符串<br/>
         * 显式指定 {@link HyggeLogConfiguration#rootPattern} / {@link HyggeLogConfiguration#hyggePattern} 时，该参数无意义
         */
        ENABLE_JSON_TYPE("hygge.logging.pattern.jsonType.enable"),
        /**
         * 日志内容编码模式
         */
        CONVERTER_MODE("hygge.logging.pattern.converter.mode"),
        /**
         * 日志输出模式
         */
        OUTPUT_MODE("hygge.logging.output.mode"),
        /**
         * log4j 专有配置项<br/>
         * 日志文件存储路径
         */
        LOG4J_FILE_PATH("hygge.logging.log4j.pattern.file.path"),
        /**
         * log4j 专有配置项<br/>
         * 单个日志文件最大文件大小
         */
        LOG4J_FILE_MAX_SIZE("hygge.logging.log4j.pattern.file.maxSize"),
        /**
         * log4j 专有配置项<br/>
         * 日志文件截断触发时间 cron 表达式
         */
        LOG4J_CRON_TRIGGER("hygge.logging.log4j.pattern.file.cron"),
        /**
         * logback 专有配置项<br/>
         * root 日志文件存储路径
         */
        LOGBACK_FILE_NAME_PATTERN_ROOT("hygge.logging.logback.pattern.fileNamePattern.root"),
        /**
         * logback 专有配置项<br/>
         * hygge 日志文件存储路径
         */
        LOGBACK_FILE_NAME_PATTERN_HYGGE("hygge.logging.logback.pattern.fileNamePattern.hygge"),
        /**
         * logback 专有配置项<br/>
         * 单个日志文件最大文件大小
         */
        LOGBACK_FILE_MAX_SIZE("hygge.logging.logback.pattern.file.maxSize"),
        /**
         * logback 专有配置项<br/>
         * 日志文件保留的最长时间
         */
        LOGBACK_FILE_MAX_HISTORY("hygge.logging.logback.pattern.file.maxHistory"),
        ;

        ConfigKey(String key) {
            this.key = key;
        }

        /**
         * 配置项 key
         */
        private final String key;

        public String getKey() {
            return key;
        }
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Map<String, Boolean> getHyggeScopePaths() {
        return hyggeScopePaths;
    }

    public void setHyggeScopePaths(Map<String, Boolean> hyggeScopePaths) {
        this.hyggeScopePaths = hyggeScopePaths;
    }

    public boolean isEnableRootOverride() {
        return enableRootOverride;
    }

    public void setEnableRootOverride(boolean enableRootOverride) {
        this.enableRootOverride = enableRootOverride;
    }

    public boolean isEnableJsonType() {
        return enableJsonType;
    }

    public void setEnableJsonType(boolean enableJsonType) {
        this.enableJsonType = enableJsonType;
    }

    public boolean getEnableColorfulConsole() {
        return enableColorfulConsole;
    }

    public void setEnableColorfulConsole(boolean enableColorfulConsole) {
        this.enableColorfulConsole = enableColorfulConsole;
    }

    public Boolean getEnableColorfulFile() {
        return enableColorfulFile;
    }

    public void setEnableColorfulFile(boolean enableColorfulFile) {
        this.enableColorfulFile = enableColorfulFile;
    }

    public String getRootPattern() {
        return rootPattern;
    }

    public void setRootPattern(String rootPattern) {
        this.rootPattern = rootPattern;
    }

    public String getHyggePattern() {
        return hyggePattern;
    }

    public void setHyggePattern(String hyggePattern) {
        this.hyggePattern = hyggePattern;
    }

    public OutputModeEnum getOutputMode() {
        return outputMode;
    }

    public void setOutputMode(OutputModeEnum outputMode) {
        this.outputMode = outputMode;
    }

    public ConverterModeEnum getConverterMode() {
        return converterMode;
    }

    public void setConverterMode(ConverterModeEnum converterMode) {
        this.converterMode = converterMode;
    }
}
