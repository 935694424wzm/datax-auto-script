package com.bigdata.quicktron.bean;

import com.bigdata.quicktron.core.PluginError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: MysqlWriterBean
 * @Description: mysql写插件
 * @Author: wzm
 * @Date: 2022/8/26 11:25
 * @Version: 1.0
 **/
public class MysqlWriterBean {



    private final Logger log= LoggerFactory.getLogger(MysqlWriterBean.class.getName());


    private String mysqlwriter;

    private String url;

    private String port;

    private String database;

    private String username;

    private String password;

    private String column;

    private String table;

    private String presql;

    private String writerMode;

    public MysqlWriterBean() {
    }


    public MysqlWriterBean(String mysqlwriter, String url, String port, String database, String username, String password, String column, String table, String presql, String writerMode) {
        this.mysqlwriter = mysqlwriter;
        this.url = url;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.column = column;
        this.table = table;
        this.presql = presql;
        this.writerMode = writerMode;


        PluginError.getError(PluginError.DATABASETYPE_MYSQL+PluginError.WRITER,url,database,username,password,column,table);
    }

    public String getMysqlwriter() {
        return mysqlwriter;
    }

    public void setMysqlwriter(String mysqlwriter) {
        this.mysqlwriter = mysqlwriter;
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

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getPresql() {
        return presql;
    }

    public void setPresql(String presql) {
        this.presql = presql;
    }

    public String getWriterMode() {
        return writerMode;
    }

    public void setWriterMode(String writerMode) {
        this.writerMode = writerMode;
    }

    @Override
    public String toString() {
        return "MysqlWriterBean{" +
                "mysqlwriter='" + mysqlwriter + '\'' +
                ", url='" + url + '\'' +
                ", port='" + port + '\'' +
                ", database='" + database + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", column='" + column + '\'' +
                ", table='" + table + '\'' +
                ", presql='" + presql + '\'' +
                ", writerMode='" + writerMode + '\'' +
                '}';
    }
}
