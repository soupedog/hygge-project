package hygge.commons.exceptions;


import hygge.commons.exceptions.code.GlobalHyggeCode;
import hygge.commons.exceptions.code.HyggeCode;
import hygge.commons.exceptions.core.HyggeRuntimeException;

/**
 * 工具类运行时异常
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public class UtilRuntimeException extends HyggeRuntimeException {
    public UtilRuntimeException(String message) {
        super(message, GlobalHyggeCode.UTIL_EXCEPTION);
    }

    public UtilRuntimeException(String message, Throwable cause) {
        super(message, GlobalHyggeCode.UTIL_EXCEPTION, cause);
    }

    public UtilRuntimeException(String message, HyggeCode hyggeCode) {
        super(message, hyggeCode);
    }

    public UtilRuntimeException(String message, HyggeCode hyggeCode, Throwable cause) {
        super(message, hyggeCode, cause);
    }

    public UtilRuntimeException(String message, HyggeCode hyggeCode, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, hyggeCode, cause, enableSuppression, writableStackTrace);
    }
}
