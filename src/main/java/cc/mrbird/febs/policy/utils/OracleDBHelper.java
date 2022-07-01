package cc.mrbird.febs.policy.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * helper中的类是实验阶段写的代码，整合到系统流程中的时候会放到响应的文件夹下。
 * 也就是说这个文件夹中的类都不对web系统产生影响
 *
 */
public class OracleDBHelper {
    private final static String url = "jdbc:oracle:thin:@172.17.80.82:1521:orcl";
    private final static String name = "oracle.jdbc.driver.OracleDriver";
    private final static String user = "yearbook";
    //private final static String user = "GGFWPT";
    private final static String password = "Huawei12#$";
//    private final static String url = "jdbc:mysql:///febs?serverTimezone=UTC&amp;useUnicode=true&amp;characterEncoding=utf-8";
//    private final static String name = "com.mysql.cj.jdbc.Driver";
//    private final static String user = "root";
//    private final static String password = "root";

    public Connection conn = null;
    public PreparedStatement pst = null;

    public OracleDBHelper(String sql) {
        try {
            Class.forName(name);//指定连接类型
            conn = DriverManager.getConnection(url, user, password);//获取连接
            pst = conn.prepareStatement(sql);//准备执行语句
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            this.conn.close();
            this.pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}