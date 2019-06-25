package com.mornsnow.common.util.base;

import java.io.*;
import java.nio.file.Paths;

public class FileUtil {

    /**
     * <strong>描述：获取资源文件夹下文件（避免打包后找不到路径）</strong><br>
     * 
     * <pre>
     * packagePath: /BOOT-INF/classes/template/email.htm
     * resourcePath: /template/email.htm
     * </pre>
     * 
     * @param packagePath jar包内路径
     * @param resourcePath resource文件下路径
     * @author user 2017年2月22日 下午4:36:59
     */
    public static InputStream getReSourcesFileStream(String packagePath, String resourcePath) {
        InputStream resource = Object.class.getResourceAsStream(packagePath);
        if (resource == null) {
            resource = Object.class.getResourceAsStream(resourcePath);
        }
        return resource;
    }

    /** 获取一个可用临时路径 */
    public static String getSystemPath() {
        return Paths.get(System.getProperty("user.dir"), "tmp").toString();
    }

    /** 获取一个可用临时路径 */
    public static String getSystemPath(String path) {
        return Paths.get(System.getProperty("user.dir"), "tmp", path).toString();
    }

    /**
     * <strong>描述：获取资源文件夹下文件（避免打包后找不到路径）</strong><br>
     * 
     * <pre>
     * packagePath: /BOOT-INF/classes/template/email.htm
     * resourcePath: /template/email.htm
     * </pre>
     * 
     * @param packagePath jar包内路径
     * @param resourcePath resource文件下路径
     * @author user 2017年2月22日 下午4:36:59
     * @throws IOException
     */
    public static String getReSourcesFileString(String packagePath, String resourcePath) throws IOException {
        InputStream reSourcesFileStream = getReSourcesFileStream(packagePath, resourcePath);
        return getFileString(reSourcesFileStream);
    }

    public static String getFileString(File file) throws IOException {
        StringBuilder result = new StringBuilder();
        BufferedReader br = new BufferedReader(new FileReader(file));// 构造一个BufferedReader类来读取文件
        String s = null;
        while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
            result.append(System.lineSeparator() + s);
        }
        br.close();
        return result.toString();
    }

    public static String getFileString(InputStream input) throws IOException {
        StringBuilder result = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(input));// 构造一个BufferedReader类来读取文件
        String s = null;
        while ((s = br.readLine()) != null) {// 使用readLine方法，一次读一行
            result.append(System.lineSeparator() + s);
        }
        br.close();
        return result.toString();
    }
}
