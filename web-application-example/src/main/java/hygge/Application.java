package hygge;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Xavier
 * @date 2022/7/2
 * @since 1.0
 */
@Slf4j
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        /*
         * 等价于 "SpringApplication.run(Application.class)"
         * */
        SpringApplication springApplication = new SpringApplication(Application.class);
        springApplication.run(args);

        log.info("\"asdasdasd\r\nasdasdasd}\r\n\"");
    }
}
