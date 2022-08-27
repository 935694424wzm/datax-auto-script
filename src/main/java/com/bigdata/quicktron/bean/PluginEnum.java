package com.bigdata.quicktron.bean;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * @author wzm
 * @description 符合 datax的枚举类
 * @date 2022/8/25 10:20
 * @param
 * @return
 */
public enum PluginEnum {




    CLICKHOUSEREADER("clickhousereader"),
    MYSQLREADER("mysqlreader"),
    HIVEREADER("hivereader"),
    MYSQLWRITER("mysqlwriter"),
    HIVEWRITER("hivewriter");



    private static Logger log =LoggerFactory.getLogger(PluginEnum.class.getName());

    private final String plunginKey;

    PluginEnum(String plunginKey) {
        this.plunginKey=plunginKey;


    }

    public String getPlunginKey() {
        return plunginKey;
    }



    public static PluginEnum get(String code){
       // log.info("-----调用插件相关的枚举方法-----");
        PluginEnum status = Arrays.stream(PluginEnum.values()).filter(e -> e.getPlunginKey().equals(code)).findFirst().orElse(null);
        return status;
    }

}
