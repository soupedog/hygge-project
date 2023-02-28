package hygge.logging.enums;

/**
 * 日志输出的模式
 *
 * @author Xavier
 * @date 2022/6/28
 * @since 1.0
 */
public enum OutputModeEnum {
    /**
     * 控制台模式
     */
    CONSOLE("CONSOLE"),
    /**
     * 文件模式
     */
    FILE("FILE"),
    /**
     * 控制台、文件均进行日志输出
     */
    CONSOLE_AND_FILE("CONSOLE_AND_FILE"),
    ;

    OutputModeEnum(String description) {
        this.description = description;
    }

    /**
     * 描述
     */
    private final String description;

    public String getDescription() {
        return description;
    }
}
