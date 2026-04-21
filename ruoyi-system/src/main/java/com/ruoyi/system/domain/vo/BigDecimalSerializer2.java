package com.ruoyi.system.domain.vo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * BigDecimal序列化
 */
public class BigDecimalSerializer2 extends JsonSerializer<BigDecimal> {


    @Override
    public void serialize(BigDecimal bigDecimal, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (bigDecimal == null) {
            jsonGenerator.writeString("0");
        }else {
            // 保留10位小数
            bigDecimal = bigDecimal.setScale(10,4);
            // stripTrailingZeros去除末尾的0，并且toPlainString不使用科学计数法
            jsonGenerator.writeString(bigDecimal.stripTrailingZeros().toPlainString());
        }
    }
}
