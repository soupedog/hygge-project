package hygge.domain;

import hygge.commons.constant.enums.GlobalHyggeCodeEnum;
import hygge.commons.template.definition.HyggeCode;
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
    LOGIN_CONFLICT(false, "Login conflict.", 409000, HttpStatus.CONFLICT),
    ;

    static {
        // 如果你的 customResponseWrapper 会使用到 HyggeCode 的 code/extraInfo 属性，为了确保数据类型的统一性，你需要为 HyggeCode 的所有实例重新调整 code/extraInfo 的值，就像下面这样：
        GlobalHyggeCodeEnum.SUCCESS.setCode("200");
        GlobalHyggeCodeEnum.SUCCESS.setExtraInfo(HttpStatus.OK);

        // 默认情况 GlobalHyggeCode.code 是数字类型，此处二次赋值为回滚操作(为了不影响其他 Controller 演示，因为这是全局会受影响的改动)
        GlobalHyggeCodeEnum.SUCCESS.setCode(200);
    }

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