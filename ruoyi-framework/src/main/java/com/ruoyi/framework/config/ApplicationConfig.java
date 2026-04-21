package com.ruoyi.framework.config;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.config.bigDecimalSerializer.BigDecimalSerializer;
import com.ruoyi.system.service.IOtherValueService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.math.BigDecimal;
import java.util.TimeZone;

/**
 * 程序注解配置
 *
 * @author ruoyi
 */
@Configuration
// 表示通过aop框架暴露该代理对象,AopContext能够访问
@EnableAspectJAutoProxy(exposeProxy = true)
// 指定要扫描的Mapper类的包的路径
@MapperScan("com.ruoyi.**.mapper")
public class ApplicationConfig
{

    @Autowired
    private IOtherValueService otherValueService;

//    @Bean
//    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization()
//    {
//        try{
//            //默认时区
//            TimeZone timeZone = TimeZone.getDefault();
//            //获取设置的时区
//            String jacksonTimeZone = otherValueService.getOtherValueByKey("jackson_time_zone",String.class);
//            if (StringUtils.isNotEmpty(jacksonTimeZone)){
//                TimeZone zone = TimeZone.getTimeZone(jacksonTimeZone);
//                if (zone.getID().equals(jacksonTimeZone)){
//                    timeZone = zone;
//                }
//            }
//            TimeZone defaultTimeZone = timeZone;
//            return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.timeZone(defaultTimeZone);
//        }catch (Exception e){
//            return jacksonObjectMapperBuilder -> jacksonObjectMapperBuilder.timeZone(TimeZone.getDefault());
//        }
//    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization()
    {
        return new Jackson2ObjectMapperBuilderCustomizer(){
            @Override
            public void customize(Jackson2ObjectMapperBuilder builder) {
                //bigdecimal序列化
                SimpleModule module = new SimpleModule();
                module.addSerializer(BigDecimal.class,new BigDecimalSerializer());
                builder.modules(module);
                //设置展示时区
                try{
                    //默认时区
                    TimeZone timeZone = TimeZone.getDefault();
                    //获取设置的时区
                    String jacksonTimeZone = otherValueService.getOtherValueByKey("jackson_time_zone",String.class);
                    if (StringUtils.isNotEmpty(jacksonTimeZone)){
                        TimeZone zone = TimeZone.getTimeZone(jacksonTimeZone);
                        if (zone.getID().equals(jacksonTimeZone)){
                            timeZone = zone;
                        }
                    }
                    TimeZone defaultTimeZone = timeZone;
                    builder.timeZone(defaultTimeZone);
                }catch (Exception e){
                    builder.timeZone(TimeZone.getDefault());
                }
            }
        };
    }
}
