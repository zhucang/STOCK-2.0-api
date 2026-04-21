package com.ruoyi.common.utils.file;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.utils.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * zip解压、压缩工具
 */
public class ZipUtils {

    /**
     * 压缩多个文件成一个zip文件
     *
     * @param srcFiles：源文件列表
     * @param srcFilesPaths：源文件压缩后的文件路径名称
     * @param destZipFile：压缩后的文件
     */
    public static void toZip(File[] srcFiles, File destZipFile, Map<String,String> srcFilesPaths) {
        byte[] buf = new byte[1024];
        try {
            // ZipOutputStream类：完成文件或文件夹的压缩
            ZipOutputStream out = new ZipOutputStream(new FileOutputStream(destZipFile));
            for (int i = 0; i < srcFiles.length; i++) {
                FileInputStream in = null;
                try {
                    in = new FileInputStream(srcFiles[i]);
                    // 给列表中的文件单独命名
                    String name = srcFiles[i].getName();
                    //如果有自定义文件路径
                    if (srcFilesPaths != null){
                        String path = srcFilesPaths.get(name);
                        if (StringUtils.isNotEmpty(path)){
                            name = path;
                        }
                    }
                    out.putNextEntry(new ZipEntry(name));
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                }catch (Exception e){

                }finally {
                    out.closeEntry();
                    if (in != null){
                        in.close();
                    }
                }
            }
            out.close();
        } catch (Exception e) {
            throw new RuntimeException("压缩文件异常");
        }
    }

    /**
     * 解压文件
     *
     * @param zipFile：需要解压缩的文件
     * @param descDir：解压后的目标目录
     */
    public static void unZipFiles(File zipFile, String descDir) throws IOException {
        File destFile = new File(descDir);
        if (!destFile.exists()) {
            destFile.mkdirs();
        }
        // 解决zip文件中有中文目录或者中文文件
        ZipFile zip = new ZipFile(zipFile, Charset.forName("GBK"));
        for (Enumeration entries = zip.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            InputStream in = zip.getInputStream(entry);
            String curEntryName = entry.getName();
            // 判断文件名路径是否存在文件夹
            int endIndex = curEntryName.lastIndexOf('/');
            // 替换
            String outPath = (descDir + curEntryName).replaceAll("\\*", "/");
            if (endIndex != -1) {
                File file = new File(outPath.substring(0, outPath.lastIndexOf("/")));
                if (!file.exists()) {
                    file.mkdirs();
                }
            }

            // 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
            File outFile = new File(outPath);
            if (outFile.isDirectory()) {
                continue;
            }
            OutputStream out = new FileOutputStream(outPath);
            byte[] buf1 = new byte[1024];
            int len;
            while ((len = in.read(buf1)) > 0) {
                out.write(buf1, 0, len);
            }
            in.close();
            out.close();
        }
    }

    /**
     * MultipartFile 转 File
     * @param multipartFile
     * @return
     */
    public static File multipartFileToFile(MultipartFile multipartFile) {
        File file = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = multipartFile.getInputStream();
            file = new File(RuoYiConfig.getUploadPath() + "/fileTransition/");
            if (!file.exists()){
                file.mkdirs();
            }
            file = new File(RuoYiConfig.getUploadPath() + "/fileTransition/" + multipartFile.getOriginalFilename());
            outputStream = new FileOutputStream(file);
            write(inputStream, outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }


    public static void write(InputStream inputStream, OutputStream outputStream) {
        byte[] buffer = new byte[4096];
        try {
            int count = inputStream.read(buffer, 0, buffer.length);
            while (count != -1) {
                outputStream.write(buffer, 0, count);
                count = inputStream.read(buffer, 0, buffer.length);
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 解析zip
     */
    public static List<ZipEntry> parseZipFileToStreams(MultipartFile multipartFile) throws Exception{
        //file
        File file = multipartFileToFile(multipartFile);
        //zipFile
        ZipFile zip = new ZipFile(file,Charset.forName("GBK"));
        List<ZipEntry> zipEntries = new ArrayList<>();
        for (Enumeration entries = zip.entries(); entries.hasMoreElements();) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            zipEntries.add(entry);
        }
        return zipEntries;
    }
}
