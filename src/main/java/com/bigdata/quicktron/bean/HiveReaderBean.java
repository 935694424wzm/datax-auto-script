package com.bigdata.quicktron.bean;

import com.bigdata.quicktron.core.PluginError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @ClassName: HiveReaderBean
 * @Description: hive reader
 * @Author: wzm
 * @Date: 2022/8/26 11:22
 * @Version: 1.0
 **/
public class HiveReaderBean {

    private final Logger log = LoggerFactory.getLogger(HiveReaderBean.class.getName());

    private String hivereader;

    private String hivesql;

    private String defaultfs;

    private String tmpdatabase;

    private String tmpdatabasepath;

    private String hivesetsql;


    public HiveReaderBean() {
    }

    public HiveReaderBean(String hivereader, String hivesql, String defaultfs, String tmpdatabase, String tmpdatabasepath, String hivesetsql) {
        this.hivereader = hivereader;
        this.hivesql = hivesql;
        this.defaultfs = defaultfs;
        this.tmpdatabase = tmpdatabase;
        this.tmpdatabasepath = tmpdatabasepath;
        this.hivesetsql = hivesetsql;

        PluginError.getError(PluginError.DATABASETYPE_HIVE+PluginError.READER,hivesql,defaultfs,tmpdatabase,tmpdatabasepath);
    }


    public String getHivereader() {
        return hivereader;
    }

    public void setHivereader(String hivereader) {
        this.hivereader = hivereader;
    }

    public String getHivesql() {
        return hivesql;
    }

    public void setHivesql(String hivesql) {
        this.hivesql = hivesql;
    }

    public String getDefaultfs() {
        return defaultfs;
    }

    public void setDefaultfs(String defaultfs) {
        this.defaultfs = defaultfs;
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

    public String getHivesetsql() {
        return hivesetsql;
    }

    public void setHivesetsql(String hivesqlset) {
        this.hivesetsql = hivesqlset;
    }


    @Override
    public String toString() {
        return "HiveReaderBean{" +
                "hivereader='" + hivereader + '\'' +
                ", hivesql='" + hivesql + '\'' +
                ", defaultfs='" + defaultfs + '\'' +
                ", tmpdatabase='" + tmpdatabase + '\'' +
                ", tmpdatabasepath='" + tmpdatabasepath + '\'' +
                ", hivesqlset='" + hivesetsql + '\'' +
                '}';
    }
}
