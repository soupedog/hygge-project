package hygge.controller;

import hygge.commons.exceptions.InternalRuntimeException;
import hygge.commons.exceptions.LightRuntimeException;
import hygge.domain.ControllerResponse;
import hygge.domain.User;
import hygge.web.template.HyggeController;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Xavier
 * @date 2023/1/11
 */
@Slf4j
@RestController
@Tag(name = "StandardHyggeController", description = "默认标准模板")
public class StandardHyggeController implements HyggeController<ResponseEntity<?>> {
    /**
     * 这是示例：swagger 不友好型，除非你手动特殊标记描述性注解，不然swagger 几乎提供不了信息
     */
    @GetMapping("/standard/swaggerUnfriendly")
    @ApiResponse(description = "swagger 不友好形式，仅能通过 swagger 相关注解、配置方式描述出参数据结构，否则出参数据结构无法展示", responseCode = "200")
    public ResponseEntity<?> swaggerUnfriendly() {
        return success(new User("swaggerUnfriendly", "swaggerUnfriendly", "swaggerUnfriendly"));
    }

    /**
     * 这是示例：swagger 友好型，在无手动特殊标记描述性注解时， swagger 默认能展示较多信息</br>
     * 但是强转显得不太优雅，只能你自己实现一个无泛型，已是具体类型的 {@link HyggeController} 类了
     */
    @GetMapping("/standard/swaggerFriendly")
    @ApiResponse(description = "swagger 友好形式，swagger 默认扫描即可显示接口出参数据结构", responseCode = "200")
    public ResponseEntity<ControllerResponse<User>> swaggerFriendly() {
        return (ResponseEntity<ControllerResponse<User>>) success(new User("swaggerFriendly", "swaggerFriendly", "swaggerFriendly"));
    }

    @GetMapping("/standard/mockException")
    public ResponseEntity<?> mockException() {
        if (ThreadLocalRandom.current().nextBoolean()) {
            throw new LightRuntimeException("模拟轻量型运行时异常");
        } else {
            throw new InternalRuntimeException("模拟服务端内部异常");
        }
    }
}
