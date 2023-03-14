package hygge.commons.templates.definitions;


import hygge.utils.UtilsCreator;
import hygge.utils.definitions.JsonHelper;

/**
 * 可深度克隆接口
 *
 * @author Xavier
 * @date 2022/6/27
 * @since 1.0
 */
public interface DeepCloneable<T> {

    /**
     * 构造当前对象的一个深度克隆
     * </p>
     * 默认基于 Json 序列化反序列化的来实现深度克，请确保当前对象可被 Json 方式序列化与反序列化。<br/>
     * 如对性能有要求，请自行重写 {@link DeepCloneable#deepClone()}。
     *
     * @return 深度克隆的当前对象
     */
    @SuppressWarnings("unchecked")
    default T deepClone() {
        T target = (T) this;
        JsonHelper<?> jsonHelper = UtilsCreator.INSTANCE.getDefaultJsonHelperInstance(false);
        return (T) jsonHelper.readAsObject(jsonHelper.formatAsString(this), target.getClass());
    }
}
