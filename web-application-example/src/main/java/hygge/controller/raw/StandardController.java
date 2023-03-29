package hygge.controller.raw;

import hygge.domain.ControllerResponse;
import hygge.domain.User;
import hygge.web.template.HyggeWebUtilContainer;
import hygge.web.template.definition.HyggeController;
import hygge.web.util.log.bo.ControllerLogInfo;
import hygge.web.util.log.enums.ControllerLogType;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Xavier
 * @date 2023/3/29
 * @since 1.0
 */
@Slf4j
@Hidden
@RestController
@Tag(name = "RawStandardController", description = "Raw 默认标准模板")
public class StandardController extends HyggeWebUtilContainer implements HyggeController<ResponseEntity<?>> {
    /**
     * 仅用于 AOP 性能评估，见 {@link hygge.controller.hygge.RawControllerTest}
     * <p>
     * 在近似完成相同工作量的情况下，一个 aop 一个非 aop
     */
    @RequestMapping(method = RequestMethod.GET, path = "/raw/swaggerFriendly")
    @ApiResponse(description = "swagger 友好形式，swagger 默认扫描即可显示接口出参数据结构", responseCode = "200")
    public ResponseEntity<ControllerResponse<User>> swaggerFriendly() {
        Long startTs = System.currentTimeMillis();
        User resultTemp = new User("swaggerFriendly", "swaggerFriendly", "swaggerFriendly");

        ControllerResponse controllerResponse = new ControllerResponse();
        controllerResponse.setMain(resultTemp);
        controllerResponse.setCode(200);

        ControllerLogInfo controllerLogInfo = new ControllerLogInfo();
        controllerLogInfo.setType(ControllerLogType.GET);
        controllerLogInfo.setPath("/raw/swaggerFriendly");
        controllerLogInfo.setOutput(controllerResponse);

        try {
            return (ResponseEntity<ControllerResponse<User>>) success(resultTemp);
        } finally {
            controllerLogInfo.setCost(System.currentTimeMillis() - startTs);
            log.info("ControllerLog:" + jsonHelper.formatAsString(controllerLogInfo));
        }
    }
}
