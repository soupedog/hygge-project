/*
 * Copyright 2022-2023 the original author or authors.
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

import java.io.File;
import java.io.FileFilter;
import java.util.List;

/**
 * 文件处理工具
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public interface FileHelper extends HyggeUtil {
    /**
     * 根据目标文件对象获取其文本内容
     *
     * @param file 目标文件对象
     * @return 目标文件对象的文本内容
     */
    StringBuilder getTextFileContent(File file);

    /**
     * 保存文本文件到指定目录
     *
     * @param absolutePath 文件存放目录
     * @param fileName     文件名
     * @param extension    文件扩展名
     * @param content      文件文本内容
     */
    void saveTextFile(String absolutePath, String fileName, String extension, String content);

    /**
     * 根据路径获取目录 File 对象，如果目录不存在，会直接创建并返回
     */
    File getOrCreateDirectoryIfNotExit(String absolutePath);

    /**
     * 通过文件名称在目录 File 对象子级目录下寻找文件
     *
     * @param directory 目录 File 对象
     * @param fileName  文件目录下子代层级所有非目录 File 对象中首个名称为 fileName 的对象作为查询结果
     * @return File 文件查询结果(可能为空)
     */
    File getFileByFileNameFromDirectory(File directory, String fileName);

    /**
     * 通过文件名称在目录 File 对象子级目录下寻找文件
     *
     * @param directory         目录 File 对象
     * @param fileExtensionName 扩展名限制，多个时用 "," 隔开  例如 ".png,.jpg,.gif"
     * @return File 文件查询结果
     */
    List<File> getFileByFileExtensionNameFromDirectory(File directory, String fileExtensionName);

    /**
     * 通过文件名称在目录 File 对象子级目录下寻找文件
     *
     * @param directory  目录 File 对象
     * @param fileFilter 文件目录下子代层级所有非目录 File 对象均会通过该过滤器验证，返回 true 时添加到查询结果集
     * @return File 文件查询结果
     */
    List<File> getFileFromDirectory(File directory, FileFilter fileFilter);

    /**
     * 在目录 File 对象下的所有后代目录下寻找文件 (递归调用实现，层级过深时请谨慎使用)
     *
     * @param directory 目录 File 对象
     * @param fileName  寻找的目标文件名称
     * @return 查找 File 文件结果
     */
    List<File> getFileByFileNameFromDirectoryIgnoreDepth(File directory, String fileName);

    /**
     * 在目录 File 对象下的所有后代目录下寻找文件 (递归调用实现，层级过深时请谨慎使用)
     *
     * @param directory         目录 File 对象
     * @param fileExtensionName 扩展名限制，多个时用 "," 隔开  例如 ".png,.jpg,.gif"
     * @return 查找 File 文件结果
     */
    List<File> getFileByFileExtensionNameFromDirectoryIgnoreDepth(File directory, String fileExtensionName);

    /**
     * 在目录 File 对象下的所有后代目录下寻找文件 (层级过深时请谨慎使用)
     *
     * @param directory                     目录 File 对象
     * @param fileFilter                    文件目录下所有后代层级非目录 File 对象均会通过该过滤器验证，返回 true 时添加到查询结果集
     * @param getTargetFileFromSubDirectory 文件目录下所有后代层级目录 File 对象均会通过该函数获取子查询结果集，该结果会追加到主查询中
     * @return 查找 File 文件结果
     */
    List<File> getFileFromDirectoryIgnoreDepth(File directory, FileFilter fileFilter, GetTargetFileFromDirectory getTargetFileFromSubDirectory);

    /**
     * 以目录对象为目标执行该函数，要求其返回该目录下的匹配 File 结果集
     */
    @FunctionalInterface
    interface GetTargetFileFromDirectory {
        List<File> apply(File childDirectory);
    }
}
