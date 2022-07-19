package hygge.utils;

import hygge.commons.exceptions.UtilRuntimeException;
import hygge.utils.definitions.JsonHelper;
import hygge.utils.definitions.RandomUniqueGenerator;

import java.util.Properties;
import java.util.function.Supplier;

import static hygge.utils.UtilsCreatorConfigurationKeeper.DEFAULT_JACKSON_JSON_HELPER_CLASS_NAME;
import static hygge.utils.UtilsCreatorConfigurationKeeper.KEY_ACTUAL_DEFAULT_JSON_HELPER;

/**
 * HyggeUtil 示例构造器
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
@SuppressWarnings("unchecked")
public enum UtilsCreator implements UtilsCreatorAbility, InfoMessageSupplier {
    /**
     * UtilsCreator 单例
     */
    INSTANCE;
    /**
     * UtilsCreator 配置项容器
     */
    public static final UtilsCreatorConfigurationKeeper CONFIG_KEEPER = new UtilsCreatorConfigurationKeeper();
    /**
     * 用于保存单例工具类的容器，key 是 {@link HyggeUtil#getHyggeName()}
     */
    private static final UtilsCreatorHyggeUtilKeeper singletonObjects = new UtilsCreatorHyggeUtilKeeper(64, 0.75F);

    /**
     * 先从 {@link UtilsCreator#singletonObjects} 里根据 cacheKey 取，如果没取到则通过构造方法获取实例，并将该实例缓存到 {@link UtilsCreator#singletonObjects}
     */
    protected static <H extends HyggeUtil> H getAndCacheInstance(String cacheKey, Supplier<H> constructor) {
        H result = (H) singletonObjects.getValue(cacheKey);
        if (result == null) {
            synchronized (singletonObjects) {
                result = (H) singletonObjects.getValue(cacheKey);
                if (result == null) {
                    result = constructor.get();
                    singletonObjects.saveValue(cacheKey, result);
                }
            }
        }
        return result;
    }

    @Override
    public <T extends HyggeUtil> T createHelper(Class<T> target) {
        return createInstanceByClass(target);
    }

    @Override
    public <T extends HyggeUtil> T createHelper(String className) {
        try {
            Class<?> defaultClass = UtilsCreator.class.getClassLoader().loadClass(className);
            Object resultTemp = defaultClass.newInstance();
            if (!(resultTemp instanceof HyggeUtil)) {
                throw new UtilRuntimeException(String.format("Class(%s) should implement HyggeUtil.", className));
            }
            return (T) resultTemp;
        } catch (ClassNotFoundException e) {
            throw new UtilRuntimeException(String.format("Class(%s) was not found.", className), e);
        } catch (IllegalAccessException | InstantiationException e) {
            throw new UtilRuntimeException(String.format("Fail to create instance of Class(%s).", className), e);
        }
    }

    @Override
    public <T extends HyggeUtil> T getDefaultInstance(Class<T> target) {
        String cacheKey = target.getSimpleName();
        return getAndCacheInstance(cacheKey, () -> {
            String className = null;

            if (JsonHelper.class.isAssignableFrom(target)) {
                // 构造 JsonHelper 默认实现需要先检查依赖
                if (tryToGetClass("com.fasterxml.jackson.databind.ObjectMapper") == null) {
                    throw new UtilRuntimeException("Default \"JsonHelper\" implementation class lacks jackson dependencies:com.fasterxml.jackson.databind.ObjectMapper.");
                }
                className = getDefaultJacksonJsonHelperPath();
            } else if (RandomUniqueGenerator.class.isAssignableFrom(target)) {
                className = String.format("hygge.utils.impl.%s", "SnowFlakeGenerator");
            }

            if (className == null) {
                className = String.format("hygge.utils.impl.Default%s", cacheKey);
            }
            return createHelper(className);
        });
    }

    @Override
    public <T> JsonHelper<T> getDefaultJsonHelperInstance(boolean indent) {
        String hyggeName = indent ? JsonHelper.class.getSimpleName() + "_INDENT" : JsonHelper.class.getSimpleName();
        return getAndCacheInstance(hyggeName, () -> {
            Class<?> jsonHelperClass = tryToGetClass(getDefaultJacksonJsonHelperPath());
            if (jsonHelperClass == null) {
                throw new UtilRuntimeException("Default \"JsonHelper\" implementation class(" + getDefaultJacksonJsonHelperPath() + ") was not found.");
            }
            if (!JsonHelper.class.isAssignableFrom(jsonHelperClass)) {
                throw new UtilRuntimeException(unexpectedClass("defaultJsonHelper", jsonHelperClass, JsonHelper.class));
            }
            //基于 jackson 默认实现需要先检查依赖
            if (DEFAULT_JACKSON_JSON_HELPER_CLASS_NAME.equals(jsonHelperClass.getName()) && tryToGetClass("com.fasterxml.jackson.databind.ObjectMapper") == null) {
                throw new UtilRuntimeException("Default \"JsonHelper\" implementation class lacks jackson dependencies:com.fasterxml.jackson.databind.ObjectMapper.");
            }
            Properties properties = new Properties();
            properties.put(JsonHelper.ConfigKey.INDENT, indent);
            return (JsonHelper<T>) createInstanceByClass(properties, jsonHelperClass);
        });
    }

    /**
     * 用的返回当前实际的 JsonHelper 默认实现类名称
     */
    private String getDefaultJacksonJsonHelperPath() {
        return CONFIG_KEEPER.getValue(KEY_ACTUAL_DEFAULT_JSON_HELPER).toString();
    }
}
