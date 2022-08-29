package com.bigdata.quicktron.bean;

import com.bigdata.quicktron.core.PluginError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: HiveWriterBean
 * @Description: hive 写插件
 * @Author: wzm
 * @Date: 2022/8/26 11:28
 * @Version: 1.0
 **/
public class HiveWriterBean {


    private final Logger log= LoggerFactory.getLogger(HiveWriterBean.class.getName());

    private String hivewriter;

    private String database;

    private String table;

    private String defaultfs;

    private String writemode;

    private String partition;

    private String tmpdatabase;

    private String tmpdatabasepath;

    private String column;

    private String presql;

    private String hivesetsql;


    public HiveWriterBean() {
    }


    public HiveWriterBean(String hivewriter, String database, String table, String defaultfs, String writemode, String partition, String tmpdatabase, String tmpdatabasepath, String column,String presql,String hivesetsql) {
        this.hivewriter = hivewriter;
        this.database = database;
        this.table = table;
        this.defaultfs = defaultfs;
        this.writemode = writemode;
        this.partition = partition;
        this.tmpdatabase = tmpdatabase;
        this.tmpdatabasepath = tmpdatabasepath;
        this.column = column;
        this.presql=presql;
        this.hivesetsql=hivesetsql;

        PluginError.getError(PluginError.DATABASETYPE_HIVE+PluginError.WRITER,database,table,defaultfs,tmpdatabase,tmpdatabasepath,column);
    }


    public String getHivewriter() {
        return hivewriter;
    }

    public void setHivewriter(String hivewriter) {
        this.hivewriter = hivewriter;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getDefaultfs() {
        return defaultfs;
    }

    public void setDefaultfs(String defaultfs) {
        this.defaultfs = defaultfs;
    }

    public String getWritemode() {
        return writemode;
    }

    public void setWritemode(String writemode) {
        this.writemode = writemode;
    }

    public String getPartition() {
        return partition;
    }

    public void setPartition(String partition) {
        this.partition = partition;
    }

    public String getTmpdatabase() {
        return tmpdatabase;
    }

    public void setTmpdatabase(String tmpdatabase) {
        this.tmpdatabase = tmpdatabase;
    }

    public String getTmpdatabasepath() {
        return tmpdatabasepath;
    }

    public void setTmpdatabasepath(String tmpdatabasepath) {
        this.tmpdatabasepath = tmpdatabasepath;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getPresql() {
        return presql;
    }

    public void setPresql(String presql) {
        this.presql = presql;
    }

    public String getHivesetsql() {
        return hivesetsql;
    }

    public void setHivesetsql(String hivesqlset) {
        this.hivesetsql = hivesqlset;
    }

    @Override
    public String toString() {
        return "HiveWriterBean{" +
                "hivewriter='" + hivewriter + '\'' +
                ", database='" + database + '\'' +
                ", table='" + table + '\'' +
                ", defaultfs='" + defaultfs + '\'' +
                ", writemode='" + writemode + '\'' +
                ", partition='" + partition + '\'' +
                ", tmpdatabase='" + tmpdatabase + '\'' +
                ", tmpdatabasepath='" + tmpdatabasepath + '\'' +
                ", column='" + column + '\'' +
                ", presql='" + presql + '\'' +
                ", hivesqlset='" + hivesetsql + '\'' +
                '}';
    }
}
