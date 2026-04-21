package com.ruoyi.common.utils;

import com.ruoyi.common.exception.ServiceException;

import java.math.BigDecimal;

/**
 * Random工具类
 */
public class RandomUtil{

    /**
     * randomBigDecimal
     * @param min 范围最小值
     * @param max 范围最大值
     * @return
     */
    
    public static BigDecimal randomBigDecimal(BigDecimal min, BigDecimal max){
        if (min.compareTo(max) == 0){
            return min;
        }else if (min.compareTo(max) > 0){
            throw new ServiceException("最小值不允许大于最大值");
        }
        return cn.hutool.core.util.RandomUtil.randomBigDecimal(min,max);
    }

    /**
     * randomBigDecimal
     */

    public static BigDecimal randomBigDecimal(){
        return cn.hutool.core.util.RandomUtil.randomBigDecimal();
    }
}
