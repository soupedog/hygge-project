package hygge.web.utils.log.bo;

import hygge.commons.templates.container.base.AbstractHyggeKeeper;
import hygge.web.utils.log.definitions.BaseControllerLogHandler;
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
