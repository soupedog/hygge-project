package hygge.util.base;

import hygge.commons.exception.UtilRuntimeException;
import hygge.util.bo.StorageUnitEnum;
import hygge.util.definition.UnitConvertHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Matcher;

/**
 * 单位转换工具基类
 *
 * @author Xavier
 * @date 2024/4/19
 * @since 1.0
 */
public abstract class BaseUnitConvertHelper implements UnitConvertHelper {

    @Override
    public int getOrderOfMagnitude(long target) {
        return BigDecimal.valueOf(Math.log10(Math.abs(target))).intValue();
    }

    @Override
    public BigDecimal storageFormat(long byteSize, StorageUnitEnum targetType) {
        if (byteSize == 0L) {
            return BigDecimal.ZERO;
        }

        int scale = 2;

        int orderOfMagnitude = getOrderOfMagnitude(byteSize);

        switch (targetType) {
            case KILOBYTE:
                orderOfMagnitude = orderOfMagnitude - 3;
                if (orderOfMagnitude < -2) {
                    scale = Math.abs(orderOfMagnitude);
                }
                return BigDecimal.valueOf(byteSize).divide(UNIT_RATE_OF_STORAGE, scale, RoundingMode.HALF_UP);
            case MEGABYTE:
                orderOfMagnitude = orderOfMagnitude - 6;
                if (orderOfMagnitude < -2) {
                    scale = Math.abs(orderOfMagnitude);
                }
                return BigDecimal.valueOf(byteSize).divide(UNIT_RATE_OF_STORAGE_SQUARE, scale, RoundingMode.HALF_UP);
            case GIGABYTE:
                orderOfMagnitude = orderOfMagnitude - 9;
                if (orderOfMagnitude < -2) {
                    scale = Math.abs(orderOfMagnitude);
                }
                return BigDecimal.valueOf(byteSize).divide(UNIT_RATE_OF_STORAGE_CUBE, scale, RoundingMode.HALF_UP);
            case TERABYTE:
                orderOfMagnitude = orderOfMagnitude - 12;
                if (orderOfMagnitude < -2) {
                    scale = Math.abs(orderOfMagnitude);
                }
                return BigDecimal.valueOf(byteSize).divide(UNIT_RATE_OF_STORAGE_4TH_POWER, scale, RoundingMode.HALF_UP);
            default:
                return BigDecimal.valueOf(byteSize);
        }
    }

    @Override
    public BigDecimal storageFormat(long byteSize, StorageUnitEnum targetType, int scale) {
        if (byteSize == 0L) {
            return BigDecimal.ZERO;
        }

        switch (targetType) {
            case KILOBYTE:
                return BigDecimal.valueOf(byteSize).divide(UNIT_RATE_OF_STORAGE, scale, RoundingMode.HALF_UP);
            case MEGABYTE:
                return BigDecimal.valueOf(byteSize).divide(UNIT_RATE_OF_STORAGE_SQUARE, scale, RoundingMode.HALF_UP);
            case GIGABYTE:
                return BigDecimal.valueOf(byteSize).divide(UNIT_RATE_OF_STORAGE_CUBE, scale, RoundingMode.HALF_UP);
            case TERABYTE:
                return BigDecimal.valueOf(byteSize).divide(UNIT_RATE_OF_STORAGE_4TH_POWER, scale, RoundingMode.HALF_UP);
            default:
                return BigDecimal.valueOf(byteSize);
        }
    }

    @Override
    public String storageSmartFormatAsString(long byteSize) {
        int orderOfMagnitude = getOrderOfMagnitude(byteSize);

        StorageUnitEnum unit;

        if (orderOfMagnitude >= StorageUnitEnum.TERABYTE.getOrderOfMagnitude()) {
            unit = StorageUnitEnum.TERABYTE;
        } else if (orderOfMagnitude >= StorageUnitEnum.GIGABYTE.getOrderOfMagnitude()) {
            unit = StorageUnitEnum.GIGABYTE;
        } else if (orderOfMagnitude >= StorageUnitEnum.MEGABYTE.getOrderOfMagnitude()) {
            unit = StorageUnitEnum.MEGABYTE;
        } else if (orderOfMagnitude >= StorageUnitEnum.KILOBYTE.getOrderOfMagnitude()) {
            unit = StorageUnitEnum.KILOBYTE;
        } else {
            unit = StorageUnitEnum.BYTE;
        }
        return storageFormat(byteSize, unit).toPlainString() + " " + unit.getAbbreviation();
    }

    @Override
    public long storageParseToBytes(String target) {
        if (target == null || target.isEmpty()) {
            return 0L;
        }

        Matcher matcher = VALUE_PATTERN.matcher(target);
        if (matcher.matches()) {
            try {
                String quantityString = matcher.group(1);

                String unit = matcher.group(3);

                if (unit == null || unit.isEmpty()) {
                    return new BigDecimal(quantityString).longValue();
                } else if (unit.equalsIgnoreCase("K")) {
                    return new BigDecimal(quantityString).multiply(UNIT_RATE_OF_STORAGE).longValue();
                } else if (unit.equalsIgnoreCase("M")) {
                    return new BigDecimal(quantityString).multiply(UNIT_RATE_OF_STORAGE_SQUARE).longValue();
                } else if (unit.equalsIgnoreCase("G")) {
                    return new BigDecimal(quantityString).multiply(UNIT_RATE_OF_STORAGE_CUBE).longValue();
                } else if (unit.equalsIgnoreCase("T")) {
                    return new BigDecimal(quantityString).multiply(UNIT_RATE_OF_STORAGE_4TH_POWER).longValue();
                } else {
                    throw new UtilRuntimeException("Storage units not recognized:" + target);
                }
            } catch (NumberFormatException e) {
                throw new UtilRuntimeException("UnitConvertHelper unable to parse numeric part:" + target, e);
            }
        } else {
            throw new UtilRuntimeException("UnitConvertHelper unable to parse bytes:" + target);
        }
    }
}
