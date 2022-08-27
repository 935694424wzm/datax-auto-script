package com.bigdata.quicktron.core;

import com.bigdata.quicktron.util.JDBCUtil;
import org.apache.commons.lang3.StringUtils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName: SendMail
 * @Description: 开发发送邮件功能
 * @Author: wzm
 * @Date: 2022/8/16 11:29
 * @Version: 1.0
 **/
public class SendMail {

    private static final String USER = "data@flashhold.com";//发件人称号，同邮件地址
    private static final String PASSWORD = "abcdABCD!";//客户端授权码

    /**
     * @param to    收件人列表，以逗号分隔
     * @param text  邮件内容，
     * @param title 邮件标题
     * @return java.lang.Boolean
     * @author wzm
     * @description 发送验证信息的邮件
     * @date 2022/8/16 11:44
     */
    public static Boolean sendMail(String to, String text, String title) {

        try {
            final Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.host", "smtp.flashhold.com");


            // 发件人的账号
            props.put("mail.user", USER);
            //发件人的密码
            props.put("mail.password", PASSWORD);

            // 构建授权信息，用于进行SMTP进行身份验证
            Authenticator authenticator = new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    // 用户名、密码
                    String userName = props.getProperty("mail.user");
                    String password = props.getProperty("mail.password");
                    return new PasswordAuthentication(userName, password);
                }
            };
            // 使用环境属性和授权信息，创建邮件会话
            Session mailSession = Session.getInstance(props, authenticator);
            // 创建邮件消息
            MimeMessage message = new MimeMessage(mailSession);
            // 设置发件人
            String username = props.getProperty("mail.user");
            InternetAddress form = new InternetAddress(username, "DIC", "UTF-8");
            message.setFrom(form);

            // 创建收件人列表
            if (to != null && to.trim().length() > 0) {
                String[] arr = to.split(",");
                int receiverCount = arr.length;
                if (receiverCount > 0) {
                    InternetAddress[] address = new InternetAddress[receiverCount];
                    for (int i = 0; i < receiverCount; i++) {
                        address[i] = new InternetAddress(arr[i]);
                    }
                    message.addRecipients(Message.RecipientType.TO, address);
                }
                // 设置邮件标题
                message.setSubject(title);

                // 设置邮件的内容体
                message.setContent(text, "text/html;charset=UTF-8");
                // 发送邮件
                Transport.send(message);
            } else {
                System.out.println("None receiver");

            }

//            InternetAddress toAddress = new InternetAddress(to);
//            message.setRecipient(Message.RecipientType.TO, toAddress);


            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * @param proName
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     * @author wzm
     * @description 获取离职人员的查询sql的结果，只获取昨天的离职人员
     * @date 2022/8/16 15:30
     */
    public static List<Map<String, String>> getSqlResult(String proName) {
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        Date d = cal.getTime();
        SimpleDateFormat sp=new SimpleDateFormat("yyyy-MM-dd");
        String yesterday=sp.format(d);//获取昨天日期

        List<Map<String, String>> tableList = new ArrayList<>();
        Connection connetion = JDBCUtil.getConnetion(proName);
        PreparedStatement pst=null;
        ResultSet rst=null;
        String sql="select emp_name,emp_email,emp_quit_date,emp_org_name,emp_position,emp_work_place from kc_collect.emp_quit_record_info where emp_quit_date=?";
        try {
            pst=connetion.prepareStatement(sql);
            pst.setString(1,yesterday);
            rst=pst.executeQuery();
            while (rst.next()){
                Map<String, String> tMap = new HashMap<>();
                tMap.put("emp_name",rst.getString("emp_name"));
                tMap.put("emp_email",rst.getString("emp_email"));
                tMap.put("emp_quit_date",rst.getString("emp_quit_date"));
                tMap.put("emp_org_name",rst.getString("emp_org_name"));
                tMap.put("emp_position",rst.getString("emp_position"));
                tMap.put("emp_work_place",rst.getString("emp_work_place"));
                tableList.add(tMap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            JDBCUtil.getCloseRset(rst);
            JDBCUtil.getClosePre(pst);
            JDBCUtil.getCloseConn(connetion);
        }

        return tableList;
    }

    /**
     * @author wzm
     * @description 生成邮件内容
     * @date 2022/8/16 15:43
     * @param listMap
     * @return java.lang.String
     */
    public static String getContent(List<Map<String, String>> listMap) {

        if(listMap==null || listMap.size()<1){
            return null;
        }
        StringBuilder content = new StringBuilder("<html><head><style>h4 {text-align:center}</style></head><body>");
        content.append("<p>您好，领导：</p>");
        content.append("<p>&#12288;&#12288;&#12288;以下表格内容为昨天离职人员名单，请查阅&抽空关闭相关人员账号！！！</p>");
        content.append("<h4>离职人员名单</h4>");
        content.append("<table border=\"2\" style=\"width:800px;height:100px;border:solid 1px #E8F2F9;font-size=14px;font-size;18px;\">");
        content.append("<tr><th>员工姓名</th><th>员工邮箱</th><th>离职日期</th><th>员工组织</th><th>员工职位</th><th>工作地点</th></tr>");

        for (Map<String, String> stringStringMap : listMap) {
            content.append("<tr>")
                    .append("<td>"+stringStringMap.get("emp_name")+"</td>")
                    .append("<td>"+stringStringMap.get("emp_email")+"</td>")
                    .append("<td>"+stringStringMap.get("emp_quit_date")+"</td>")
                    .append("<td>"+stringStringMap.get("emp_org_name")+"</td>")
                    .append("<td>"+stringStringMap.get("emp_position")+"</td>")
                    .append("<td>"+stringStringMap.get("emp_work_place")+"</td>")
                    .append("</tr>");
        }

//        content.append("<tr>" +
//                "<td>李xx</td>" +
//                "<td>lixx@flashhold.com</td>" +
//                "<td>2022-08-15</td>" +
//                "<td>xxxxxxx/xxxxx/xxxx</td>" +
//                "<td>java开发</td>" +
//                "<td>上海</td>" +
//                "</tr>");
        content.append("</table>").append("</body></html>");
        return content.toString();
    }


    /**
     * @author wzm
     * @description 触发任务
     * @date 2022/8/16 16:18
     * @param
     * @return java.lang.String
     */
    public static void startJob(String to){
        List<Map<String, String>> sqlResult = getSqlResult("mysql.properties");
        String content = getContent(sqlResult);
//        String to="wangliang@flashhold.com,wangziming@flashhold.com,zhabowen@flashhold.com";


        if (StringUtils.isNotEmpty(content)){
            SendMail.sendMail(to, content, "DIC-最新离职人员名单");
            System.out.println("========================邮件发送成功=============================");
        }else{
            System.out.println("========================今日无离职人员不需要发送邮件=============================");
        }


    }


    public static void main(String[] args) {
        SendMail.startJob(args[0]);
    }
}

