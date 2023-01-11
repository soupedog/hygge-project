package hygge.commons.exceptions;


import hygge.commons.exceptions.code.GlobalHyggeCode;
import hygge.commons.exceptions.code.HyggeCode;
import hygge.commons.exceptions.core.HyggeRuntimeException;

/**
 * 服务端外部运行时异常
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public class ExternalRuntimeException extends HyggeRuntimeException {
    public ExternalRuntimeException(String message) {
        super(message, GlobalHyggeCode.SERVER_END_EXCEPTION);
    }

    public ExternalRuntimeException(String message, Throwable cause) {
        super(message, GlobalHyggeCode.SERVER_END_EXCEPTION, cause);
    }

    public ExternalRuntimeException(String message, HyggeCode hyggeCode) {
        super(message, hyggeCode);
    }

    public ExternalRuntimeException(String message, HyggeCode hyggeCode, Throwable cause) {
        super(message, hyggeCode, cause);
    }

    public ExternalRuntimeException(String message, HyggeCode hyggeCode, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, hyggeCode, cause, enableSuppression, writableStackTrace);
    }
}
