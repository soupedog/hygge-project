package hygge.util.bo;


/**
 * 存储类型单位枚举
 *
 * @author Xavier
 * @date 2024/4/19
 * @since 1.0
 */
public enum StorageUnitEnum {
    /**
     * B
     */
    BYTE(0, "B"),
    /**
     * 1 KB = 1024 B
     */
    KILOBYTE(3, "KB"),
    /**
     * 1 MB = 1024 KB
     */
    MEGABYTE(6, "MB"),
    /**
     * 1 GB = 1024 MB
     */
    GIGABYTE(9, "GB"),
    /**
     * 1 TB = 1024 GB
     */
    TERABYTE(12, "TB"),
    ;
    /**
     * 以字节为单位时的大小数量级
     */
    private final int orderOfMagnitude;
    /**
     * 单位缩写
     */
    private final String abbreviation;

    StorageUnitEnum(int orderOfMagnitude, String abbreviation) {
        this.orderOfMagnitude = orderOfMagnitude;
        this.abbreviation = abbreviation;
    }

    public int getOrderOfMagnitude() {
        return orderOfMagnitude;
    }

    public String getAbbreviation() {
        return abbreviation;
    }
}
