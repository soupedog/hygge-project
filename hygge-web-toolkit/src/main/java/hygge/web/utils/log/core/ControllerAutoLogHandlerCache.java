package hygge.web.utils.log.core;

import hygge.commons.templates.container.base.AbstractHyggeKeeper;
import hygge.web.utils.log.core.handler.BaseControllerAutoLogHandler;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;

/**
 * ControllerAutoLogHandler 缓存
 *
 * @author Xavier
 * @date 2022/7/15
 * @since 1.0
 */
public class ControllerAutoLogHandlerCache extends AbstractHyggeKeeper<Method, BaseControllerAutoLogHandler> implements BeanPostProcessor {
}
