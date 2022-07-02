package hygge.logging.configuration;

import hygge.commons.spring.HyggeSpringContext;
import hygge.commons.spring.enums.DeploymentEnvironmentEnum;
import hygge.logging.enums.ConverterModeEnum;
import hygge.logging.enums.OutputModeEnum;
import hygge.utils.UtilsCreator;
import hygge.utils.definitions.ParameterHelper;
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
    protected static final ParameterHelper parameterHelper = UtilsCreator.INSTANCE.getDefaultInstance(ParameterHelper.class);
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
     * 日志模板是否启用彩色
     */
    protected Boolean enableColorful;
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
        this.enableColorful = parameterHelper.booleanFormat(ConfigKey.ENABLE_COLORFUL.getKey(), configurableEnvironment.getProperty(ConfigKey.ENABLE_COLORFUL.getKey()));
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
        this.enableJsonType = parameterHelper.booleanFormatOfNullable("simulateJsonPatter.enable", this.enableJsonType, false);

        // DEV 环境外默认使用
        if (HyggeSpringContext.getDeploymentEnvironment().getPrivilegeLevel() > DeploymentEnvironmentEnum.DEV.getPrivilegeLevel()) {
            this.outputMode = parameterHelper.parseObjectOfNullable(ConfigKey.OUTPUT_MODE.getKey(), this.outputMode, OutputModeEnum.FILE);
            this.converterMode = parameterHelper.parseObjectOfNullable(ConfigKey.OUTPUT_MODE.getKey(), this.converterMode, ConverterModeEnum.JSON_FRIENDLY);
            // 默认关闭彩色
            this.enableColorful = parameterHelper.booleanFormatOfNullable(ConfigKey.ENABLE_COLORFUL.getKey(), this.enableColorful, false);
        } else {
            this.outputMode = parameterHelper.parseObjectOfNullable(ConfigKey.OUTPUT_MODE.getKey(), this.outputMode, OutputModeEnum.CONSOLE);
            this.converterMode = parameterHelper.parseObjectOfNullable(ConfigKey.OUTPUT_MODE.getKey(), this.converterMode, ConverterModeEnum.DEFAULT);
            // DEV 默认开启彩色
            this.enableColorful = parameterHelper.booleanFormatOfNullable(ConfigKey.ENABLE_COLORFUL.getKey(), this.enableColorful, true);
        }
    }

    public enum ConfigKey {
        /**
         * 项目名称
         */
        PROJECT_NAME("hygge.logging.projectName"),
        /**
         * 应用名称
         */
        APP_NAME("hygge.logging.appName"),
        /**
         * 应用版本号
         */
        VERSION("hygge.logging.version"),
        /**
         * 是否启动
         */
        ENABLE("hygge.logging.enable"),
        /**
         * rootLogger 覆写是否开启
         */
        ROOT_OVERRIDE("hygge.logging.root.override.enable"),
        /**
         * hygge 包路径范围
         */
        HYGGE_SCOPE_PATHS("hygge.logging.scope.paths"),
        /**
         * 日志模板是否启用彩色
         */
        ENABLE_COLORFUL("hygge.logging.pattern.colorful.enable"),
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
         * 日志输出模式
         */
        OUTPUT_MODE("hygge.logging.output.mode"),
        /**
         * 日志内容编码模式
         */
        CONVERTER_MODE("hygge.logging.pattern.converter.mode"),
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
         * 日志文件存储路径
         */
        LOGBACK_FILE_NAME_PATTERN("hygge.logging.logback.pattern.file.namePattern"),
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

    public static ParameterHelper getParameterHelper() {
        return parameterHelper;
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

    public boolean isEnableColorful() {
        return enableColorful;
    }

    public void setEnableColorful(boolean enableColorful) {
        this.enableColorful = enableColorful;
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
