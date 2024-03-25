/*
 * Copyright 2022-2024 the original author or authors.
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

package hygge.util;

import hygge.commons.exception.UtilRuntimeException;
import hygge.commons.template.definition.Alterego;
import hygge.commons.template.definition.InfoMessageSupplier;
import hygge.util.inner.UtilCreatorConfigurationKeeper;
import hygge.util.inner.UtilCreatorHyggeUtilKeeper;
import hygge.util.definition.HyggeUtil;
import hygge.util.definition.JsonHelper;
import hygge.util.definition.RandomUniqueGenerator;
import hygge.util.definition.UtilCreatorAbility;

import java.util.Properties;
import java.util.function.Supplier;

/**
 * HyggeUtil 示例构造器
 * <p>
 * 写作枚举，本体其实是一个工具实体类。(利用枚举单例特性)
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
@SuppressWarnings("unchecked")
public enum UtilCreator implements UtilCreatorAbility, InfoMessageSupplier, Alterego {
    /**
     * UtilsCreator 单例
     */
    INSTANCE;
    /**
     * UtilsCreator 配置项容器
     */
    public static final UtilCreatorConfigurationKeeper CONFIG_KEEPER = new UtilCreatorConfigurationKeeper();
    /**
     * 用于保存单例工具类的容器，key 是 {@link HyggeUtil#getHyggeName()}
     */
    private static final UtilCreatorHyggeUtilKeeper singletonObjects = new UtilCreatorHyggeUtilKeeper(64, 0.75F);

    /**
     * 先从 {@link UtilCreator#singletonObjects} 里根据 cacheKey 取，如果没取到则通过构造方法获取实例，并将该实例缓存到 {@link UtilCreator#singletonObjects}
     */
    private static <H extends HyggeUtil> H getAndCacheInstance(String cacheKey, Supplier<H> constructor) {
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
            Class<?> defaultClass = UtilCreator.class.getClassLoader().loadClass(className);
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
                className = String.format("hygge.util.impl.%s", "SnowFlakeGenerator");
            }

            if (className == null) {
                className = String.format("hygge.util.impl.Default%s", cacheKey);
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
            if (UtilCreatorConfigurationKeeper.DEFAULT_JACKSON_JSON_HELPER_CLASS_NAME.equals(jsonHelperClass.getName()) && tryToGetClass("com.fasterxml.jackson.databind.ObjectMapper") == null) {
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
        return CONFIG_KEEPER.getValue(UtilCreatorConfigurationKeeper.KEY_ACTUAL_DEFAULT_JSON_HELPER).toString();
    }
}
