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

package hygge.util.definition;

import hygge.commons.exception.UtilRuntimeException;
import hygge.util.UtilCreator;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;

/**
 * UtilsCreator 需要具备的能力
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public interface UtilCreatorAbility {
    /**
     * 创建一个 HyggeUtil 实例
     */
    <T extends HyggeUtil> T createHelper(Class<T> target);

    /**
     * 创建一个 HyggeUtil 实例
     *
     * @param className 类名<br/> e.g {@code hygge.util.impl.DefaultFileHelper}
     */
    <T extends HyggeUtil> T createHelper(String className);

    /**
     * 获取目标工具的默认单例实现
     * <p/>
     * 会通过 "hygge.util.impl.Default" + {@link HyggeUtil#getHyggeName()} 来确定默认实现类的类名，并尝试通过反射构造实例
     *
     * @param target {@link UtilCreator} 要求入参必须下列任意其一：<br/>
     *               {@link CollectionHelper}<br/>
     *               {@link DaoHelper}<br/>
     *               {@link FileHelper}<br/>
     *               {@link JsonHelper} 默认是不开启缩进的<br/>
     *               {@link ParameterHelper}<br/>
     *               {@link RandomHelper}<br/>
     *               {@link RandomUniqueGenerator}<br/>
     *               {@link TimeHelper}<br/>
     */
    <T extends HyggeUtil> T getDefaultInstance(Class<T> target);


    /**
     * 获取一个 Json 默认实现的单例
     *
     * @param indent 是否开启缩进
     */
    <T> JsonHelper<T> getDefaultJsonHelperInstance(boolean indent);

    /**
     * 创建用户自定义的类实例
     *
     * @param customClass 自定义类
     * @return 自定义类实例
     */
    default <C> C createInstanceByClass(Class<C> customClass) {
        if (customClass == null) {
            throw new UtilRuntimeException("Create target can't be null.");
        }
        try {
            return customClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new UtilRuntimeException("Fail to get instance of " + customClass.getName() + ".", e);
        }
    }

    /**
     * 创建用户自定义的类实例
     *
     * @param properties  必要的构造配置项
     * @param customClass 自定义类
     * @return 自定义类实例
     */
    default <C> C createInstanceByClass(Properties properties, Class<C> customClass) {
        if (customClass == null) {
            throw new UtilRuntimeException("Create target can't be null.");
        }
        try {
            Constructor<C> constructor = customClass.getConstructor(Properties.class);
            return constructor.newInstance(properties);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new UtilRuntimeException("Fail to get instance of " + customClass.getName() + ".", e);
        }
    }

    default Class<?> tryToGetClass(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
