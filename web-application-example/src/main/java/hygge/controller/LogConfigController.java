package hygge.controller;

import hygge.commons.constant.enums.StringCategoryEnum;
import hygge.commons.template.definition.HyggeInfo;
import hygge.commons.template.definition.HyggeLogInfoObject;
import hygge.commons.annotation.HyggeExpressionInfo;
import hygge.domain.User;
import hygge.domain.UserSpecialForLog;
import hygge.util.UtilCreator;
import hygge.web.template.definition.HyggeController;
import hygge.web.template.HyggeControllerResponse;
import hygge.web.template.HyggeWebUtilContainer;
import hygge.web.util.log.annotation.ControllerLog;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Xavier
 * @date 2023/1/12
 */
@Slf4j
@RestController
@Tag(name = "LogConfigController", description = "演示日志配置相关操作，配置结果请从控制台日志观察")
public class LogConfigController extends HyggeWebUtilContainer implements HyggeController<ResponseEntity<?>> {
    @Override
    public boolean printNonSeriousExceptionLog(HyggeInfo exception) {
        // 修改这个方法会影响 “非严重异常” 的日志是否需要打印，用于快捷过虑掉非重要日志
        // 此处仍然恒定返回 true ,允许日志打印，有兴趣可以自行本地修改后调试
        return HyggeController.super.printNonSeriousExceptionLog(exception);
    }

    @PostMapping("/logConfig/user/standard")
    public ResponseEntity<?> createUserStandard(@RequestBody User user,
                                                @RequestParam(value = "prefix", required = false) String prefix) {
        String randomUid = randomHelper.getRandomString(6, StringCategoryEnum.A_Z, StringCategoryEnum.a_z, StringCategoryEnum.NUMBER);

        if (parameterHelper.isNotEmpty(prefix)) {
            randomUid = prefix.concat(randomUid);
        }
        user.setUid(randomUid);

        return success(user);
    }

    /**
     * 此处演示的是通过注解配置 ControllerLog 日志脱敏不展示 prefix 和 password
     * <p>
     * outputParamExpressions 中出现 "main" 是因为： {@link HyggeController#success(Object)} 默认返回的是 {@link HyggeControllerResponse} 类型，所以表达式是在访问 {@link HyggeControllerResponse} 实例
     */
    @ControllerLog(
            ignoreParamNames = {"prefix"},
            inputParamGetExpressions = {
                    @HyggeExpressionInfo(rootObjectName = "user", name = "change-uid", value = "uid"),
                    @HyggeExpressionInfo(rootObjectName = "user", name = "change-userName", value = "userName")
            },
            outputParamExpressions = {
                    @HyggeExpressionInfo(rootObjectName = "#root", name = "change-uid-response", value = "main == null ? null : main.uid"),
                    @HyggeExpressionInfo(rootObjectName = "#root", name = "change-userName-response", value = "main == null ? null : main.userName")
            }
    )
    @PostMapping("/logConfig/user/annotation")
    public ResponseEntity<?> createUserAnnotation(@RequestBody User user,
                                                  @RequestParam(value = "prefix", required = false) String prefix) {
        String randomUid = randomHelper.getRandomString(6, StringCategoryEnum.A_Z, StringCategoryEnum.a_z, StringCategoryEnum.NUMBER);

        if (parameterHelper.isNotEmpty(prefix)) {
            randomUid = prefix.concat(randomUid);
        }
        user.setUid(randomUid);

        return success(user);
    }

    /**
     * 此处演示的是通过特殊的 Dto 实体配置 ControllerLog 日志脱敏不展示 prefix 和 password
     * <p>
     * Dto 需要实现 {@link HyggeLogInfoObject} 接口
     */
    @PostMapping("/logConfig/user/specialDto")
    public ResponseEntity<?> createUserSpecialDto(@RequestBody UserSpecialForLog user,
                                                  @RequestParam(value = "prefix", required = false) String prefix) {
        String randomUid = randomHelper.getRandomString(6, StringCategoryEnum.A_Z, StringCategoryEnum.a_z, StringCategoryEnum.NUMBER);

        if (parameterHelper.isNotEmpty(prefix)) {
            randomUid = prefix.concat(randomUid);
        }
        user.setUid(randomUid);

        return success(user);
    }

    // 禁用该方法的自动 Controller 层日志
    @ControllerLog(enable = false)
    @GetMapping("/logConfig/disableAutoControllerLog")
    public ResponseEntity<?> disableAutoControllerLog() {
        log.info("info 模拟的日志信息");
        Map<Object, Object> mockObject = new HashMap<>();
        mockObject.put("1", 1);
        String mockObjectStringVal = UtilCreator.INSTANCE.getDefaultJsonHelperInstance(true).formatAsString(mockObject);
        log.warn("warn 模拟的日志对象 {}", mockObjectStringVal);
        log.error("error 模拟的日志信息");
        return success();
    }
}
