/*
 * Copyright 2022-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hygge.web.util.log;

import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Controller 层自动日志的 Advisor
 *
 * @author Xavier
 * @date 2022/7/15
 * @since 1.0
 */
public class ControllerLogAdvisor extends AbstractBeanFactoryPointcutAdvisor implements BeanPostProcessor {
    /**
     * i just copy from {@link AbstractBeanFactoryPointcutAdvisor#advice}
     */
    private transient volatile Pointcut pointcut;

    public ControllerLogAdvisor(int order, MethodInterceptor methodInterceptor, Pointcut pointcut) {
        setOrder(order);
        setAdvice(methodInterceptor);
        this.pointcut = pointcut;
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }
}
