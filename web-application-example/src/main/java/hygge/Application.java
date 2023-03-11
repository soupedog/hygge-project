package hygge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 监控端点列表 "http://localhost:8080/actuator"
 * Swagger 链接 "http://localhost:8080/swagger-ui/index.html"
 *
 * @author Xavier
 * @date 2022/7/2
 * @since 1.0
 */
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        /*
         * 等价于 "SpringApplication.run(Application.class)"
         * */
        SpringApplication springApplication = new SpringApplication(Application.class);
        springApplication.run(args);
    }
}
