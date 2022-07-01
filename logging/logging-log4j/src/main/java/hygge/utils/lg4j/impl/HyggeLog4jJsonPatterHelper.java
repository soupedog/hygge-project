package hygge.utils.lg4j.impl;

import hygge.logging.enums.ConverterModeEnum;
import hygge.utils.definitions.HyggeLogJsonPatterHelper;

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
        return enableColorful ? "%pid" : "%clr{%pid}{magenta}";
    }

    @Override
    public String getThread(boolean enableColorful, ConverterModeEnum converterMode) {
        return enableColorful ? "%t" : "%15t";
    }

    @Override
    public String getClassPath(boolean enableColorful, ConverterModeEnum converterMode) {
        return enableColorful ? "%c{1.}" : "%clr{%c{1.}}{blue}";
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
