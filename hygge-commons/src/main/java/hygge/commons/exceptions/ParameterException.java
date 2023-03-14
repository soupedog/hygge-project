package hygge.commons.exceptions;


import hygge.commons.constants.enums.GlobalHyggeCode;
import hygge.commons.constants.enums.definitions.HyggeCode;
import hygge.commons.exceptions.core.HyggeException;

/**
 * 入参不符合预期异常
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public class ParameterException extends HyggeException {
    public ParameterException(String message) {
        super(message, GlobalHyggeCode.UNEXPECTED_PARAMETER);
    }

    public ParameterException(String message, Throwable cause) {
        super(message, GlobalHyggeCode.UNEXPECTED_PARAMETER, cause);
    }

    public ParameterException(String message, HyggeCode hyggeCode) {
        super(message, hyggeCode);
    }

    public ParameterException(String message, HyggeCode hyggeCode, Throwable cause) {
        super(message, hyggeCode, cause);
    }

    public ParameterException(String message, HyggeCode hyggeCode, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, hyggeCode, cause, enableSuppression, writableStackTrace);
    }
}
