package com.bigdata.quicktron.bean;

import com.bigdata.quicktron.core.PluginError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: ClickhouseReaderBean
 * @Description:  ck reader
 * @Author: wzm
 * @Date: 2022/8/27 16:04
 * @Version: 1.0
 **/
public class ClickhouseReaderBean {


    private static final Logger log = LoggerFactory.getLogger(ClickhouseReaderBean.class.getName());


    private String CLICKHOUSEREADER;

    private String url;

    private String port;

    private String database;

    private String username;

    private String password;

    private String querySql;


    public ClickhouseReaderBean() {
    }

    public ClickhouseReaderBean(String CLICKHOUSEREADER, String url, String port, String database, String username, String password, String querySql) {
        this.CLICKHOUSEREADER = CLICKHOUSEREADER;
        this.url = url;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.querySql = querySql;

        PluginError.getError(PluginError.DATABASETYPE_CLICKHOUSE+PluginError.READER,url,database,username,password,querySql);
    }

    public String getCLICKHOUSEREADER() {
        return CLICKHOUSEREADER;
    }

    public void setCLICKHOUSEREADER(String CLICKHOUSEREADER) {
        this.CLICKHOUSEREADER = CLICKHOUSEREADER;
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

    @Override
    public String toString() {
        return "ClickhouseReaderBean{" +
                "CLICKHOUSEREADER='" + CLICKHOUSEREADER + '\'' +
                ", url='" + url + '\'' +
                ", port='" + port + '\'' +
                ", database='" + database + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", querySql='" + querySql + '\'' +
                '}';
    }
}
