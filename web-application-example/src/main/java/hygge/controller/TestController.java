package hygge.controller;

import hygge.commons.exceptions.InternalRuntimeException;
import hygge.commons.exceptions.LightRuntimeException;
import hygge.commons.exceptions.code.GlobalHyggeCode;
import hygge.domain.ControllerResponse;
import hygge.utils.UtilsCreator;
import hygge.web.template.HyggeController;
import hygge.web.utils.log.annotation.ControllerAutoLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 演示用的 Controller
 *
 * @author Xavier
 * @date 2022/7/7
 * @since 1.0
 */
@Slf4j
@RestController
public class TestController implements HyggeController<ResponseEntity<?>> {
    /**
     * 这是示例：swagger 友好型，在无手动特殊标记描述性注解时， swagger 默认能展示较多信息</br>
     * 但是强转显得不太优雅，只能你自己实现一个无泛型，已是具体类型的 {@link HyggeController} 类了
     */
    @GetMapping("/swaggerFriendly")
    public ResponseEntity<ControllerResponse<Timestamp>> swaggerFriendly() {
        return (ResponseEntity<ControllerResponse<Timestamp>>) success(new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 这是示例：swagger 不友好型，除非你手动特殊标记描述性注解，不然swagger 几乎提供不了信息
     */
    @GetMapping("/swaggerUnfriendly")
    public ResponseEntity<?> swaggerUnfriendly() {
        return success(new Timestamp(System.currentTimeMillis()));
    }

    private static final HyggeControllerResponseWrapper<ResponseEntity<?>> customResponseWrapper = (httpStatus, headers, hyggeCode, msg, entity, throwable) -> {
        ResponseEntity.BodyBuilder result = ResponseEntity.status(httpStatus);
        return result.body(entity);
    };

    /**
     * 这是示例：演示自定义输出内容，此处是自定义为去掉 {@link ControllerResponse} 的主协议封装
     */
    @GetMapping("/custom")
    public ResponseEntity<?> custom() {
        return success(HttpStatus.OK, null, GlobalHyggeCode.SUCCESS, null, new Timestamp(System.currentTimeMillis()), customResponseWrapper);
    }

    @GetMapping("/mockLog")
    @ControllerAutoLog(enable = false)
    public ResponseEntity<?> mockLog() {
        log.info("info 模拟的日志信息");
        Map<Object, Object> mockObject = new HashMap<>();
        mockObject.put("1", 1);
        String mockObjectStringVal = UtilsCreator.INSTANCE.getDefaultJsonHelperInstance(true).formatAsString(mockObject);
        log.warn("warn 模拟的日志对象 {}", mockObjectStringVal);
        log.error("error 模拟的日志信息");

        if (ThreadLocalRandom.current().nextBoolean()) {
            if (ThreadLocalRandom.current().nextBoolean()) {
                throw new LightRuntimeException("模拟轻量型运行时异常");
            } else {
                throw new InternalRuntimeException("模拟服务端内部异常");
            }
        }
        return success();
    }
}
