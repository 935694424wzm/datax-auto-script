package com.bigdata.quicktron.bean;

import com.bigdata.quicktron.core.PluginError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: MysqlReaderBean
 * @Description: mysql的Reader Bean 类
 * @Author: wzm
 * @Date: 2022/8/26 10:59
 * @Version: 1.0
 **/
public class MysqlReaderBean {


    private final Logger log= LoggerFactory.getLogger(MysqlReaderBean.class.getName());

    private String MYSQLREADER;

    private String url;

    private String port;

    private String database;

    private String username;

    private String password;

    private String querySql;



    private String splitpk;

    public MysqlReaderBean() {

    }

    public MysqlReaderBean(String MYSQLREADER, String url, String port, String database, String username, String password, String querySql, String splitpk) {
        this.MYSQLREADER = MYSQLREADER;
        this.url = url;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.querySql = querySql;
        this.splitpk = splitpk;


        PluginError.getError(PluginError.DATABASETYPE_MYSQL+PluginError.READER,url,database,username,password,querySql);
    }



    public String getMYSQLREADER() {
        return MYSQLREADER;
    }

    public void setMYSQLREADER(String MYSQLREADER) {
        this.MYSQLREADER = MYSQLREADER;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getQuerySql() {
        return querySql;
    }

    public void setQuerySql(String querySql) {
        this.querySql = querySql;
    }

    public String getSplitpk() {
        return splitpk;
    }

    public void setSplitpk(String splitpk) {
        this.splitpk = splitpk;
    }


    @Override
    public String toString() {
        return "MysqlReaderBean{" +
                "MYSQLREADER='" + MYSQLREADER + '\'' +
                ", url='" + url + '\'' +
                ", port='" + port + '\'' +
                ", database='" + database + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", querySql='" + querySql + '\'' +
                ", splitpk='" + splitpk + '\'' +
                '}';
    }
}
