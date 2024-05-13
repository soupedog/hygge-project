package hygge.util.definition;

import hygge.util.bo.StorageUnitEnum;

import java.math.BigDecimal;
import java.util.regex.Pattern;

/**
 * 单位转换工具
 *
 * @author Xavier
 * @date 2024/4/19
 * @since 1.0
 */
public interface UnitConvertHelper extends HyggeUtil {
    Pattern VALUE_PATTERN = Pattern.compile("([0-9]+([.][0-9]+)?)\\s*(|K|M|G|T)B?", Pattern.CASE_INSENSITIVE);
    BigDecimal UNIT_RATE_OF_STORAGE = new BigDecimal(1024);
    BigDecimal UNIT_RATE_OF_STORAGE_SQUARE = new BigDecimal(1048576);
    BigDecimal UNIT_RATE_OF_STORAGE_CUBE = new BigDecimal(1073741824);
    BigDecimal UNIT_RATE_OF_STORAGE_4TH_POWER = new BigDecimal(1099511627776L);

    /**
     * 获取目标数的数量级
     * e.g:<br/>
     * 100 = 2<br/>
     * 10 = 1<br/>
     * 0 = 0<br/>
     * 0.1 = -1<br/>
     * 0.01 = -2
     */
    int getOrderOfMagnitude(long target);

    /**
     * 将字节数为单位的存储大小转换成目标存储单位(自动识别精度，有保留 2 位小数的倾向)
     *
     * @param byteSize   存储大小字节数
     * @param targetType 希望转换的目标存储单位
     */
    BigDecimal storageFormat(long byteSize, StorageUnitEnum targetType);

    /**
     * 将字节数为单位的存储大小转换成目标存储单位
     *
     * @param byteSize   存储大小字节数
     * @param targetType 希望转换的目标存储单位
     * @param scale      返结果的小数精度
     */
    BigDecimal storageFormat(long byteSize, StorageUnitEnum targetType, int scale);

    /**
     * 智能将字节数存储单位转换成人便于理解的文本形式
     */
    String storageSmartFormatAsString(long byteSize);

    /**
     * 等同于 {@link UnitConvertHelper#storageSmartFormatAsString(long)} 的逆函数，将人便于理解的文本转化为字节数为单位的存储大小
     */
    long storageParseToBytes(String target);
}
