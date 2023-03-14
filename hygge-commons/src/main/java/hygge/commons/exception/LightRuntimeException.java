package hygge.commons.exception;


import hygge.commons.constant.enums.GlobalHyggeCode;
import hygge.commons.constant.enums.definition.HyggeCode;
import hygge.commons.exception.core.HyggeRuntimeException;

/**
 * 轻量型运行时异常
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public class LightRuntimeException extends HyggeRuntimeException {
    public LightRuntimeException(String message) {
        super(message, GlobalHyggeCode.CLIENT_END_EXCEPTION);
    }

    public LightRuntimeException(String message, Throwable cause) {
        super(message, GlobalHyggeCode.CLIENT_END_EXCEPTION, cause);
    }

    public LightRuntimeException(String message, HyggeCode hyggeCode) {
        super(message, hyggeCode);
    }

    public LightRuntimeException(String message, HyggeCode hyggeCode, Throwable cause) {
        super(message, hyggeCode, cause);
    }

    public LightRuntimeException(String message, HyggeCode hyggeCode, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, hyggeCode, cause, enableSuppression, writableStackTrace);
    }
}
