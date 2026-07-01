package hygge.commons.spring.event;

import org.springframework.context.ApplicationEventPublisher;

import java.time.Clock;
import java.util.function.BiFunction;

/**
 * @author Xavier
 * @date 2026/6/30
 */
public abstract class BaseHyggeEventService {
    protected final ApplicationEventPublisher applicationEventPublisher;

    protected BaseHyggeEventService(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * @param event 不可为 null
     * @see BaseHyggeEventListener 消费者
     */
    public void fireEvent(BaseHyggeEvent<?> event) {
        applicationEventPublisher.publishEvent(event);
    }

    /**
     * 低版本 JDK 不允许 super 放在第二行，只能通过这种方式来进行入参类型转换
     *
     * @param source    SpringEvent 的 source
     * @param construct 调用两个入参构造函数的 Lambda 表达式
     */
    public <T, E extends BaseHyggeEvent<T>> DefaultHyggeEventBuilder<T, E> buildEvent(T source, BiFunction<T, Clock, E> construct) {
        return new DefaultHyggeEventBuilder<>(construct, source);
    }
}