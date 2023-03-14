package hygge.web.template;

import hygge.utils.UtilsCreator;
import hygge.util.definition.JsonHelper;
import hygge.utils.template.HyggeCoreUtilContainer;

/**
 * web 服务 Hygge 工具容器
 *
 * @author Xavier
 * @date 2022/7/14
 * @since 1.0
 */
public class HyggeWebUtilContainer extends HyggeCoreUtilContainer {
    protected static final JsonHelper<?> jsonHelper = UtilsCreator.INSTANCE.getDefaultJsonHelperInstance(false);
    protected static final JsonHelper<?> jsonHelperIndent = UtilsCreator.INSTANCE.getDefaultJsonHelperInstance(true);
}
