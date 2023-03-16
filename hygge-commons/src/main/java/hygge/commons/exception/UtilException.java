package hygge.commons.exception;


import hygge.commons.constant.enums.GlobalHyggeCodeEnum;
import hygge.commons.template.definition.HyggeCode;
import hygge.commons.exception.main.HyggeException;

/**
 * 工具类异常
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public class UtilException extends HyggeException {
    public UtilException(String message) {
        super(message, GlobalHyggeCodeEnum.UTIL_EXCEPTION);
    }

    public UtilException(String message, Throwable cause) {
        super(message, GlobalHyggeCodeEnum.UTIL_EXCEPTION, cause);
    }

    public UtilException(String message, HyggeCode hyggeCode) {
        super(message, hyggeCode);
    }

    public UtilException(String message, HyggeCode hyggeCode, Throwable cause) {
        super(message, hyggeCode, cause);
    }

    public UtilException(String message, HyggeCode hyggeCode, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, hyggeCode, cause, enableSuppression, writableStackTrace);
    }
}
