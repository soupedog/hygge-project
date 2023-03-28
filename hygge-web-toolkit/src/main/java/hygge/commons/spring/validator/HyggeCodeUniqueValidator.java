package hygge.commons.spring.validator;

import hygge.commons.exception.InternalRuntimeException;
import hygge.commons.spring.config.configuration.HyggeSpringValidatorConfiguration;
import hygge.commons.spring.validator.definition.HyggeSpringValidator;
import hygge.commons.template.definition.HyggeCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * HyggeCode 校验器，确保其 code 是全局唯一的未发生定义冲突
 *
 * @author Xavier
 * @date 2023/3/28
 * @since 1.0
 */
@Component
@EnableConfigurationProperties(HyggeSpringValidatorConfiguration.class)
@ConditionalOnExpression("#{environment['hygge.web-toolkit.validator.hyggeCode.basePackages'] != null && true.equals(environment['hygge.web-toolkit.validator.hyggeCode.uniqueEnable'])}")
public class HyggeCodeUniqueValidator implements HyggeSpringValidator {
    public static final String GLOBAL_HYGGE_CODE_ENUM_BASE_PATH = "hygge.commons.constant.enums";

    @Autowired
    private HyggeSpringValidatorConfiguration hyggeSpringValidatorConfiguration;

    @Override
    public void validate() {
        Collection<Class<HyggeCode>> hyggeCodeCollection = getHyggeCodeFromLocalSystem(hyggeSpringValidatorConfiguration.getHyggeCode().getBasePackages());
        uniqueValidate(hyggeCodeCollection);
    }

    /**
     * 要求 HyggeCode 实例是枚举，且 code 不重复
     */
    public void uniqueValidate(Collection<Class<HyggeCode>> hyggeCodeClassArray) {
        HashMap<Object, HyggeCode> container = new HashMap<>();

        for (Class<HyggeCode> hyggeCodeClass : hyggeCodeClassArray) {
            // 如果不是枚举，数组会为空
            HyggeCode[] hyggeCodeArray = hyggeCodeClass.getEnumConstants();
            if (hyggeCodeArray == null) {
                throw new InternalRuntimeException("The instance of hyggeCode must be a non-empty Enum class, but we found " + hyggeCodeClass.getName() + ".");
            }

            for (HyggeCode hyggeCode : hyggeCodeArray) {
                HyggeCode conflictCode = container.put(hyggeCode.getCode(), hyggeCode);
                if (conflictCode != null) {
                    String firstOneInfo = conflictCode.getClass().getName() + "." + conflictCode;
                    String secondOneInfo = hyggeCode.getClass().getName() + "." + hyggeCode;
                    throw new InternalRuntimeException("HyggeCode conflict occurs(" + firstOneInfo + " & " + secondOneInfo + "), please make sure they have a unique code value.");
                }
            }
        }
    }

    /**
     * 所有 Hygge 都定义在本地的应用来获取 HyggeCode 定义的方法
     *
     * @param customBasePath 自定义 HyggeCode 包路径，多个时用 "," 隔开
     */
    public Collection<Class<HyggeCode>> getHyggeCodeFromLocalSystem(String customBasePath) {
        String[] array = customBasePath.split(",");

        Collection<Class<HyggeCode>> result = new ArrayList<>();
        // 是否启用默认过滤器
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        // 要求是 HyggeCode 实例
        provider.addIncludeFilter(new AssignableTypeFilter(HyggeCode.class));

        Set<BeanDefinition> hyggeCodeBeanDefinitionSet = new LinkedHashSet<>();

        for (String path : array) {
            Set<BeanDefinition> customComponents = provider.findCandidateComponents(path);
            hyggeCodeBeanDefinitionSet.addAll(customComponents);
        }

        Set<BeanDefinition> globalComponents = provider.findCandidateComponents(GLOBAL_HYGGE_CODE_ENUM_BASE_PATH);
        hyggeCodeBeanDefinitionSet.addAll(globalComponents);

        for (BeanDefinition component : hyggeCodeBeanDefinitionSet) {
            try {
                Class<HyggeCode> cls = (Class<HyggeCode>) Class.forName(component.getBeanClassName());
                result.add(cls);
            } catch (ClassNotFoundException e) {
                throw new InternalRuntimeException("Fail to get Class info of" + component.getBeanClassName() + ".", e);
            }
        }
        return result;
    }
}
