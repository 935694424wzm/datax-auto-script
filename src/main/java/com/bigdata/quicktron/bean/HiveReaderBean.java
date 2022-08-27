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


    public HiveReaderBean() {
    }

    public HiveReaderBean(String hivereader, String hivesql, String defaultfs) {
        this.hivereader = hivereader;
        this.hivesql = hivesql;
        this.defaultfs = defaultfs;

        PluginError.getError(PluginError.DATABASETYPE_HIVE+PluginError.READER,hivesql,defaultfs);

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

    @Override
    public String toString() {
        return "HiveReaderBean{" +
                "hivereader='" + hivereader + '\'' +
                ", hivesql='" + hivesql + '\'' +
                ", defaultfs='" + defaultfs + '\'' +
                '}';
    }
}
