package com.bigdata.quicktron.core;

import com.bigdata.quicktron.bean.*;
import com.bigdata.quicktron.util.PluginUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Map;

/**
 * @ClassName: PluginJob
 * @Description: 生成插件的job触发类
 * 参数规范："开头 \ 每个参数后跟随空格加\ 最后一个参数不需要 \"
 * @Author: wzm
 * @Date: 2022/8/26 10:51
 * @Version: 1.0
 **/
public class PluginJob {


    private final Logger log = LoggerFactory.getLogger(PluginJob.class.getName());

    private String filename;


    public Configuration configuration = null;
    public FileSystem fs = null;

    public static final String HDFS_DEFAULTFS = "hdfs://001.bg.qkt:8020";

    public static final String HDFSDIR="/user/datax_json/";

    public static final String SUFFIX=".json";


    /**
     * @param
     * @return void
     * @author wzm
     * @description 操作hdfs文件
     * @date 2022/8/26 19:28
     */
    public void operateHdfs(String arg,String filename) {



        //建立与hdfs的链接
        log.info("------开始建立与HDFS的链接-----");
        configuration = new Configuration();
        configuration.set("fs.hdfs.impl", "org.apache.hadoop.hdfs.DistributedFileSystem");
        String serverPathName=HDFSDIR+filename+SUFFIX;
        String clientPathName=PluginUtil.linuxTmpPath+filename+SUFFIX;
        Path path = new Path(serverPathName);
        try {
            fs = FileSystem.get(new URI(HDFS_DEFAULTFS), configuration, "hadoop");

            if(fs.exists(path)){

                log.warn("-----HDFS上已经存在相同的文件-----");
            }else{


                //生成json文件内容
                String json = createJson(arg);

                //写文件到本地路径
                PluginUtil.writeFileContent(clientPathName,json);
//                PluginUtil.writeFileContentStream(clientPathName,json);


                //客户端上传路径
                Path clientPath = new Path(clientPathName);

                //服务器上传路径
                Path serverPath = new Path(serverPathName);

                // 调用文件系统的文件复制方法，第一个参数是否删除原文件true为删除，默认为false
                fs.copyFromLocalFile(false, clientPath, serverPath);




/*                //创建hdfs上的文件
               FSDataOutputStream out = fs.create(path);
                out.writeUTF(json);
                out.flush();//立即将缓冲区的数据输出到接收方
                out.close();*/

                log.info(json);
                log.info("-----datax-json采集文件写入HDFS成功-----"+serverPathName);
            }

            //删除本地文件
            PluginUtil.deleteLoaclFile(clientPathName);



            //关闭HDFS链接
            configuration=null;
            fs.close();
            log.info("-----关闭与HDFS的链接-----");
        } catch (Exception e) {
            log.error("-----与HDFS建立链接出现异常-----"+e);
        }


    }

