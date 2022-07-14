package hygge.web.utils.log.core;

import hygge.commons.templates.container.base.AbstractHyggeKeeper;
import hygge.commons.templates.core.annotation.HyggeExpressionInfo;
import org.springframework.expression.Expression;

import java.util.Map;

/**
 * Expression 缓存容器</br>
 * key:{@link HyggeExpressionInfo#name()}
 *
 * @author Xavier
 * @date 2022/7/14
 * @since 1.0
 */
public class ExpressionCache extends AbstractHyggeKeeper<String, Expression> {
    public Map<String, Expression> getContainer() {
        return container;
    }
}
