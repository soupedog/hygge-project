package hygge.web.utils.log.core;

import hygge.commons.templates.container.base.AbstractHyggeContext;
import hygge.web.utils.log.bo.ControllerLogInfo;

/**
 * ControllerAutoLog 上下文容器
 *
 * @author Xavier
 * @date 2022/7/14
 * @since 1.0
 */
public class ControllerAutoLogContext extends AbstractHyggeContext<ControllerAutoLogContext.Key> {
    private Long startTs;
    private ControllerLogInfo logInfo = new ControllerLogInfo();

    public ControllerAutoLogContext(Long startTs) {
        this.startTs = startTs;
    }

    public Long getStartTs() {
        return startTs;
    }

    public void setStartTs(Long startTs) {
        this.startTs = startTs;
    }

    public ControllerLogInfo getLogInfo() {
        return logInfo;
    }

    public void setLogInfo(ControllerLogInfo logInfo) {
        this.logInfo = logInfo;
    }

    public enum Key {
        RAW_RESPONSE,
        ;
    }
}
