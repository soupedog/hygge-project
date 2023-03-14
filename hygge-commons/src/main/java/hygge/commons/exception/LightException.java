package hygge.commons.exception;


import hygge.commons.constant.enums.GlobalHyggeCode;
import hygge.commons.constant.enums.definition.HyggeCode;
import hygge.commons.exception.core.HyggeException;

/**
 * 轻量型异常
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public class LightException extends HyggeException {
    public LightException(String message) {
        super(message, GlobalHyggeCode.CLIENT_END_EXCEPTION);
    }

    public LightException(String message, Throwable cause) {
        super(message, GlobalHyggeCode.CLIENT_END_EXCEPTION, cause);
    }

    public LightException(String message, HyggeCode hyggeCode) {
        super(message, hyggeCode);
    }

    public LightException(String message, HyggeCode hyggeCode, Throwable cause) {
        super(message, hyggeCode, cause);
    }

    public LightException(String message, HyggeCode hyggeCode, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, hyggeCode, cause, enableSuppression, writableStackTrace);
    }
}
