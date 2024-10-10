package hygge.util.impl;

import hygge.commons.exception.ParameterRuntimeException;
import hygge.commons.exception.UtilRuntimeException;
import hygge.util.UtilCreator;
import hygge.util.bo.configuration.arranger.PropertiesArrangerConfiguration;
import hygge.util.bo.configuration.arranger.PropertiesConfigurationFile;
import hygge.util.bo.configuration.arranger.PropertiesConfigurationItem;
import hygge.util.definition.FileHelper;
import hygge.util.template.HyggeJsonUtilContainer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringJoiner;

/**
 * .properties 文件整理工具
 *
 * @author Xavier
 * @date 2024/9/25
 * @since 1.0
 */
public class PropertiesConfigurationArranger extends HyggeJsonUtilContainer {
    private static final FileHelper fileHelper = UtilCreator.INSTANCE.getDefaultInstance(FileHelper.class);

    public void organizePropertiesFiles(String absolutePath, PropertiesArrangerConfiguration configuration) {
        List<File> fileList = fileHelper.getFileByFileExtensionNameFromDirectoryIgnoreDepth(new File(absolutePath), ".properties");

        fileList = collectionHelper.filterNonemptyItemAsArrayList(false, fileList, file -> {
            // 文件在跳过名单中则无需进行整理
            if (configuration.getSkipFileNameList().contains(file.getName())) {
                return null;
            } else {
                return file;
            }
        });

        List<PropertiesConfigurationFile> propertiesConfigurationFileList = readPropertiesFile(fileList);

        for (PropertiesConfigurationFile propertiesConfigurationFile : propertiesConfigurationFileList) {
            // 对配置项元素标记类型
            propertiesConfigurationFile.getConfigurationItemList().forEach(propertiesConfigurationItem ->
                    propertiesConfigurationItem.initType(configuration)
            );

            // 当前文件中，先根据 type 优先级排序，越小越优先；优先级相同再根据 key 字典排序
            propertiesConfigurationFile.getConfigurationItemList().sort((o1, o2) -> {
                if (o1.getType().getOrder() == o2.getType().getOrder()) {
                    return Comparator.<String>naturalOrder().compare(o1.getKey(), o2.getKey());
                } else if (o1.getType().getOrder() < o2.getType().getOrder()) {
                    return -1;
                } else {
                    return 1;
                }
            });
        }

        int previousOrderLevel = -1;
        int currentOrderLevel = -1;

        for (PropertiesConfigurationFile propertiesConfigurationFile : propertiesConfigurationFileList) {
            StringJoiner stringJoiner = new StringJoiner(System.lineSeparator());

            for (PropertiesConfigurationItem propertiesConfigurationItem : propertiesConfigurationFile.getConfigurationItemList()) {
                currentOrderLevel = propertiesConfigurationItem.getType().getOrder();

                if (previousOrderLevel < 0) {
                    // 初次初始化
                    previousOrderLevel = currentOrderLevel;
                }

                // 当前键值优先级和上一对键值优先级有区别，进行空行排版
                if (previousOrderLevel != currentOrderLevel) {
                    stringJoiner.add("");
                }

                // 输出注释
                List<String> commentList = propertiesConfigurationItem.getCommentList();
                if (!propertiesConfigurationItem.getCommentList().isEmpty()) {
                    commentList.forEach(stringJoiner::add);
                }

                // 输出键值对
                if (parameterHelper.isNotEmpty(propertiesConfigurationItem.getKey())) {
                    stringJoiner.add(propertiesConfigurationItem.getKey() + "=" + propertiesConfigurationItem.getValue());
                }

                // 下一轮遍历开始前，上一个遍历元素优先级指示器需要重置
                previousOrderLevel = currentOrderLevel;
            }

            if (!propertiesConfigurationFile.getConfigurationItemList().isEmpty()) {
                // 存在配置项的文件用换行符结尾
                stringJoiner.add("");
            }

            String folderAbsolutePath = propertiesConfigurationFile.getAbsolutePath().substring(0, propertiesConfigurationFile.getAbsolutePath().length() - propertiesConfigurationFile.getFileName().length() - 1);

            fileHelper.saveTextFile(folderAbsolutePath, propertiesConfigurationFile.getFileName(), "", stringJoiner.toString());

            // 处理下一个文件时，上一个遍历元素优先级指示器需要重置
            previousOrderLevel = -1;
        }

        System.out.println(jsonHelperIndent.formatAsString(propertiesConfigurationFileList));
    }

    private List<PropertiesConfigurationFile> readPropertiesFile(List<File> fileList) {
        List<PropertiesConfigurationFile> result = new ArrayList<>();

        if (fileList != null && !fileList.isEmpty()) {
            fileList.forEach(item -> {
                PropertiesConfigurationFile propertiesConfigurationFile = new PropertiesConfigurationFile();
                propertiesConfigurationFile.setAbsolutePath(item.getAbsolutePath());
                propertiesConfigurationFile.setFileName(item.getName());

                List<PropertiesConfigurationItem> propertiesConfigurationItemList = new ArrayList<>();
                propertiesConfigurationFile.setConfigurationItemList(propertiesConfigurationItemList);

                result.add(propertiesConfigurationFile);

                List<String> lineInfo = getLineInfo(item);
                PropertiesConfigurationItem propertiesConfigurationItem = null;

                for (String currentLine : lineInfo) {
                    if (parameterHelper.isEmpty(currentLine)) {
                        // 空白行跳过无需处理
                        continue;
                    }

                    if (propertiesConfigurationItem == null) {
                        propertiesConfigurationItem = new PropertiesConfigurationItem();
                    }

                    boolean isComment = currentLine.startsWith("#");

                    if (isComment) {
                        propertiesConfigurationItem.getCommentList().add(currentLine);
                    } else {
                        parameterHelper.notEmpty((Object) currentLine, item.getName() + " 为非标准 .properties 格式，整理失败。");

                        parameterHelper.stringNotEmpty(currentLine, 3, Integer.MAX_VALUE, item.getName() + " 为非标准 .properties 格式，整理失败。");

                        int indexOfSplit = currentLine.indexOf("=");
                        if (indexOfSplit < 0) {
                            throw new ParameterRuntimeException(item.getName() + " 为非标准 .properties 格式，整理失败。");
                        }

                        String key = currentLine.substring(0, indexOfSplit);
                        String value = currentLine.substring(indexOfSplit + 1);
                        propertiesConfigurationItem.setKey(key);
                        propertiesConfigurationItem.setValue(value);
                        propertiesConfigurationItemList.add(propertiesConfigurationItem);
                        // 添加完一个列表元素需要重置
                        propertiesConfigurationItem = null;
                    }
                }
            });
        }

        return result;
    }

    private List<String> getLineInfo(File file) {
        parameterHelper.objectNotNull("file", file);
        if (!file.exists() || file.isDirectory()) {
            throw new UtilRuntimeException("File(" + file.getAbsolutePath() + ") was not found or it shouldn't be a directory.");
        }
        String lineContent;
        List<String> result = new ArrayList<>();

        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            while ((lineContent = in.readLine()) != null) {
                result.add(lineContent);
            }
        } catch (IOException e) {
            throw new UtilRuntimeException("Fail to read File(" + file.getAbsolutePath() + ").", e);
        }
        return result;
    }
}
