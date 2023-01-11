package hygge.commons.exceptions;


import hygge.commons.exceptions.code.GlobalHyggeCode;
import hygge.commons.exceptions.code.HyggeCode;
import hygge.commons.exceptions.core.HyggeRuntimeException;

/**
 * 入参不符合预期运行时异常
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public class ParameterRuntimeException extends HyggeRuntimeException {
    public ParameterRuntimeException(String message) {
        super(message, GlobalHyggeCode.UNEXPECTED_PARAMETER);
    }

    public ParameterRuntimeException(String message, Throwable cause) {
        super(message, GlobalHyggeCode.UNEXPECTED_PARAMETER, cause);
    }

    public ParameterRuntimeException(String message, HyggeCode hyggeCode) {
        super(message, hyggeCode);
    }

    public ParameterRuntimeException(String message, HyggeCode hyggeCode, Throwable cause) {
        super(message, hyggeCode, cause);
    }

    public ParameterRuntimeException(String message, HyggeCode hyggeCode, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, hyggeCode, cause, enableSuppression, writableStackTrace);
    }
}
