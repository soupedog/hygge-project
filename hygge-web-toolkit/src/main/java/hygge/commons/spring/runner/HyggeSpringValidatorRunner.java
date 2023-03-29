package hygge.commons.spring.runner;

import hygge.commons.spring.validator.definition.HyggeSpringValidator;
import hygge.util.UtilCreator;
import hygge.util.definition.ParameterHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 用于激活有效的 {@link HyggeSpringValidator} 实例进行校验
 *
 * @author Xavier
 * @date 2023/3/28
 * @since 1.0
 */
@Component
public class HyggeSpringValidatorRunner implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(HyggeSpringValidatorRunner.class);

    private final List<HyggeSpringValidator> hyggeSpringValidatorList;
    private final DefaultListableBeanFactory defaultListableBeanFactory;

    public HyggeSpringValidatorRunner(@Autowired List<HyggeSpringValidator> hyggeSpringValidatorList, @Autowired DefaultListableBeanFactory defaultListableBeanFactory) {
        this.hyggeSpringValidatorList = hyggeSpringValidatorList;
        this.defaultListableBeanFactory = defaultListableBeanFactory;
    }

    @Override
    public void run(String... args) {
        if (hyggeSpringValidatorList.isEmpty()) {
            return;
        }

        hyggeSpringValidatorList.sort(Comparator.comparingInt(HyggeSpringValidator::getOrder));

        for (HyggeSpringValidator validator : hyggeSpringValidatorList) {
            validator.validate();
        }

        ParameterHelper parameterHelper = UtilCreator.INSTANCE.getDefaultInstance(ParameterHelper.class);

        List<String> validatorNames = new ArrayList<>();
        // 扫尾清理
        for (HyggeSpringValidator validator : hyggeSpringValidatorList) {
            defaultListableBeanFactory.destroyBean(validator);
            // 此处使用的 Bean 默认名称，进行了重命名的 Bean 将不被支持
            defaultListableBeanFactory.removeBeanDefinition(parameterHelper.lowerCaseFirstLetter(validator.getClass().getSimpleName()));
            validatorNames.add(validator.getClass().getSimpleName());
        }

        log.info("HyggeSpringValidators execution successful:{}", validatorNames);
    }
}
