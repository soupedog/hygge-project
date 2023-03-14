package hygge.web.template;

import hygge.util.UtilCreator;
import hygge.util.definition.JsonHelper;
import hygge.util.template.HyggeCoreUtilContainer;

/**
 * web 服务 Hygge 工具容器
 * <p>
 * 写作抽象类，本体其实方便子类获取 HyggeUtil 示例的代码模板。(利用抽象类不可被实例化的特性)
 *
 * @author Xavier
 * @date 2022/7/14
 * @since 1.0
 */
public abstract class HyggeWebUtilContainer extends HyggeCoreUtilContainer {
    protected static final JsonHelper<?> jsonHelper = UtilCreator.INSTANCE.getDefaultJsonHelperInstance(false);
    protected static final JsonHelper<?> jsonHelperIndent = UtilCreator.INSTANCE.getDefaultJsonHelperInstance(true);
}
