package hygge.web.template;

/**
 * 请求的默认返回对象
 *
 * @author Xavier
 * @date 2022/7/5
 * @since 1.0
 */
public class HyggeControllerResponse<C, T> {
    /**
     * 自定义业务码
     */
    private C code;
    /**
     * 提示信息
     */
    private String msg;
    /**
     * 返回核心内容
     */
    private T main;

    public C getCode() {
        return code;
    }

    public void setCode(C code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getMain() {
        return main;
    }

    public void setMain(T main) {
        this.main = main;
    }
}
