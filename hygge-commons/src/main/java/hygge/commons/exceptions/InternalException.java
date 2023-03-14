package hygge.commons.exceptions;


import hygge.commons.exceptions.constants.enums.GlobalHyggeCode;
import hygge.commons.exceptions.definitions.HyggeCode;
import hygge.commons.exceptions.core.HyggeException;

/**
 * 服务端内部异常
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public class InternalException extends HyggeException {
    public InternalException(String message) {
        super(message, GlobalHyggeCode.SERVER_END_EXCEPTION);
    }

    public InternalException(String message, Throwable cause) {
        super(message, GlobalHyggeCode.SERVER_END_EXCEPTION, cause);
    }

    public InternalException(String message, HyggeCode hyggeCode) {
        super(message, hyggeCode);
    }

    public InternalException(String message, HyggeCode hyggeCode, Throwable cause) {
        super(message, hyggeCode, cause);
    }

    public InternalException(String message, HyggeCode hyggeCode, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, hyggeCode, cause, enableSuppression, writableStackTrace);
    }
}
