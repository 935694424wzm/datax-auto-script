package com.bigdata.quicktron.util;





import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.*;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @ClassName: PluginUtil
 * @Description: 插件的通用类
 * @Author: wzm
 * @Date: 2022/8/25 16:06
 * @Version: 1.0
 **/
public class PluginUtil {



    private static final Logger log= LoggerFactory.getLogger(PluginUtil.class.getName());

    public static final String linuxTmpPath="/opt/module/datax/localtmp/";



    /**
     * @author wzm
     * @description 生成properties对象类，根据属性获取相应的文件
     * @date 2022/8/25 16:13
     * @param pathName 若是在resources下，直接文件名，其它为  目录/文件名
     * @return java.util.Properties
     */
    public Properties loadPropertiesFile(String pathName) {

        Properties properties = new Properties();
        try {
            properties.load(this.getClass().getClassLoader().getResourceAsStream(pathName));
        } catch (IOException e) {
            log.error("-----加载properties对象类错误-----");
        }


        return properties;

    }



    /**
     * @author wzm
     * @description 获取resource目录下的文件地址
     * @date 2022/8/25 16:29
     * @param filePathName 文件名称
     * @return java.lang.String
     */
    public static String getResourcePath(String filePathName) {
        return ClassLoader.getSystemClassLoader().getResource(filePathName).getPath();
    }


    /**
     * @author wzm
     * @description 获取resource资源下的文件流
     * @date 2022/8/25 16:30
     * @param fileName 文件名称
     * @return java.io.InputStream
     */
    public static InputStream getResourcePathInputStream(String fileName){
        return new PluginUtil().getClass().getResourceAsStream("/"+fileName);
    }


    public static String readFileContent(String fileName) {

        InputStream resourcePathInputStream = getResourcePathInputStream(fileName);
        StringBuilder sb=new StringBuilder();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(resourcePathInputStream));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sb.append(tempStr).append("\n");
            }
            reader.close();
        } catch (FileNotFoundException e) {
            log.error("-----文件不存在----");
        } catch (IOException e) {
            log.error("-----文件读取内容异常-----");
        }


        return sb.toString();
    }




    /**
     * @author wzm
     * @description 写文件
     * @date 2022/8/27 14:41
     * @param fileName  文件名称
     * @param fileContext 文件内容
     * @return void
     */
    public static void writeFileContent(String fileName, String fileContext) {

        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
        file.getParentFile().mkdirs();
        BufferedWriter wsb = null;
        try {
            //获取该文件的缓冲输出流
            wsb = new BufferedWriter(new FileWriter(fileName));
            wsb.write(fileContext);
            wsb.flush();//清空缓存区
            wsb.close();//关闭输出流
        } catch (IOException e) {
            log.error("-----写文件时的打开文件出现错误-----");
        }
    }



    /**
     * @author wzm
     * @description 删除本地文件
     * @date 2022/8/27 14:51
     * @param filenamePath 本地文件路径地址
     * @return void
     */
    public static void deleteLoaclFile(String filenamePath){
        File file = new File(filenamePath);
        if (file.exists()) {
            file.delete();
            log.info("-----删除客户端"+filenamePath+"临时文件成功-----");
        }
    }

    /**
     * @author wzm
     * @description 根据字符串参数，判断取正则前面的值，或者正则后面的值
     * @date 2022/8/25 17:02
     * @param params 字符串参数
     * @param regexp 正则表达式
     * @return java.lang.String
     */
    public static String getRegexContent(String params,String regexp){
        String result="11";
        if (StringUtils.isEmpty(params) && StringUtils.isEmpty(regexp)) return result;

        Matcher matcher = Pattern.compile(regexp).matcher(params);
        if (matcher.find()){
            result=matcher.group(0);

        }

        return result;

    }


    /**
     * @author wzm
     * @description 根据单个参数判断，是否包含某个参数，并返回后面的参数的值
     * @date 2022/8/26 13:44
     * @param argPlugin 参数以及值
     * @param prefix 前缀
     * @return java.lang.String
     */
    public static String getPluginArg(String argPlugin,String prefix){

        if (StringUtils.isNotEmpty(prefix)) {
            String trimSrt = argPlugin.trim();
            if (StringUtils.startsWith(trimSrt, prefix)) {
                String replaceStr = trimSrt.replaceAll("^" + prefix, "").replaceAll("\\\\$","").trim();
                return replaceStr;
            }
        }
        return null;
    }

    public static Boolean getPrefixArg(String argPlugin,String prefix){
        String trimSrt = argPlugin.trim();

        return StringUtils.startsWith(trimSrt,prefix)?true:false;
    }



}
