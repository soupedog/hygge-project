package hygge.commons.constants.enums;

import hygge.commons.constants.enums.definitions.HyggeCode;

/**
 * 通用型全局业务码
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public enum GlobalHyggeCode implements HyggeCode {
    /**
     * 响应正常
     */
    SUCCESS(false, null, 200, null),
    /**
     * 默认的工具类引发异常
     */
    UTIL_EXCEPTION(true, "Internal Server Error", 500, null),
    /**
     * 默认的服务端引发异常
     */
    SERVER_END_EXCEPTION(true, "Internal Server Error", 500, null),
    /**
     * 默认的依赖的远端系统引发异常
     */
    EXTERNAL_SYSTEM_EXCEPTION(true, "External Server Error", 500, null),
    /**
     * 默认的客户端端引发异常
     */
    CLIENT_END_EXCEPTION(false, null, 400, null),
    /**
     * 默认的不符合预期的入参入参引发异常
     */
    UNEXPECTED_PARAMETER(false, null, 400, null),
    ;

    private final boolean serious;
    private final String publicMessage;
    private Object code;
    private Object extraInfo;

    @Override
    public boolean isSerious() {
        return serious;
    }

    @Override
    public String getPublicMessage() {
        return publicMessage;
    }

    @Override
    public <C> C getCode() {
        return (C) code;
    }

    @Override
    public <E> E getExtraInfo() {
        return (E) extraInfo;
    }

    GlobalHyggeCode(boolean serious, String publicMessage, Object code, Object extraInfo) {
        this.extraInfo = extraInfo;
        this.serious = serious;
        this.publicMessage = publicMessage;
        this.code = code;
    }

    public void setCode(Object code) {
        this.code = code;
    }

    public void setExtraInfo(Object extraInfo) {
        this.extraInfo = extraInfo;
    }
}
