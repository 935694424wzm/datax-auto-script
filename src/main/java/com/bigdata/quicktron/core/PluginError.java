package com.bigdata.quicktron.core;

import com.bigdata.quicktron.bean.PluginEnum;
import com.bigdata.quicktron.bean.PluginKey;
import com.bigdata.quicktron.util.PluginUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: PluginError
 * @Description: 判断 传入插件参数是否为不正确，报错
 * @Author: wzm
 * @Date: 2022/8/25 16:40
 * @Version: 1.0
 **/
public class PluginError {

    private static final Logger log= LoggerFactory.getLogger(PluginError.class.getName());

    public static final String REGEXPSIGN="\\\\\\s{0,}--";

    public static final String READER="reader";

    public static final String WRITER="writer";

    public static final String READERAGRS="readerargs";

    public static final String WRITERARGS="writerargs";

    public static final String DATABASETYPE_MYSQL="mysql";

    public static final String DATABASETYPE_HIVE="hive";

    public static final String DATABASETYPE_CLICKHOUSE="clickhouse";




    /**
     * @author wzm
     * @description 对生成datax传入的reader和writer的参数进行验证
     * @date 2022/8/25 16:43
     * @param arg 传入的参数
     * @return map
     */
    public static Map<String,String> argsVerify(String arg){

        Map<String,String> argsMap=new HashMap<>();
        String pluginChannel=null;
//        String pluginFileName=null;

        if (StringUtils.isNotEmpty(arg)){

            String prefix=PluginKey.PREFIX;
            if (StringUtils.contains(arg,prefix+PluginKey.RWSEPARATOR)){ // 判断是否有参数--separator

                arg=arg.trim(); //去除参数首尾两端的空白字符
                String[] split = arg.split(prefix + PluginKey.RWSEPARATOR);
                if (split.length<2 || !split[0].contains(prefix+PluginKey.READERPLUGIN) || !split[1].contains(prefix+PluginKey.WRITERPLUGIN)){ // 判断reader和writer参数是否同时设置
                    log.error("-----切分reader和writer参数出现错误，请检查是否--readerPlugin以及--writerPlugin 参数是否分别在--separator参数的两端-----");
                    return null;
                }else {
                    String readerPluginArgs = split[0];
                    String[] readerSplits = readerPluginArgs.split(REGEXPSIGN);
                    for (String readerSplit : readerSplits) {
                        String readerTrim = readerSplit.trim();
                        if (readerTrim.contains(PluginKey.READERPLUGIN)){
                            String reader = readerTrim.replaceAll("^" + PluginKey.READERPLUGIN, "").trim();
                            PluginEnum pluginEnum = PluginEnum.get(reader);
                            if (pluginEnum==null){
                                throw new NullPointerException("-----获取reader插件名称出现错误，请检查是否按照规范设置-----");
                            }
                            switch (pluginEnum){
                                case MYSQLREADER :
                                    log.info("-----reader是mysql-----");
                                    argsMap.put(READER,DATABASETYPE_MYSQL);
                                    break;
                                case HIVEREADER :
                                    log.info("-----reader是hive-----");
                                    argsMap.put(READER,DATABASETYPE_HIVE);
                                    break;
                                case CLICKHOUSEREADER :
                                    log.info("-----reader是clickhouse-----");
                                    argsMap.put(READER,DATABASETYPE_CLICKHOUSE);
                                    break;
                                default :
                                    log.error("-----readerPlugin参数只能是mysql或者hive或者clickhouse-----");
                                    return null;

                            }
                        }
                        pluginChannel = PluginUtil.getPluginArg(readerSplit, PluginKey.CHANNEL);
//                        pluginFileName = PluginUtil.getPluginArg(readerSplit, PluginKey.FILENAME);


                    }


                    String writerPluginArgs = split[1];
                    String[] writerSplits = writerPluginArgs.split(REGEXPSIGN);
                    for (String writerSplit : writerSplits) {
                        String writerTrim = writerSplit.trim();
                        if (writerTrim.contains(PluginKey.WRITERPLUGIN)){
                            String writer = writerTrim.replaceAll("^" + PluginKey.WRITERPLUGIN, "").trim();
                            PluginEnum pluginEnum = PluginEnum.get(writer);
                            if (pluginEnum==null){
                                throw new NullPointerException("-----获取writer插件名称出现错误，请检查是否按照规范设置-----");
                            }
                            switch (pluginEnum){
                                case MYSQLWRITER :
                                    log.info("-----writer是mysql-----");
                                    argsMap.put(WRITER,DATABASETYPE_MYSQL);
                                    break;
                                case HIVEWRITER :
                                    log.info("-----writer是hive-----");
                                    argsMap.put(WRITER,DATABASETYPE_HIVE);
                                    break;
                                default :
                                    log.error("-----writerPlugin参数只能是mysql或者hive-----");
                                    return null;

                            }
                        }
                        if (StringUtils.isEmpty(pluginChannel)){
                            pluginChannel = PluginUtil.getPluginArg(writerSplit, PluginKey.CHANNEL);

                        }

//                        if (StringUtils.isEmpty(pluginFileName)){
//                            pluginFileName = PluginUtil.getPluginArg(writerSplit, PluginKey.FILENAME);
//                        }

                    }

                    argsMap.put(READERAGRS,readerPluginArgs);

                    argsMap.put(WRITERARGS,writerPluginArgs);

                    argsMap.put(PluginKey.CHANNEL,pluginChannel);
//                    argsMap.put(PluginKey.FILENAME,pluginFileName);
                }


            }else {
                log.error("-----参数--separator未设置，请设置此参数-----");
                return null;
            }

        }
        return argsMap;

    }


