package hygge.web.utils.log.bo;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import hygge.utils.json.jackson.serializer.HyggeLogInfoSerializer;

/**
 * Controller 层自动日志对象
 *
 * @author Xavier
 * @date 2022/7/14
 * @since 1.0
 */
public class ControllerLogInfo {
    private ControllerAutoLogType type;
    private String path;
    @JsonSerialize(using = HyggeLogInfoSerializer.class)
    private Object input;
    @JsonSerialize(using = HyggeLogInfoSerializer.class)
    private Object output;
    private String errorMessage;
    /**
     * 处理耗时(ms)
     */
    private Long cost;

    public ControllerAutoLogType getType() {
        return type;
    }

    public void setType(ControllerAutoLogType type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Object getInput() {
        return input;
    }

    public void setInput(Object input) {
        this.input = input;
    }

    public Object getOutput() {
        return output;
    }

    public void setOutput(Object output) {
        this.output = output;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }
}
