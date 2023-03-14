package hygge.domain;

import hygge.commons.constants.enums.definitions.HyggeCode;
import org.springframework.http.HttpStatus;

/**
 * 自定义的业务码列表
 *
 * @author Xavier
 * @date 2023/1/12
 */
public enum CustomSystemCode implements HyggeCode {
    /**
     * 用户登录信息有误
     */
    LOGIN_ILLEGAL(false, "Unexpected login info.", 403000, HttpStatus.FORBIDDEN),
    ;

    private final boolean serious;
    private final String publicMessage;
    private final Integer code;
    private final HttpStatus extraInfo;

    CustomSystemCode(boolean serious, String publicMessage, int code, HttpStatus extraInfo) {
        this.serious = serious;
        this.publicMessage = publicMessage;
        this.code = code;
        this.extraInfo = extraInfo;
    }

    @Override
    public boolean isSerious() {
        return serious;
    }

    @Override
    public String getPublicMessage() {
        return publicMessage;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public HttpStatus getExtraInfo() {
        return extraInfo;
    }
}