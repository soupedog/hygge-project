package hygge.controller;

import hygge.commons.constant.enums.GlobalHyggeCodeEnum;
import hygge.domain.ControllerResponse;
import hygge.domain.User;
import hygge.web.template.definition.HyggeController;
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

    static {
        // 如果你的 customResponseWrapper 会使用到 HyggeCode 的 code/extraInfo 属性，为了确保数据类型的统一性，你需要为 HyggeCode 的所有实例重新调整 code/extraInfo 的值，就像下面这样：
        GlobalHyggeCodeEnum.SUCCESS.setCode("200");
        GlobalHyggeCodeEnum.SUCCESS.setExtraInfo(HttpStatus.OK);

        // 默认情况 GlobalHyggeCode.code 是数字类型，此处二次赋值为回滚操作(为了不影响其他 Controller 演示，因为这是全局会受影响的改动)
        GlobalHyggeCodeEnum.SUCCESS.setCode(200);
    }

    private static final HyggeControllerResponseWrapper<ResponseEntity<?>> customResponseWrapper = (httpStatus, headers, hyggeCode, msg, entity, throwable) -> {
        ResponseEntity.BodyBuilder result = ResponseEntity.status(httpStatus);
        return result.body(entity);
    };

    /**
     * 这是示例：演示自定义输出内容，此处是自定义为去掉 {@link ControllerResponse} 的主协议封装
     */
    @GetMapping("/replace/customResponse")
    public ResponseEntity<?> customResponse() {
        return success(HttpStatus.OK, null, GlobalHyggeCodeEnum.SUCCESS, null, new User("customResponse", "customResponse", "customResponse"), customResponseWrapper);
    }
}
