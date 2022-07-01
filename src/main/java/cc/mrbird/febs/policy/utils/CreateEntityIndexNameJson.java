package cc.mrbird.febs.policy.utils;

import cc.mrbird.febs.policy.utils.CreateFileUtil;
import cc.mrbird.febs.policy.utils.DBHelper;
import com.alibaba.fastjson.JSONObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 将科技本体指标名称信息保存到json文件里，用于自动补全。
 */
public class CreateEntityIndexNameJson {
    static String sql = null;
    static DBHelper db1 = null;
    static ResultSet ret = null;
    public static void main(String[] main){
        sql = "SELECT entity_name,quantity FROM entity_index_name";
        db1 = new DBHelper(sql);//创建DBHelper对象
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code",0);
        List<JSONObject> data = new ArrayList<>();
        try {
            ret = db1.pst.executeQuery();//执行语句，得到结果集
            while (ret.next()) {
                JSONObject tmpJson = new JSONObject();
                tmpJson.put("name",ret.getString(1));
                tmpJson.put("quantity",ret.getString(2));
                data.add(tmpJson);
            }//显示数据
            ret.close();
            db1.close();//关闭连接
        } catch (SQLException e) {
            e.printStackTrace();
        }
        jsonObject.put("data",data);
        CreateFileUtil.createJsonFile(jsonObject.toString(), "defaultEffect", "entityIndexName");
    }
}
