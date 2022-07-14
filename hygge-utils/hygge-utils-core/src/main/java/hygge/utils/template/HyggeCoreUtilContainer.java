package hygge.utils.template;

import hygge.utils.UtilsCreator;
import hygge.utils.definitions.CollectionHelper;
import hygge.utils.definitions.ParameterHelper;
import hygge.utils.definitions.RandomHelper;
import hygge.utils.definitions.TimeHelper;

/**
 * 核心 Hygge 工具容器
 *
 * @author Xavier
 * @date 2022/7/14
 * @since 1.0
 */
public abstract class HyggeCoreUtilContainer {
    protected HyggeCoreUtilContainer() {
    }

    protected static final CollectionHelper collectionHelper = UtilsCreator.INSTANCE.getDefaultInstance(CollectionHelper.class);
    protected static final ParameterHelper parameterHelper = UtilsCreator.INSTANCE.getDefaultInstance(ParameterHelper.class);
    protected static final RandomHelper randomHelper = UtilsCreator.INSTANCE.getDefaultInstance(RandomHelper.class);
    protected static final TimeHelper timeHelper = UtilsCreator.INSTANCE.getDefaultInstance(TimeHelper.class);
}
