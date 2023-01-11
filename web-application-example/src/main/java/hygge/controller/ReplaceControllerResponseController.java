package hygge.controller;

import hygge.commons.exceptions.code.GlobalHyggeCode;
import hygge.domain.ControllerResponse;
import hygge.domain.User;
import hygge.web.template.HyggeController;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 演示替换 response 默认结构用的 Controller
 *
 * @author Xavier
 * @date 2022/7/7
 * @since 1.0
 */
@Slf4j
@RestController
@Tag(name = "ReplaceControllerResponseController", description = "演示自定义数据结构相关操作，请与 '/logConfig/user/standard' 进行比较")
public class ReplaceControllerResponseController implements HyggeController<ResponseEntity<?>> {
    private static final HyggeControllerResponseWrapper<ResponseEntity<?>> customResponseWrapper = (httpStatus, headers, hyggeCode, msg, entity, throwable) -> {
        ResponseEntity.BodyBuilder result = ResponseEntity.status(httpStatus);
        return result.body(entity);
    };

    /**
     * 这是示例：演示自定义输出内容，此处是自定义为去掉 {@link ControllerResponse} 的主协议封装
     */
    @GetMapping("/replace/customResponse")
    public ResponseEntity<?> customResponse() {
        return success(HttpStatus.OK, null, GlobalHyggeCode.SUCCESS, null, new User("customResponse", "customResponse", "customResponse"), customResponseWrapper);
    }
}