    /**
     * @author wzm
     * @description 判断是否满足 需要的插件的必须要设置的参数
     * @date 2022/8/26 14:31
     * @param plugin 插件名称
     * @param args 必须的参数
     * @return void
     */
    public static void getError(String plugin,String... args) {

        StringBuilder sb=new StringBuilder();
        if (plugin.equals(DATABASETYPE_MYSQL+READER)){
         sb.append(PluginKey.IPADDRESS).append(",")
                 .append(PluginKey.PORT).append(",")
                 .append(PluginKey.DATABASE).append(",")
                 .append(PluginKey.USERNAME).append(",")
                 .append(PluginKey.PASSWORD).append(",")
                 .append(PluginKey.QUERYSQL);
        }else if (plugin.equals(DATABASETYPE_MYSQL+WRITER)){
            sb.append(PluginKey.IPADDRESS).append(",")
                    .append(PluginKey.PORT).append(",")
                    .append(PluginKey.DATABASE).append(",")
                    .append(PluginKey.USERNAME).append(",")
                    .append(PluginKey.PASSWORD).append(",")
                    .append(PluginKey.COLUMN).append(",")
                    .append(PluginKey.TABLE);

        }else if (plugin.equals(DATABASETYPE_HIVE+READER)) {
            sb.append(PluginKey.HIVESQL).append(",")
                    .append(PluginKey.DEFAULTFS).append(",")
                    .append(PluginKey.TMPDATABASE).append(",")
                    .append(PluginKey.TMPPATH);

        }else if (plugin.equals(DATABASETYPE_HIVE+WRITER)){
            sb.append(PluginKey.DATABASE).append(",")
                    .append(PluginKey.TABLE).append(",")
                    .append(PluginKey.DEFAULTFS).append(",")
                    .append(PluginKey.TMPDATABASE).append(",")
                    .append(PluginKey.TMPPATH).append(",")
                    .append(PluginKey.COLUMN);

        }

        for (String arg : args) {
            if (StringUtils.isEmpty(arg)){
                throw new NullPointerException("-----请检查"+plugin+"是否配置以下必须的参数"+sb.toString()+"-----");
            }
        }


    }
}
