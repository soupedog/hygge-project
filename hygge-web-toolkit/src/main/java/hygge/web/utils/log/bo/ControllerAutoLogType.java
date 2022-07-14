package hygge.web.utils.log.bo;

/**
 * Controller 层自动日志的类型
 *
 * @author Xavier
 * @date 2022/7/14
 * @since 1.0
 */
public enum ControllerAutoLogType {
    /**
     * 不需要自动日志
     */
    NONE,
    GET,
    POST,
    PATCH,
    PUT,
    DELETE
}
