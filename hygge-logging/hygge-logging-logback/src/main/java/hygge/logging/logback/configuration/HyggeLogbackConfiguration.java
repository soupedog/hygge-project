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

package hygge.logging.logback.configuration;

import hygge.logging.config.configuration.base.HyggeLogConfiguration;
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
     * 自定义日志工具 root 文件输出路径<br/>
     * {@link HyggeLogConfiguration#outputMode} 为控制台时，该参数无意义
     */
    protected String fileNamePatternRoot;
    /**
     * 自定义日志工具文件 hygge 输出路径<br/>
     * {@link HyggeLogConfiguration#outputMode} 为控制台时，该参数无意义
     */
    protected String fileNamePatternHygge;
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
        this.fileNamePatternRoot = configurableEnvironment.getProperty(ConfigKey.LOGBACK_FILE_NAME_PATTERN_ROOT.getKey());
        this.fileNamePatternHygge = configurableEnvironment.getProperty(ConfigKey.LOGBACK_FILE_NAME_PATTERN_HYGGE.getKey());
        this.fileMaxSize = configurableEnvironment.getProperty(ConfigKey.LOGBACK_FILE_MAX_SIZE.getKey());
        this.fileMaxHistory = parameterHelper.integerFormatOfNullable("fileMaxHistory", configurableEnvironment.getProperty(ConfigKey.LOGBACK_FILE_MAX_HISTORY.getKey()), 0);
    }

    @Override
    protected void exclusiveCheckAndInit() {
        // 包含文件类型输出时，校验文件输出相关参数
        if (OutputModeEnum.FILE.equals(outputMode) || OutputModeEnum.CONSOLE_AND_FILE.equals(outputMode)) {
            this.fileNamePatternRoot = parameterHelper.stringNotEmpty("fileNamePatternRoot", (Object) fileNamePatternRoot);
            this.fileNamePatternHygge = parameterHelper.stringNotEmpty("fileNamePatternHygge", (Object) fileNamePatternHygge);
            this.fileMaxSize = parameterHelper.stringOfNullable(fileMaxSize, "2MB");
        }
    }

    public String getFileNamePatternRoot() {
        return fileNamePatternRoot;
    }

    public void setFileNamePatternRoot(String fileNamePatternRoot) {
        this.fileNamePatternRoot = fileNamePatternRoot;
    }

    public String getFileNamePatternHygge() {
        return fileNamePatternHygge;
    }

    public void setFileNamePatternHygge(String fileNamePatternHygge) {
        this.fileNamePatternHygge = fileNamePatternHygge;
    }

    public String getFileMaxSize() {
        return fileMaxSize;
    }

    public void setFileMaxSize(String fileMaxSize) {
        this.fileMaxSize = fileMaxSize;
    }

    public int getFileMaxHistory() {
        return fileMaxHistory;
    }

    public void setFileMaxHistory(int fileMaxHistory) {
        this.fileMaxHistory = fileMaxHistory;
    }
}
