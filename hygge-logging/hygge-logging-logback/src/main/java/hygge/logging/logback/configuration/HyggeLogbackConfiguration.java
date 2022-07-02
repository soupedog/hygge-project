package hygge.logging.logback.configuration;

import hygge.logging.configuration.HyggeLogConfiguration;
import hygge.logging.enums.OutputModeEnum;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Hygge logback 日志配置项
 *
 * @author Xavier
 * @date 2022/7/1
 * @since 1.0
 */
public class HyggeLogbackConfiguration extends HyggeLogConfiguration {
    /**
     * 自定义日志工具文件输出路径<br/>
     * {@link HyggeLogConfiguration#outputMode} 为控制台时，该参数无意义
     */
    protected String fileNamePattern;
    /**
     * 单个日志文件最大文件大小，样例：10MB/10KB<br/>
     * {@link HyggeLogConfiguration#outputMode} 为控制台时，该参数无意义
     */
    protected String fileMaxSize;
    /**
     * 文件保留期限<br/>
     * 默认 0 永久保留
     */
    protected int fileMaxHistory;

    @Override
    protected void exclusiveRead(ConfigurableEnvironment configurableEnvironment) {
        this.fileNamePattern = configurableEnvironment.getProperty(ConfigKey.LOGBACK_FILE_NAME_PATTERN.getKey());
        this.fileMaxSize = configurableEnvironment.getProperty(ConfigKey.LOGBACK_FILE_MAX_SIZE.getKey());
        this.fileMaxHistory = parameterHelper.integerFormatOfNullable("fileMaxHistory", configurableEnvironment.getProperty(ConfigKey.LOGBACK_FILE_MAX_HISTORY.getKey()), 0);
    }

    @Override
    protected void exclusiveCheckAndInit() {
        // 如果是文件输出
        if (OutputModeEnum.FILE.equals(outputMode)) {
            this.fileNamePattern = parameterHelper.stringNotEmpty("fileNamePattern", (Object) fileNamePattern);
            this.fileMaxSize = parameterHelper.stringOfNullable(fileMaxSize, "2MB");
        }
    }

    public String getFileNamePattern() {
        return fileNamePattern;
    }

    public void setFileNamePattern(String fileNamePattern) {
        this.fileNamePattern = fileNamePattern;
    }

    public String getFileMaxSize() {
        return fileMaxSize;
    }

    public void setFileMaxSize(String fileMaxSize) {
        this.fileMaxSize = fileMaxSize;
    }
}
