package com.ruoyi.common.utils.cmd;

import com.ruoyi.common.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * bat运行工具类
 */
public class RunBatUtils {

    private static final Logger log = LoggerFactory.getLogger(RunBatUtils.class);

    /**
     * 运行bat文件
     * @param locationCmd bat文件路径
     */
    public static void  callCmd(String locationCmd){
        StringBuilder sb = new StringBuilder();
        try {
            Process child = Runtime.getRuntime().exec(locationCmd);
            InputStream in = child.getInputStream();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(in));
            String line;
            while((line=bufferedReader.readLine())!=null)
            {
                sb.append(line + "\n");
            }
            in.close();
            try {
                child.waitFor();
            } catch (InterruptedException e) {
                System.out.println(e);
            }
            System.out.println("callCmd execute finished");
        } catch (Exception e) {
            throw new ServiceException(e.getStackTrace().toString(),true);
        }
    }
}
