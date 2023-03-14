package hygge.web.util.log.bo;

import hygge.commons.template.container.base.AbstractHyggeKeeper;
import hygge.web.util.log.base.BaseControllerLogHandler;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;

/**
 * ControllerLogHandler 缓存
 *
 * @author Xavier
 * @date 2022/7/15
 * @since 1.0
 */
public class ControllerLogHandlerCache extends AbstractHyggeKeeper<Method, BaseControllerLogHandler> implements BeanPostProcessor {
}
