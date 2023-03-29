package hygge.controller.hygge;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static hygge.controller.hygge.RawControllerTest.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 冷启动测试结果：完成近似相同工作量，AOP 并未产生明显性能下降
 *
 * @author Xavier
 * @date 2023/3/29
 * @since 1.0
 */
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class StandardHyggeControllerTest {
    @Test
    void aop(@Autowired MockMvc mvc) throws Exception {
        mvc.perform(get("/actuator/health")).andExpect(status().isOk());

        long startTs = System.currentTimeMillis();

        for (int i = 0; i < times; i++) {
            mvc.perform(get("/standard/swaggerFriendly")).andExpect(status().isOk());
        }

        log.info("Aop 耗时:" + (System.currentTimeMillis() - startTs));
    }
}
