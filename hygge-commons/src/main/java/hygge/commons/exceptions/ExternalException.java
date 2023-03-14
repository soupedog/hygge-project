package hygge.commons.exceptions;


import hygge.commons.constants.enums.GlobalHyggeCode;
import hygge.commons.constants.enums.definitions.HyggeCode;
import hygge.commons.exceptions.core.HyggeException;

/**
 * 服务端外部异常
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public class ExternalException extends HyggeException {
    public ExternalException(String message) {
        super(message, GlobalHyggeCode.EXTERNAL_SYSTEM_EXCEPTION);
    }

    public ExternalException(String message, Throwable cause) {
        super(message, GlobalHyggeCode.EXTERNAL_SYSTEM_EXCEPTION, cause);
    }

    public ExternalException(String message, HyggeCode hyggeCode) {
        super(message, hyggeCode);
    }

    public ExternalException(String message, HyggeCode hyggeCode, Throwable cause) {
        super(message, hyggeCode, cause);
    }

    public ExternalException(String message, HyggeCode hyggeCode, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, hyggeCode, cause, enableSuppression, writableStackTrace);
    }
}
