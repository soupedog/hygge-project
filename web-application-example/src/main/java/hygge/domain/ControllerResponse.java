package hygge.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import hygge.web.template.HyggeControllerResponse;

/**
 * 自定义 Controller 返回类型
 *
 * @author Xavier
 * @date 2022/7/7
 * @since 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ControllerResponse<T> extends HyggeControllerResponse<Integer, T> {
}
