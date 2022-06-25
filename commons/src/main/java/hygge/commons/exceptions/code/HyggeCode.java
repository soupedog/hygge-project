package hygge.commons.exceptions.code;

/**
 * 业务码
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public interface HyggeCode<C, E> {
    /**
     * 是否为严重问题。
     * <p/>
     * 返回 false 通常代表是入参引发的问题，有可能通过重试自愈<br/>
     * 返回 true 时需要人工介入检查，通常无法自愈
     */
    boolean serious();

    /**
     * 获取已脱敏可暴露给系统外部的提示信息
     */
    String getPublicMessage();

    /**
     * 获取业务标识码
     */
    C getCode();

    /**
     * 用于自由拓展的额外的信息，你可以通过异常向外部传递某些信息
     */
    E getExtraInfo();
}