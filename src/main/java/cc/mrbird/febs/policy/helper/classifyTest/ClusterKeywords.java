package cc.mrbird.febs.policy.helper.classifyTest;

import cc.mrbird.febs.policy.utils.DBHelper;
import com.hankcs.hanlp.mining.cluster.ClusterAnalyzer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

/**
 * 对关键词聚类
 */
public class ClusterKeywords {
    static String sql = null;
    static DBHelper db1 = null;
    static ResultSet ret = null;

    public static void main(String[] args) {
//        clusterTitleByKeywords();
//        clusterKeywordsByKeywords();
//        clusterTitleByKeywords();
        /*Map<String,Integer> map = new HashMap<>();
        map.put("A",0);
        map.put("A",1);
        map.put("A",0);
        map.put("B",0);
        System.out.println(map.size());*/
        clusterPolicyName();
    }

    private static void clusterTitleByKeywords() {
        sql = "SELECT p.name '政策标题', p.text '关键词' FROM policy p where p.name is NOT NULL and p.keyword != 'nan'";
        db1 = new DBHelper(sql);//创建DBHelper对象
        ClusterAnalyzer<String> analyzer = new ClusterAnalyzer<String>();
        try {
            ret = db1.pst.executeQuery();//执行语句，得到结果集
            while (ret.next()) {
                String title = ret.getString(1);
                String keywords = ret.getString(2);
                analyzer.addDocument(title, keywords);
            }//显示数据
            ret.close();
            db1.close();//关闭连接
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        System.out.println(analyzer.kmeans(3));
//        System.out.println(analyzer.repeatedBisection(3));
        System.out.println(analyzer.repeatedBisection(1.0));
    }

    private static void clusterKeywordsByKeywords() {
        sql = "SELECT t.word '关键词',t.frequent '频率' FROM fieldlevel_three t ORDER BY t.frequent DESC";//SQL语句
        db1 = new DBHelper(sql);//创建DBHelper对象
        ClusterAnalyzer<String> analyzer = new ClusterAnalyzer<String>();
        try {
            ret = db1.pst.executeQuery();//执行语句，得到结果集
            while (ret.next()) {
                String word = ret.getString(1);
                String frequent = ret.getString(2);
//                System.out.println(word + "\t" + frequent + "\t");
                analyzer.addDocument(word, word);
            }//显示数据
            ret.close();
            db1.close();//关闭连接
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        System.out.println(analyzer.kmeans(3));
//        System.out.println(analyzer.repeatedBisection(3));
        System.out.println(analyzer.repeatedBisection(15));
    }

    /**
     * 对科技政策标题进行聚类
     * 目的是想对政策对象进行分类
     */
    private static void clusterPolicyName() {
        sql = "SELECT id, name FROM policy";//SQL语句
        db1 = new DBHelper(sql);//创建DBHelper对象
        ClusterAnalyzer<String> analyzer = new ClusterAnalyzer<String>();
        try {
            ret = db1.pst.executeQuery();//执行语句，得到结果集
            while (ret.next()) {
                int id = ret.getInt(1);
                String name = ret.getString(2);
                analyzer.addDocument(Integer.toString(id), name);
            }//显示数据
            ret.close();
            db1.close();//关闭连接
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Set<String>> result = analyzer.repeatedBisection(1.0);
        System.out.println(result);
        int field = 0;
        for (Set<String> oneTheme : result) {
            field++;
            for (String id : oneTheme) {
                sql = "update policy set superior = " + field + " where id = " + id;
                System.out.println(sql);
                db1 = new DBHelper(sql);//创建DBHelper对象
                try {
                    db1.pst.executeUpdate();//执行语句，得到结果集
                    db1.close();//关闭连接
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
