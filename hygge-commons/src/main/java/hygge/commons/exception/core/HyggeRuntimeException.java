package hygge.commons.exception.core;

import hygge.commons.constant.enums.definition.HyggeCode;
import hygge.commons.exception.definition.HyggeInfo;

/**
 * Hygge 运行时异常
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public class HyggeRuntimeException extends RuntimeException implements HyggeInfo {
    protected final transient HyggeCode hyggeCode;

    public HyggeRuntimeException(HyggeCode hyggeCode) {
        super(hyggeCode.getPublicMessage());
        this.hyggeCode = hyggeCode;
    }

    public HyggeRuntimeException(String message, HyggeCode hyggeCode) {
        super(message);
        this.hyggeCode = hyggeCode;
    }

    public HyggeRuntimeException(String message, HyggeCode hyggeCode, Throwable cause) {
        super(message, cause);
        this.hyggeCode = hyggeCode;
    }

    public HyggeRuntimeException(String message, HyggeCode hyggeCode, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.hyggeCode = hyggeCode;
    }

    @Override
    public HyggeCode getHyggeCode() {
        return hyggeCode;
    }
}