    /**
     * @param arg
     * @return void
     * @author wzm
     * @description 生成相应的json采集文件
     * @date 2022/8/26 14:07
     */
    public String createJson(String arg) {

        Map<String, String> stringStringMap = PluginError.argsVerify(arg);
        String jobJson = PluginUtil.readFileContent("datax/job.txt");
        String readerJson = null;
        String writerJson = null;


        if (null == stringStringMap) {

            throw new NullPointerException("-----解析参数后，产生空指针错误，请检查配置的相关参数----");
        }


        String channelValue = stringStringMap.get(PluginKey.CHANNEL);
        if (StringUtils.isEmpty(channelValue)) {
            channelValue = "2";
        }
//        filename=stringStringMap.get(PluginKey.FILENAME);
//        if (StringUtils.isEmpty(filename)){
//            throw new NullPointerException("-----未设置生成的json文件名称，异常退出-----");
//        }



        String readerplugin = stringStringMap.get(PluginError.READER) + PluginError.READER;
        //获取readerJson
        readerJson = PluginUtil.readFileContent("datax/" + readerplugin + ".txt");

        String reader = stringStringMap.get(PluginError.READER);
        String[] readerSplit = stringStringMap.get(PluginError.READERAGRS).split(PluginError.REGEXPSIGN);
        if (reader == PluginError.DATABASETYPE_MYSQL) {
            String url = null;
            String port = null;
            String database = null;
            String username = null;
            String password = null;
            String querysql = null;
            String splitpk = null;
            for (String s : readerSplit) {
                if (PluginUtil.getPrefixArg(s, PluginKey.IPADDRESS)) {
                    url = PluginUtil.getPluginArg(s, PluginKey.IPADDRESS);
                } else if (PluginUtil.getPrefixArg(s, PluginKey.PORT)) {
                    port = PluginUtil.getPluginArg(s, PluginKey.PORT);
                } else if (PluginUtil.getPrefixArg(s, PluginKey.DATABASE)) {
                    database = PluginUtil.getPluginArg(s, PluginKey.DATABASE);
                } else if (PluginUtil.getPrefixArg(s, PluginKey.USERNAME)) {
                    username = PluginUtil.getPluginArg(s, PluginKey.USERNAME);
                } else if (PluginUtil.getPrefixArg(s, PluginKey.PASSWORD)) {
                    password = PluginUtil.getPluginArg(s, PluginKey.PASSWORD);
                } else if (PluginUtil.getPrefixArg(s, PluginKey.QUERYSQL)) {
                    querysql = PluginUtil.getPluginArg(s, PluginKey.QUERYSQL);
                } else if (PluginUtil.getPrefixArg(s, PluginKey.SPLITPK)) {
                    splitpk = PluginUtil.getPluginArg(s, PluginKey.SPLITPK);
                }

            }

            MysqlReaderBean mysqlReaderBean = new MysqlReaderBean(readerplugin, url, port, database, username, password, querysql, splitpk); //判断是否符合参数化


            if (StringUtils.isEmpty(port)) {
                port = "3306";
            }
            if (StringUtils.isEmpty(splitpk)) {
                splitpk = "";
            }
            readerJson = readerJson.replace("${ipAddress}", url)
                    .replace("${port}", port)
                    .replace("${dataBase}", database)
                    .replace("${querySql}", "\"" + querysql + "\"")
                    .replace("${passWord}", password)
                    .replace("${userName}", username)
                    .replace("${splitPk}", splitpk);


        } else if (reader == PluginError.DATABASETYPE_HIVE) {
            String hivesql = null;
            String defaultfs = null;
            String tmpdatabase=null;
            String tmpdatabasepath=null;
            String hivesetsql=null;
            for (String s : readerSplit) {
                if (PluginUtil.getPrefixArg(s, PluginKey.HIVESQL)) {
                    hivesql = PluginUtil.getPluginArg(s, PluginKey.HIVESQL);
                } else if (PluginUtil.getPrefixArg(s, PluginKey.DEFAULTFS)) {
                    defaultfs = PluginUtil.getPluginArg(s, PluginKey.DEFAULTFS);
                } else if (PluginUtil.getPrefixArg(s, PluginKey.TMPDATABASE)) {
                    tmpdatabase = PluginUtil.getPluginArg(s, PluginKey.TMPDATABASE);
                } else if (PluginUtil.getPrefixArg(s, PluginKey.TMPPATH)) {
                    tmpdatabasepath = PluginUtil.getPluginArg(s, PluginKey.TMPPATH);
                }else if (PluginUtil.getPrefixArg(s, PluginKey.HIVESETSQL)) {
                    hivesetsql = PluginUtil.getPluginArg(s, PluginKey.HIVESETSQL);
                }
            }
            HiveReaderBean hiveReaderBean = new HiveReaderBean(readerplugin, hivesql, defaultfs,tmpdatabase,tmpdatabasepath,hivesetsql); //判断是否符合参数化


            if (StringUtils.isEmpty(hivesetsql)){
                hivesetsql="";
            }

            readerJson = readerJson.replace("${hiveSql}", "\"" + hivesql + "\"")
                    .replace("${defaultFs}", defaultfs)
                    .replace("${tmpDataBase}", tmpdatabase)
                    .replace("${tmpPath}", tmpdatabasepath)
                    .replace("${hiveSetSql}",hivesetsql);

        }else if (reader == PluginError.DATABASETYPE_CLICKHOUSE){

            String username=null;
            String password=null;
            String querysql=null;
            String url = null;
            String port = null;
            String database = null;

            for (String s : readerSplit) {
                if (PluginUtil.getPrefixArg(s, PluginKey.USERNAME)) {
                    username = PluginUtil.getPluginArg(s, PluginKey.USERNAME);
                } else if (PluginUtil.getPrefixArg(s, PluginKey.PASSWORD)) {
                    password = PluginUtil.getPluginArg(s, PluginKey.PASSWORD);
                } else if (PluginUtil.getPrefixArg(s, PluginKey.QUERYSQL)) {
                    querysql = PluginUtil.getPluginArg(s, PluginKey.QUERYSQL);
                } else if (PluginUtil.getPrefixArg(s, PluginKey.IPADDRESS)) {
                    url = PluginUtil.getPluginArg(s, PluginKey.IPADDRESS);
                } else if (PluginUtil.getPrefixArg(s, PluginKey.PORT)) {
                    port = PluginUtil.getPluginArg(s, PluginKey.PORT);
                } else if (PluginUtil.getPrefixArg(s, PluginKey.DATABASE)) {
                    database = PluginUtil.getPluginArg(s, PluginKey.DATABASE);
                }



            }

            ClickhouseReaderBean clickhouseReaderBean = new ClickhouseReaderBean(readerplugin, url,port,database, username, password, querysql); //判断是否符合参数化

            if (StringUtils.isEmpty(port)){
                port="8123";
            }
            readerJson = readerJson.replace("${userName}", username)
                    .replace("${passWord}", password)
                    .replace("${querySql}", "\"" + querysql + "\"")
                    .replace("${ipAddress}", url)
                    .replace("${port}", port)
                    .replace("${dataBase}",database);

        }


        ////////////////////////////////////////////////////////////////////////////////////////////////////////分隔符

        String writerplugin = stringStringMap.get(PluginError.WRITER) + PluginError.WRITER;
        //获取writerJson
        writerJson = PluginUtil.readFileContent("datax/" + writerplugin + ".txt");
        String writer = stringStringMap.get(PluginError.WRITER);
        String[] writerSplit = stringStringMap.get(PluginError.WRITERARGS).split(PluginError.REGEXPSIGN);

        if (writer == PluginError.DATABASETYPE_MYSQL) {
            String url = null;
            String port = null;
            String database = null;
            String username = null;
            String password = null;
            String column = null;
            String table = null;
            String presql = null;
            String writerMode = null;
            for (String s : writerSplit) {
                if (PluginUtil.getPrefixArg(s, PluginKey.IPADDRESS)) {
                    url = PluginUtil.getPluginArg(s, PluginKey.IPADDRESS);
                } else if (PluginUtil.getPrefixArg(s, PluginKey.PORT)) {
                    port = PluginUtil.getPluginArg(s, PluginKey.PORT);
                } else if (PluginUtil.getPrefixArg(s, PluginKey.DATABASE)) {
                    database = PluginUtil.getPluginArg(s, PluginKey.DATABASE);
                } else if (PluginUtil.getPrefixArg(s, PluginKey.USERNAME)) {
                    username = PluginUtil.getPluginArg(s, PluginKey.USERNAME);
                } else if (PluginUtil.getPrefixArg(s, PluginKey.PASSWORD)) {
                    password = PluginUtil.getPluginArg(s, PluginKey.PASSWORD);
                } else if (PluginUtil.getPrefixArg(s, PluginKey.COLUMN)) {
                    column = PluginUtil.getPluginArg(s, PluginKey.COLUMN);
                } else if (PluginUtil.getPrefixArg(s, PluginKey.TABLE)) {
                    table = PluginUtil.getPluginArg(s, PluginKey.TABLE);
                } else if (PluginUtil.getPrefixArg(s, PluginKey.PRESQL)) {
                    presql = PluginUtil.getPluginArg(s, PluginKey.PRESQL);
                } else if (PluginUtil.getPrefixArg(s, PluginKey.WRITEMODE)) {
                    writerMode = PluginUtil.getPluginArg(s, PluginKey.WRITEMODE);
                }

            }
            MysqlWriterBean mysqlWriterBean = new MysqlWriterBean(writerplugin, url, port, database, username, password, column, table, presql, writerMode); //判断是否符合参数化

            if (StringUtils.isEmpty(port)) {
                port = "3306";
            }
            if (StringUtils.isEmpty(presql)) {
                presql = "";
            }
            if (StringUtils.isEmpty(writerMode)) {
                writerMode = "insert";
            }

            column = "\"" + column.replace(",", "\",\"") + "\"";

            writerJson = writerJson.replace("${column}", column)
                    .replace("${ipAddress}", url)
                    .replace("${port}", port)
                    .replace("${dataBase}", database)
                    .replace("${table}", "\"" + table + "\"")
                    .replace("${preSql}", "\"" + presql + "\"")
                    .replace("${passWord}", password)
                    .replace("${userName}", username)
                    .replace("${writeMode}", writerMode);


        } else if (writer == PluginError.DATABASETYPE_HIVE) {
            String database = null;
            String table = null;
            String defaultfs = null;
            String writemode = null;
            String partition = null;
            String tmpdatabase = null;
            String tmpdatabasepath = null;
            String presql = null;
            String column = null;
            String hivesetsql=null;
            for (String s : writerSplit) {
                if (PluginUtil.getPrefixArg(s, PluginKey.DATABASE)) {
                    database = PluginUtil.getPluginArg(s, PluginKey.DATABASE);
                } else if (PluginUtil.getPrefixArg(s, PluginKey.TABLE)) {
                    table = PluginUtil.getPluginArg(s, PluginKey.TABLE);
                } else if (PluginUtil.getPrefixArg(s, PluginKey.DEFAULTFS)) {
                    defaultfs = PluginUtil.getPluginArg(s, PluginKey.DEFAULTFS);
                } else if (PluginUtil.getPrefixArg(s, PluginKey.WRITEMODE)) {
                    writemode = PluginUtil.getPluginArg(s, PluginKey.WRITEMODE);
                } else if (PluginUtil.getPrefixArg(s, PluginKey.PARTITION)) {
                    partition = PluginUtil.getPluginArg(s, PluginKey.PARTITION);
                } else if (PluginUtil.getPrefixArg(s, PluginKey.TMPDATABASE)) {
                    tmpdatabase = PluginUtil.getPluginArg(s, PluginKey.TMPDATABASE);
                } else if (PluginUtil.getPrefixArg(s, PluginKey.TMPPATH)) {
                    tmpdatabasepath = PluginUtil.getPluginArg(s, PluginKey.TMPPATH);
                } else if (PluginUtil.getPrefixArg(s, PluginKey.COLUMN)) {
                    column = PluginUtil.getPluginArg(s, PluginKey.COLUMN);
                } else if (PluginUtil.getPrefixArg(s, PluginKey.PRESQL)) {
                    presql = PluginUtil.getPluginArg(s, PluginKey.PRESQL);
                }else if (PluginUtil.getPrefixArg(s, PluginKey.HIVESETSQL)) {
                    hivesetsql = PluginUtil.getPluginArg(s, PluginKey.HIVESETSQL);
                }
            }


            HiveWriterBean hiveWriterBean = new HiveWriterBean(writerplugin, database, table, defaultfs, writemode, partition, tmpdatabase, tmpdatabasepath, column, presql,hivesetsql);//判断是否符合参数化


            if (StringUtils.isEmpty(presql)) {
                presql = "select 1";
            }
            if (StringUtils.isEmpty(writemode)) {
                writemode = "insert";

            }
            if (StringUtils.isEmpty(tmpdatabase)) {
                tmpdatabase = database;
            }
            if (StringUtils.isEmpty(partition)) {

                partition = "";
            }
            if (StringUtils.isEmpty(hivesetsql)){
                hivesetsql="";
            }


            StringBuilder columnSb = new StringBuilder();
            String[] columnSplit = column.split(",");
            for (String s : columnSplit) {
                columnSb.append("{\"name\":\"").append(StringUtils.trim(s)).append("\",")
                        .append("\"type\":\"String\"},");
            }
            column = columnSb.toString().replaceAll(",$", "");

            writerJson = writerJson.replace("${dataBase}", database)
                    .replace("${table}", table)
                    .replace("${defaultFs}", defaultfs)
                    .replace("${hiveSetSql}",hivesetsql)
                    .replace("${writeMode}", writemode)
                    .replace("${preSql}", presql)
                    .replace("${tmpDataBase}", tmpdatabase)
                    .replace("${tmpPath}", tmpdatabasepath)
                    .replace("${partition}", "\"" + partition + "\"")
                    .replace("${column}", column);


        }


        log.info("-----json相关reader,writer整合完成-----");
        return jobJson.replace("arg2", channelValue).replace("args0", readerJson).replace("args1", writerJson);

    }



    public static void main(String[] args)  {
//        String s1 ="\\--readerPlugin clickhousereader \\  " +
//                "--userName select * from dwd.dwd_dtk_pk \\" +
//                "--passWord hdfs://aldnasdn \\" +
//                "--querySql select 8 frm,snd,s \\" +
//                "--ipAddress 005.bg.qkt \\" +
//                "--dataBase dwd \\" +
//                "--separator \\" +
//                "--writerPlugin hivewriter \\" +
//                "--dataBase ads \\" +
//                "--table emp \\" +
//                "--defaultFs hdfs \\" +
//                "--tmpDataBase tmp \\" +
//                "--tmpPath /user/tmp/pp \\" +
//                "--column id,name,age,address \\" +
//                "--channel 9 \\" +
//                "--dataxfilename test1";

        PluginJob pluginJob = new PluginJob();
//        System.out.println(pluginJob.createJson(args[0]));
        if (args.length != 2 || StringUtils.isEmpty(args[0]) || StringUtils.isEmpty(args[1])) {
            throw new NullPointerException("-----异常退出，传入参数必须为两个，第一个参数为json参数，第二个参数为json文件名称-----");
        }
        pluginJob.operateHdfs(args[0],args[1]);


    }
}
