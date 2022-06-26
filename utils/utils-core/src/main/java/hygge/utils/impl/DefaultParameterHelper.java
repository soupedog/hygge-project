package hygge.utils.impl;


import hygge.utils.base.BaseParameterHelper;

import java.math.BigDecimal;

/**
 * 默认的 ParameterHelper 实现类
 *
 * @author Xavier
 * @date 2022/6/26
 * @since 1.0
 */
public class DefaultParameterHelper extends BaseParameterHelper {
    @Override
    public String hookString(String resultTemp) {
        return resultTemp;
    }

    @Override
    public Boolean hookBoolean(Boolean resultTemp) {
        return resultTemp;
    }

    @Override
    public Byte hookByte(Byte resultTemp) {
        return resultTemp;
    }

    @Override
    public Short hookShort(Short resultTemp) {
        return resultTemp;
    }

    @Override
    public Integer hookInteger(Integer resultTemp) {
        return resultTemp;
    }

    @Override
    public Long hookLong(Long resultTemp) {
        return resultTemp;
    }

    @Override
    public Float hookFloat(Float resultTemp) {
        return resultTemp;
    }

    @Override
    public Double hookDouble(Double resultTemp) {
        return resultTemp;
    }

    @Override
    public BigDecimal hookBigDecimal(BigDecimal resultTemp) {
        return resultTemp;
    }
}
