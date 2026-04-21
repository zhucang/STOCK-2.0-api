package com.ruoyi.common.utils;


import java.util.Random;

/**
 * CodeUtils
 */
public class CodeUtils {

    /**
     * 生成订单号
     * @return
     */
    public static String generateOrderCode(String prefix) {
        Random random = new Random();
        Integer number = random.nextInt(900) + 100;
        return (StringUtils.isNotEmpty(prefix)?prefix:"") + System.currentTimeMillis() + number;
    }

    /**
     * 生成邀请码
     * @return
     */
    public static String generateInviteCode(Integer num) {
        //默认7位数
        if (num != null){
            num = num - 1;
        }else {
            num = 6;
        }
        //结果
        String result = "";
        Random random = new Random();
        for (int i = 0; i < num; i++) {
            result = result + random.nextInt(10);
        }
        return result;
    }

}
