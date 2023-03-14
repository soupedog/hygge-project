package hygge.logging.log4j.configuration;

import hygge.logging.configuration.base.HyggeLogConfiguration;
import hygge.logging.enums.OutputModeEnum;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Hygge log4j 日志配置项
 *
 * @author Xavier
 * @date 2022/7/1
 * @since 1.0
 */
public class HyggeLg4jConfiguration extends HyggeLogConfiguration {
    /**
     * 自定义日志工具文件输出路径<br/>
     * {@link HyggeLogConfiguration#outputMode} 为控制台时，该参数无意义
     */
    protected String filePath;
    /**
     * 单个日志文件最大文件大小，样例：10MB/10KB<br/>
     * {@link HyggeLogConfiguration#outputMode} 为控制台时，该参数无意义
     */
    protected String fileMaxSize;
    /**
     * 日志文件截断触发时间 cron 表达式<br/>
     * {@link HyggeLogConfiguration#outputMode} 为控制台时，该参数无意义
     */
    protected String cronTrigger;

    @Override
    protected void exclusiveRead(ConfigurableEnvironment configurableEnvironment) {
        this.filePath = configurableEnvironment.getProperty(ConfigKey.LOG4J_FILE_PATH.getKey());
        this.fileMaxSize = configurableEnvironment.getProperty(ConfigKey.LOG4J_FILE_MAX_SIZE.getKey());
        this.cronTrigger = configurableEnvironment.getProperty(ConfigKey.LOG4J_CRON_TRIGGER.getKey());
    }

    @Override
    protected void exclusiveCheckAndInit() {
        // 包含文件类型输出时，校验文件输出相关参数
        if (OutputModeEnum.FILE.equals(outputMode)
                || OutputModeEnum.CONSOLE_AND_FILE.equals(outputMode)) {
            this.filePath = parameterHelper.stringNotEmpty("filePath", (Object) filePath);
            this.fileMaxSize = parameterHelper.stringOfNullable(fileMaxSize, "2MB");
            // 默认 0 点截断日志文件
            this.cronTrigger = parameterHelper.stringOfNullable(cronTrigger, "0 0 0 * * ?");
        }
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileMaxSize() {
        return fileMaxSize;
    }

    public void setFileMaxSize(String fileMaxSize) {
        this.fileMaxSize = fileMaxSize;
    }

    public String getCronTrigger() {
        return cronTrigger;
    }

    public void setCronTrigger(String cronTrigger) {
        this.cronTrigger = cronTrigger;
    }
}
