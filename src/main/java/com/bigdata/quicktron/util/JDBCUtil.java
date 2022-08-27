package com.bigdata.quicktron.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

/**
 * @ClassName: JDBCUtil
 * @Description: jdbc链接工具类
 * @Author: wzm
 * @Date: 2022/8/16 14:01
 * @Version: 1.0
 **/
public class JDBCUtil {


    private static Logger log = LoggerFactory.getLogger(JDBCUtil.class.getName());
    private static String url_name = "jdbc:mysql://host_name:port_name/";
    private static Properties pros;




    /**
     * @param proFile
     * @return Properties
     * @author wzm
     * @description 获取配置文件的内容
     * @date 2021/12/15 16:22
     */
    public static Properties getProResult(String proFile) {
        try {
            pros = new Properties();
            pros.load(JDBCUtil.class.getResourceAsStream("/" + proFile));
            log.info("#####获取配置文件成功####");
        } catch (IOException e) {
            log.error("#####获取配置文件失败#####");
        }
        return pros;
    }

    /**
     * 获取数据库链接
     *
     * @return
     */
    public static Connection getConnetion(String proName) {
        Properties proResult = JDBCUtil.getProResult(proName);
        Connection conn = null;
        try {
            Class.forName(proResult.getProperty("driver"));
            //这是源代码
            conn = DriverManager.getConnection(proResult.getProperty("url"), proResult.getProperty("user"), proResult.getProperty("password"));
//            conn = DriverManager.getConnection(proResult.getProperty("pre_url")+ip+proResult.getProperty("suf_url"), proResult.getProperty("user"), proResult.getProperty("password"));
        } catch (ClassNotFoundException e) {
            log.error("加载驱动失败", e);
        } catch (SQLException e) {
            log.error("链接数据库失败", e);
        }
        return conn;
    }



    public static void getCloseConn(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                log.info("Connection链接关闭成功");
            } catch (SQLException e) {
                log.error("数据库Connection链接关闭失败", e);
            }
        }
    }

    public static void getClosePre(PreparedStatement pret) {
        if (pret != null) {
            try {
                pret.close();
                log.info("sql预编译对象关闭成功");
            } catch (SQLException e) {
                log.error("sql预编译对象关闭失败", e);
            }
        }
    }

    public static void getCloseSt(Statement st) {
        if (st != null) {
            try {
                st.close();
                log.info("st关闭成功");
            } catch (SQLException e) {
                log.error("st关闭失败");
            }
        }
    }

    public static void getCloseRset(ResultSet rset) {
        if (rset != null) {
            try {
                rset.close();
                log.info("rset对象关闭成功");
            } catch (SQLException e) {
                log.error("rset对象关闭失败");
            }
        }
    }


}
