package hygge.utils.logback.impl;

import hygge.logging.enums.ConverterModeEnum;
import hygge.utils.definitions.HyggeLogJsonPatterHelper;

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
        return enableColorful ? "%clr{%5p}" : "%5p";
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
        return enableColorful ? "%pid" : "%clr{%pid}{magenta}";
    }

    @Override
    public String getThread(boolean enableColorful, ConverterModeEnum converterMode) {
        return "%15.15t";
    }

    @Override
    public String getClassPath(boolean enableColorful, ConverterModeEnum converterMode) {
        return enableColorful ? "%-40.40logger{39}" : "%clr(%-40.40logger{39}){cyan}";
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
        return "%wEx";
    }
}
