package hygge.commons.spring.validator.definition;

import hygge.commons.template.definition.HyggeValidator;
import org.springframework.core.Ordered;

/**
 * spring 环境下的 Hygge 校验器，会被统一获取并执行。
 *
 * @author Xavier
 * @date 2023/3/28
 */
public interface HyggeSpringValidator extends HyggeValidator, Ordered {

    /**
     * 不用于与其他 Spring bean 先后顺序调整，仅用于调整 HyggeSpringValidator 实例间的排序
     */
    @Override
    default int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 1000;
    }
}
