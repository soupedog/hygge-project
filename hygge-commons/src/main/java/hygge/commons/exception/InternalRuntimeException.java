package hygge.commons.exception;


import hygge.commons.constant.enums.GlobalHyggeCode;
import hygge.commons.constant.enums.definition.HyggeCode;
import hygge.commons.exception.main.HyggeRuntimeException;

/**
 * 服务端内部运行时异常
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public class InternalRuntimeException extends HyggeRuntimeException {
    public InternalRuntimeException(String message) {
        super(message, GlobalHyggeCode.SERVER_END_EXCEPTION);
    }

    public InternalRuntimeException(String message, Throwable cause) {
        super(message, GlobalHyggeCode.SERVER_END_EXCEPTION, cause);
    }

    public InternalRuntimeException(String message, HyggeCode hyggeCode) {
        super(message, hyggeCode);
    }

    public InternalRuntimeException(String message, HyggeCode hyggeCode, Throwable cause) {
        super(message, hyggeCode, cause);
    }

    public InternalRuntimeException(String message, HyggeCode hyggeCode, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, hyggeCode, cause, enableSuppression, writableStackTrace);
    }
}
