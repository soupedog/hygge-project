package hygge.util.template;

import hygge.commons.template.definition.Alterego;
import hygge.util.UtilCreator;
import hygge.util.definition.CollectionHelper;
import hygge.util.definition.ParameterHelper;
import hygge.util.definition.RandomHelper;
import hygge.util.definition.TimeHelper;

/**
 * 核心 Hygge 工具容器
 *
 * @author Xavier
 * @date 2022/7/14
 * @since 1.0
 */
public abstract class HyggeCoreUtilContainer implements Alterego {
    protected HyggeCoreUtilContainer() {
    }

    protected static final CollectionHelper collectionHelper = UtilCreator.INSTANCE.getDefaultInstance(CollectionHelper.class);
    protected static final ParameterHelper parameterHelper = UtilCreator.INSTANCE.getDefaultInstance(ParameterHelper.class);
    protected static final RandomHelper randomHelper = UtilCreator.INSTANCE.getDefaultInstance(RandomHelper.class);
    protected static final TimeHelper timeHelper = UtilCreator.INSTANCE.getDefaultInstance(TimeHelper.class);
}
