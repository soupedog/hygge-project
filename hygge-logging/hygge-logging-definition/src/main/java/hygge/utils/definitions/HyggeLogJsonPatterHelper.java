package hygge.utils.definitions;


import hygge.logging.enums.ConverterModeEnum;
import hygge.utils.UtilsCreator;

import java.util.LinkedHashMap;

/**
 * json 日志模板生成器
 *
 * @author Xavier
 * @date 2022/7/1
 * @since 1.0
 */
public abstract class HyggeLogJsonPatterHelper {
    protected String type;
    protected String projectName;
    protected String appName;
    protected String version;

    protected static final ParameterHelper parameterHelper = UtilsCreator.INSTANCE.getDefaultInstance(ParameterHelper.class);

    protected HyggeLogJsonPatterHelper(String type, String projectName, String appName, String version) {
        this.type = parameterHelper.stringNotEmpty("type", (Object) type);
        this.projectName = parameterHelper.stringNotEmpty("projectName", (Object) projectName);
        this.appName = parameterHelper.stringNotEmpty("appName", (Object) appName);
        this.version = parameterHelper.stringNotEmpty("version", (Object) version);
    }

    public String create(boolean enableColorful, ConverterModeEnum converterMode) {
        LinkedHashMap<String, String> temp = new LinkedHashMap<>();
        temp.put("level", getLevel(enableColorful, converterMode));
        temp.put("type", type);
        temp.put("projectName", projectName);
        temp.put("appName", appName);
        temp.put("version", version);
        temp.put("host", getHost(enableColorful, converterMode));
        temp.put("pid", getPid(enableColorful, converterMode));
        temp.put("thread", getThread(enableColorful, converterMode));
        temp.put("classPath", getClassPath(enableColorful, converterMode));
        temp.put("ts", getTs(enableColorful, converterMode));
        temp.put("msg", getMsg(enableColorful, converterMode));
        temp.put("thrown", getThrown(enableColorful, converterMode));
        JsonHelper<?> jsonHelper = UtilsCreator.INSTANCE.getDefaultJsonHelperInstance(false);
        return jsonHelper.formatAsString(temp);
    }

    public abstract String getLevel(boolean enableColorful, ConverterModeEnum converterMode);

    public String getType() {
        return type;
    }

    public abstract String getType(boolean enableColorful, ConverterModeEnum converterMode);

    public void setType(String type) {
        this.type = type;
    }

    public String getProjectName() {
        return projectName;
    }

    public abstract String getProjectName(boolean enableColorful, ConverterModeEnum converterMode);

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getAppName() {
        return appName;
    }

    public abstract String getAppName(boolean enableColorful, ConverterModeEnum converterMode);

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getVersion() {
        return version;
    }

    public abstract String getVersion(boolean enableColorful, ConverterModeEnum converterMode);

    public void setVersion(String version) {
        this.version = version;
    }

    public abstract String getHost(boolean enableColorful, ConverterModeEnum converterMode);

    public abstract String getPid(boolean enableColorful, ConverterModeEnum converterMode);

    public abstract String getThread(boolean enableColorful, ConverterModeEnum converterMode);

    public abstract String getClassPath(boolean enableColorful, ConverterModeEnum converterMode);

    public abstract String getTs(boolean enableColorful, ConverterModeEnum converterMode);

    public abstract String getMsg(boolean enableColorful, ConverterModeEnum converterMode);

    public abstract String getThrown(boolean enableColorful, ConverterModeEnum converterMode);
}
