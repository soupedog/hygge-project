package hygge.utils.base;


import hygge.commons.exception.UtilRuntimeException;
import hygge.utils.UtilsCreator;
import hygge.utils.definitions.FileHelper;
import hygge.utils.definitions.ParameterHelper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import static hygge.commons.constant.ConstantParameters.LINE_SEPARATOR;

/**
 * FileHelper 处理工具类基类
 *
 * @author Xavier
 * @date 2022/7/11
 * @since 1.0
 */
public abstract class BaseFileHelper implements FileHelper {
    protected static final ParameterHelper PROPERTIES_HELPER = UtilsCreator.INSTANCE.getDefaultInstance(ParameterHelper.class);
    protected static final ConcurrentHashMap<String, FileFilter> FILE_NAME_FILTER_CACHE = new ConcurrentHashMap<>();
    protected static final ConcurrentHashMap<String, FileFilter> FILE_EXTENSION_NAME_FILTER_CACHE = new ConcurrentHashMap<>();
    protected static final ReentrantLock FILE_FILTER_CREATE_LOCK = new ReentrantLock();

    @Override
    public StringBuilder getTextFileContent(File file) {
        PROPERTIES_HELPER.objectNotNull("file", file);
        if (!file.exists() || file.isDirectory()) {
            throw new UtilRuntimeException("File(" + file.getAbsolutePath() + ") was not found or it shouldn't be a directory.");
        }
        String lineContent;
        StringBuilder result = new StringBuilder();

        try (BufferedReader in = new BufferedReader(new FileReader(file))) {
            while ((lineContent = in.readLine()) != null) {
                result.append(lineContent);
                result.append(LINE_SEPARATOR);
            }
        } catch (IOException e) {
            throw new UtilRuntimeException("Fail to read File(" + file.getAbsolutePath() + ").", e);
        }

        if (result.length() > 0) {
            PROPERTIES_HELPER.removeStringFormTail(result, LINE_SEPARATOR, 1);
        }
        return result;
    }

    @Override
    public void saveTextFile(String absolutePath, String fileName, String extension, String content) {
        getOrCreateDirectoryIfNotExit(absolutePath);

        String finalPath = absolutePath + File.separator + fileName + extension;

        try (FileWriter fileWriter = new FileWriter(finalPath);
             BufferedWriter bw = new BufferedWriter(fileWriter)) {
            // 写入内容
            bw.write(content);
            bw.flush();
        } catch (IOException e) {
            throw new UtilRuntimeException(String.format("Fail to persistent file(%s)", fileName + ".java"), e);
        }
    }

    @Override
    public File getOrCreateDirectoryIfNotExit(String absolutePath) {
        PROPERTIES_HELPER.stringNotEmpty("absolutePath", (Object) absolutePath);
        File result = new File(absolutePath);
        if (result.exists() && !result.isDirectory()) {
            throw new UtilRuntimeException("[absolutePath] should be a path of directory.");
        }
        if (!result.exists() && Boolean.FALSE.equals(result.mkdirs())) {
            throw new UtilRuntimeException("Fail to create directory:" + absolutePath);
        }
        return result;
    }

    @Override
    public File getFileByFileNameFromDirectory(File directory, String fileName) {
        PROPERTIES_HELPER.objectNotNull("directory", directory);
        PROPERTIES_HELPER.stringNotEmpty("fileName", (Object) fileName);

        if (directory.isDirectory()) {
            return null;
        }

        FileFilter fileFilter = createFileNameFilterIfAbsent(fileName);
        File[] resultTemp = directory.listFiles(fileFilter);
        if (resultTemp != null && resultTemp.length > 0) {
            return resultTemp[0];
        }
        return null;
    }

    @Override
    public List<File> getFileByFileExtensionNameFromDirectory(File directory, String fileExtensionName) {
        PROPERTIES_HELPER.objectNotNull("directory", directory);
        PROPERTIES_HELPER.stringNotEmpty("fileExtensionName", (Object) fileExtensionName);
        List<File> result = new ArrayList<>();

        if (directory.exists() && directory.isDirectory()) {
            FileFilter fileFilter = createFileExtensionNameFilterIfAbsent(fileExtensionName);
            searchInCurrentDirectoryByFilter(directory, result, fileFilter);
        }
        return result;
    }

    @Override
    public List<File> getFileFromDirectory(File directory, FileFilter fileFilter) {
        PROPERTIES_HELPER.objectNotNull("directory", directory);
        PROPERTIES_HELPER.stringNotEmpty("fileFilter", fileFilter);

        List<File> result = new ArrayList<>();
        if (directory.exists() && directory.isDirectory()) {
            searchInCurrentDirectoryByFilter(directory, result, fileFilter);
        }
        return result;
    }

