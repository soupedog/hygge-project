package hygge.web.util.log.bo;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import hygge.utils.json.jackson.serializer.HyggeLogInfoSerializer;
import hygge.web.util.log.enums.ControllerLogType;

/**
 * Controller 层自动日志对象
 *
 * @author Xavier
 * @date 2022/7/14
 * @since 1.0
 */
public class ControllerLogInfo {
    private ControllerLogType type;
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

    public ControllerLogType getType() {
        return type;
    }

    public void setType(ControllerLogType type) {
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
