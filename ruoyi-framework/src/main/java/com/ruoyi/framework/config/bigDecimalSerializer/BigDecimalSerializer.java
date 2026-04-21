package com.ruoyi.framework.config.bigDecimalSerializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.system.service.IOtherValueService;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * BigDecimal序列化
 */
public class BigDecimalSerializer extends JsonSerializer<BigDecimal> {


    private IOtherValueService otherValueService = SpringUtils.getBean(IOtherValueService.class);

    @Override
    public void serialize(BigDecimal bigDecimal, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (bigDecimal == null) {
            jsonGenerator.writeString("0");
        }else {
            //保留小数点
            Integer bigDecimalScale = otherValueService.getOtherValueByKey("bigDecimal_scale", Integer.class);
            bigDecimalScale = bigDecimalScale != null ? bigDecimalScale : 10;
            // 保留10位小数
            bigDecimal = bigDecimal.setScale(bigDecimalScale,4);
            // stripTrailingZeros去除末尾的0，并且toPlainString不使用科学计数法
            jsonGenerator.writeString(bigDecimal.stripTrailingZeros().toPlainString());
        }
    }
}
