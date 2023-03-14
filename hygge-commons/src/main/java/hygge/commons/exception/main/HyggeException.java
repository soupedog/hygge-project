package hygge.commons.exception.main;

import hygge.commons.constant.enums.definition.HyggeCode;
import hygge.commons.exception.definition.HyggeInfo;

/**
 * Hygge 异常
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public class HyggeException extends Exception implements HyggeInfo {
    protected final transient HyggeCode hyggeCode;

    public HyggeException(HyggeCode hyggeCode) {
        super(hyggeCode.getPublicMessage());
        this.hyggeCode = hyggeCode;
    }

    public HyggeException(String message, HyggeCode hyggeCode) {
        super(message);
        this.hyggeCode = hyggeCode;
    }

    public HyggeException(String message, HyggeCode hyggeCode, Throwable cause) {
        super(message, cause);
        this.hyggeCode = hyggeCode;
    }

    public HyggeException(String message, HyggeCode hyggeCode, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.hyggeCode = hyggeCode;
    }

    @Override
    public HyggeCode getHyggeCode() {
        return hyggeCode;
    }
}
