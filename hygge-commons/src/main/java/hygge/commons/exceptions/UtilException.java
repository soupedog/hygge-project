package hygge.commons.exceptions;


import hygge.commons.constants.enums.GlobalHyggeCode;
import hygge.commons.constants.enums.definitions.HyggeCode;
import hygge.commons.exceptions.core.HyggeException;

/**
 * 工具类异常
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public class UtilException extends HyggeException {
    public UtilException(String message) {
        super(message, GlobalHyggeCode.UTIL_EXCEPTION);
    }

    public UtilException(String message, Throwable cause) {
        super(message, GlobalHyggeCode.UTIL_EXCEPTION, cause);
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