    @Override
    public List<File> getFileByFileNameFromDirectoryIgnoreDepth(File directory, String fileName) {
        PROPERTIES_HELPER.objectNotNull("directory", directory);
        PROPERTIES_HELPER.stringNotEmpty("fileName", (Object) fileName);

        FileFilter fileFilter = createFileNameFilterIfAbsent(fileName);

        GetTargetFileFromDirectory function = (subDirectory) -> getFileByFileNameFromDirectoryIgnoreDepth(subDirectory, fileName);

        return getFileFromDirectoryIgnoreDepth(directory, fileFilter, function);
    }

    @Override
    public List<File> getFileByFileExtensionNameFromDirectoryIgnoreDepth(File directory, String fileExtensionName) {
        PROPERTIES_HELPER.objectNotNull("directory", directory);
        PROPERTIES_HELPER.stringNotEmpty("fileExtensionName", (Object) fileExtensionName);

        FileFilter fileFilter = createFileExtensionNameFilterIfAbsent(fileExtensionName);

        GetTargetFileFromDirectory function = (subDirectory) -> getFileByFileExtensionNameFromDirectoryIgnoreDepth(subDirectory, fileExtensionName);

        return getFileFromDirectoryIgnoreDepth(directory, fileFilter, function);
    }

    @Override
    public List<File> getFileFromDirectoryIgnoreDepth(File directory, FileFilter fileFilter, GetTargetFileFromDirectory getTargetFileFromSubDirectory) {
        PROPERTIES_HELPER.objectNotNull("directory", directory);
        PROPERTIES_HELPER.stringNotEmpty("fileFilter", fileFilter);
        List<File> result = new ArrayList<>();

        File[] resultTemp = directory.listFiles();
        if (resultTemp == null || resultTemp.length < 1) {
            return result;
        }

        for (File item : resultTemp) {
            if (item.isDirectory()) {
                List<File> subResult = getTargetFileFromSubDirectory.apply(item);
                if (subResult != null && !subResult.isEmpty()) {
                    result.addAll(subResult);
                }
            } else {
                if (fileFilter.accept(item)) {
                    result.add(item);
                }
            }
        }
        return result;
    }

    protected FileFilter createFileNameFilterIfAbsent(String fileName) {
        FileFilter fileFilter = FILE_NAME_FILTER_CACHE.get(fileName);
        if (fileFilter == null) {
            FILE_FILTER_CREATE_LOCK.lock();
            try {
                if (FILE_NAME_FILTER_CACHE.get(fileName) == null) {
                    fileFilter = fileItem -> fileItem != null && !fileItem.isDirectory() && fileName.equals(fileItem.getName());
                    FILE_NAME_FILTER_CACHE.put(fileName, fileFilter);
                } else {
                    fileFilter = FILE_NAME_FILTER_CACHE.get(fileName);
                }
            } finally {
                FILE_FILTER_CREATE_LOCK.unlock();
            }
        }
        return fileFilter;
    }

    protected FileFilter createFileExtensionNameFilterIfAbsent(String fileExtensionName) {
        FileFilter fileFilter = FILE_EXTENSION_NAME_FILTER_CACHE.get(fileExtensionName);
        if (fileFilter == null) {
            FILE_FILTER_CREATE_LOCK.lock();
            try {
                if (FILE_EXTENSION_NAME_FILTER_CACHE.get(fileExtensionName) == null) {
                    fileFilter = createFileExtensionFilter(fileExtensionName);
                    FILE_EXTENSION_NAME_FILTER_CACHE.put(fileExtensionName, fileFilter);
                } else {
                    fileFilter = FILE_EXTENSION_NAME_FILTER_CACHE.get(fileExtensionName);
                }
            } finally {
                FILE_FILTER_CREATE_LOCK.unlock();
            }
        }
        return fileFilter;
    }

    protected FileFilter createFileExtensionFilter(String fileExtensionName) {
        return new FileFilter() {
            private final String[] extensionArray = fileExtensionName.split(",");

            @Override
            public boolean accept(File fileItem) {
                if (fileItem == null || fileItem.isDirectory()) {
                    return false;
                }

                String extensionValue = fileItem.getName();
                for (String extension : extensionArray) {
                    if (extensionValue.endsWith(extension)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    protected void searchInCurrentDirectoryByFilter(File directory, List<File> result, FileFilter fileFilter) {
        File[] resultTemp = directory.listFiles(item -> !item.isDirectory());
        if (resultTemp != null && resultTemp.length > 0) {
            for (File item : resultTemp) {
                if (item != null && fileFilter.accept(item)) {
                    result.add(item);
                }
            }
        }
    }
}
