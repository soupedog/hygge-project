package hygge.commons.exception;


import hygge.commons.constant.enums.GlobalHyggeCodeEnum;
import hygge.commons.template.definition.HyggeCode;
import hygge.commons.exception.main.HyggeException;

/**
 * 入参不符合预期异常
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public class ParameterException extends HyggeException {
    public ParameterException(String message) {
        super(message, GlobalHyggeCodeEnum.UNEXPECTED_PARAMETER);
    }

    public ParameterException(String message, Throwable cause) {
        super(message, GlobalHyggeCodeEnum.UNEXPECTED_PARAMETER, cause);
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
