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

package hygge.commons.spring.listener;

import hygge.commons.spring.validator.definition.HyggeSpringValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * HyggeSpringValidator 执行的触发器
 *
 * @author Xavier
 * @date 2023/8/8
 * @since 1.0
 */
public class HyggeSpringValidatorRunnerListener implements Ordered, ApplicationListener<ApplicationReadyEvent> {
    private static final Logger log = LoggerFactory.getLogger(HyggeSpringValidatorRunnerListener.class);

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ConfigurableApplicationContext context = event.getApplicationContext();

        Map<String, HyggeSpringValidator> validatorMap = context.getBeansOfType(HyggeSpringValidator.class);

        // 未检测到 "HyggeSpringValidator" 实例时提前结束
        if (validatorMap.isEmpty()) {
            return;
        }

        List<HyggeSpringValidator> hyggeSpringValidatorList = new ArrayList<>(validatorMap.values());

        // 从小到大排序
        hyggeSpringValidatorList.sort(Comparator.comparingInt(HyggeSpringValidator::getOrder));

        for (HyggeSpringValidator validator : hyggeSpringValidatorList) {
            validator.validate();
        }

        List<String> validatorNames = new ArrayList<>();
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();

        // 验证是一次性动作，执行完后可以进行移除
        for (HyggeSpringValidator validator : hyggeSpringValidatorList) {
            beanFactory.destroyBean(validator);
            validatorNames.add(validator.getClass().getSimpleName());
        }

        log.info("HyggeSpringValidators execution successful:{}", validatorNames);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 100;
    }
}
