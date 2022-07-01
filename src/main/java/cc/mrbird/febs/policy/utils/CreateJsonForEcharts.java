package cc.mrbird.febs.policy.utils;

import cc.mrbird.febs.policy.entity.GexfNodeHelper;
import cc.mrbird.febs.policy.entity.IntermediateVariable;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 为echarts生成json文件
 */

/*
gexf echarts的节点类
"id":"0",
        "name":"Myriel",
        "symbolSize":28.685715,
        "x":-266.82776,
        "y":299.6904,
        "attributes":{
            "modularity_class":0
        }
*/
@Data
class GexfNode {
    private int id;
    private String name;
    private float symbolSize;
    private double x;
    private double y;
    private GexfNodeAttributes attributes;

}

/**
 * 节点字段辅助类
 */
@Data
class GexfNodeAttributes {
    private int modularity_class;

    GexfNodeAttributes(int modularity_class) {
        this.modularity_class = modularity_class;
    }
}

/**
 * gexf echarts的链接类
 * "id":null,
 * "name":null,
 * "source":"11",
 * "target":"2",
 * "lineStyle":{
 * "normal":{
 * <p>
 * }
 * }
 */
@Data
class GexfLink {
    private int id;
    private String name = "";
    private int source;
    private int target;
    private String lineStyle = "";
}

public class CreateJsonForEcharts {
    private static String sql = null;
    private static DBHelper db1 = null;
    private static ResultSet ret = null;

    /**
     * 构造政策工具的json数据。
     * 目前采用的就是这个方法，还可以使用mybatis-plus访问数据库的一种方式，在policy2Service.java中有写，但是比较复杂，所以目前采用了这种简单的方法。
     * 返回满足现实要求的json结果，并将结果保存到json文件中，用于下次直接读取
     * @return json
     */
    public static JSONObject getPolicyInstrumentTableJsonData(IntermediateVariable variable) {

        //查找符合条件的所有关键词
        List<String> keywordList = new ArrayList<>();
        StringBuilder sqlBuf = new StringBuilder("SELECT keyword '关键词' FROM  policy where 1 = 1");
        if(variable.getRegionId()!=null&&!"".equals(variable.getRegionId().trim())){
            sqlBuf.append(" and type = ").append(variable.getRegionId());
        }
        if(variable.getCreateTimeFrom()!=null&&!"".equals(variable.getCreateTimeFrom().trim())){
            sqlBuf.append(" and pubdata > '").append(variable.getCreateTimeFrom()).append("'");
        }
        if(variable.getCreateTimeTo()!=null&&!"".equals(variable.getCreateTimeTo().trim())){
            sqlBuf.append(" and pubdata < '").append(variable.getCreateTimeTo()).append("'");
        }
        sql = sqlBuf.toString();
        System.out.println(sql);
        db1 = new DBHelper(sql);//创建DBHelper对象

        try {
            ret = db1.pst.executeQuery();//执行语句，得到结果集
            while (ret.next()) {
                String keyword = ret.getString(1);
                keywordList.add(keyword);
            }//显示数据
            ret.close();
            db1.close();//关闭连接
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //对关键词去重并保存频率
        Map<String, Integer> wordFrequentMap = new HashMap<>();
        for (String policyKeywords : keywordList) {
            if (policyKeywords == null || policyKeywords.equals("nan"))
                continue;
            for (String word : policyKeywords.split(",")) {
                if (wordFrequentMap.containsKey(word)) {
                    wordFrequentMap.put(word, wordFrequentMap.get(word) + 1);
                } else {
                    wordFrequentMap.put(word, 1);
                }
            }
        }
        //对wordFrequentMap排序
        List<Map.Entry<String, Integer>> list = new ArrayList<>(wordFrequentMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            //升序排序
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        int SecondInstrumentNum = ClassifyKeywords.SecondInstrumentNum;
        int FirstInstrumentNum = ClassifyKeywords.FirstInstrumentNum;
        List<String>[] keywords = new ArrayList[SecondInstrumentNum];
        int[] frequency = new int[SecondInstrumentNum];
        int[] group_frequency = new int[FirstInstrumentNum];
        double[] proportion = new double[SecondInstrumentNum];
        double[] group_proportion = new double[FirstInstrumentNum];
        for (int i = 0; i < keywords.length; i++) {
            keywords[i] = new ArrayList<>();
        }
        //保存不同工具类型的关键词集合和词频
        for (Map.Entry<String, Integer> e : list) {
            String word = e.getKey();
            int wordInstrumentType = ClassifyKeywords.getInstrumentSecondIndexByKeyword(word);
            if(wordInstrumentType!=-1){
                keywords[wordInstrumentType].add(word);
                frequency[wordInstrumentType] += e.getValue();
            }
        }
        for (int i = 0; i < SecondInstrumentNum; i++) {
            group_frequency[ClassifyKeywords.getInstrumentFirsrIndexBySecondIndex(i)] += frequency[i];
        }
        for (int i = 0; i < SecondInstrumentNum; i++) {
            proportion[i] = (double) frequency[i] / group_frequency[ClassifyKeywords.getInstrumentFirsrIndexBySecondIndex(i)];
        }
        int words_num = 0;
        for (int i = 0; i < FirstInstrumentNum; i++) {
            words_num += group_frequency[i];
        }
        for (int i = 0; i < FirstInstrumentNum; i++) {
            group_proportion[i] = (double) group_frequency[i] / words_num;
        }


        JSONObject json = new JSONObject();
        json.put("keywords", keywords);
        json.put("frequency", frequency);
        json.put("group_frequency", group_frequency);
        json.put("proportion", proportion);
        json.put("group_proportion", group_proportion);
        json.put("words_num", words_num);
        CreateFileUtil.createJsonFile(json.toString(), "json_for_helpers", "instrumentTableData");
        return json;
    }


    /**
     * 计算关键词的各种维度，用于构造政策导向分析图所需的数据
     * @param variable 传递参数的中间变量
     * @return json
     */
    public static JSONObject getDimensionOfKeywordsToJson(IntermediateVariable variable) {
        //查找符合条件的所有关键词
        List<String> pubdataList = new ArrayList<>();
        List<String> organList = new ArrayList<>();
        List<String> keywordList = new ArrayList<>();
        StringBuilder sqlBuf = new StringBuilder("SELECT YEAR(pubdata) '发布时间', organ '发布单位', keyword '关键词' FROM policy where 1 = 1");
        if(variable.getRegionId()!=null&&!"".equals(variable.getRegionId().trim())){
            sqlBuf.append(" and type = ").append(variable.getRegionId());
        }
        if(variable.getCreateTimeFrom()!=null&&!"".equals(variable.getCreateTimeFrom().trim())){
            sqlBuf.append(" and pubdata > '").append(variable.getCreateTimeFrom()).append("'");
        }
        if(variable.getCreateTimeTo()!=null&&!"".equals(variable.getCreateTimeTo().trim())){
            sqlBuf.append(" and pubdata < '").append(variable.getCreateTimeTo()).append("'");
        }
        sql = sqlBuf.toString();
        System.out.println(sql);
        db1 = new DBHelper(sql);//创建DBHelper对象

        try {
            ret = db1.pst.executeQuery();//执行语句，得到结果集
            while (ret.next()) {
                String keyword = ret.getString(3);
                String pubdata = ret.getString(1);
                String organ = ret.getString(2);
//                System.out.println(pubdata.toString() + " " + organ + " " + keyword);
                if (pubdata == null) {
                    pubdataList.add(null);
                } else {
                    pubdataList.add(pubdata);
                    keywordList.add(keyword);
                    organList.add(organ);
                }

            }//显示数据
            ret.close();
            db1.close();//关闭连接
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<String[]> data = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0; i < keywordList.size(); i++) {
            if (keywordList.get(i) == null) {
                continue;
            }
            for (String word : keywordList.get(i).split(",")) {
                String[] row = new String[6];
                row[0] = pubdataList.get(i);
                row[1] = organList.get(i);
                int InstrumentSecondIndex = ClassifyKeywords.getInstrumentSecondIndexByKeyword(word);
                int threeIndustriesSecondIndex = ClassifyKeywords.getThreeIndustriesSecondIndexByKeyword(word);
                String activityName = ClassifyKeywords.getActivityNameByIndex(ClassifyKeywords.getActivityTypeIndexByKeyword(word));
                if (InstrumentSecondIndex == -1 || threeIndustriesSecondIndex == -1 || activityName == null)
                    continue;
                row[2] = String.valueOf(rand.nextDouble() + InstrumentSecondIndex);
                row[3] = String.valueOf(rand.nextDouble() + threeIndustriesSecondIndex);
                row[4] = activityName;
                row[5] = word;
                data.add(row);
            }

        }
        JSONObject json = new JSONObject();
        json.put("data", data);
        CreateFileUtil.createJsonFile(json.toString(), "json_for_helpers", "dimensionOfKeywords");
        return json;
    }


    public static void main(String[] args) throws IOException {
//        createJsonForEcharts();
//        for(int i = 0;i<1000;i++){
//            System.out.println(Math.sin(i)+","+Math.cos(i));
//        }
//        JSONObject json = getPolicyInstrumentTableJsonData();
//        CreateFileUtil.createJsonFile(json.toString(), "json_for_instrument", "instrumentTableData");
//        JSONObject json = getDimensionOfKeywordsToJson();
//        CreateFileUtil.createJsonFile(json.toString(), "json_for_helpers", "dimensionOfKeywords");
        /*Random rand = new Random();

        for(int j=0;j<5;j++){
            for(int i=0; i<10; i++) {
                System.out.println(rand.nextDouble() + 1);
            }
            System.out.println("***");
        }*/

    }

    /**
     * 废弃的一种写法，构造关键词语义网络的json数据
     * @throws IOException
     */
    private static void createJsonForEcharts() throws IOException {
        sql = "SELECT e.word '关键词', o.id '所述大类', e.frequent '频率' " +
                "FROM " +
                " fieldlevel_one o " +
                "JOIN fieldlevel_two t ON o.id = t.parentId " +
                "JOIN fieldlevel_three e ON t.id = e.parent_id " +
                "WHERE " +
                " e.word != 'nan' " +
                "ORDER BY e.frequent limit 300";
        db1 = new DBHelper(sql);//创建DBHelper对象

        //保存关键词对应的id
        Map<String, Integer> name_id_map = new HashMap<>();
        List<GexfNodeHelper> nodes = new ArrayList<>();
        try {
            int node_id = 0;
            ret = db1.pst.executeQuery();//执行语句，得到结果集
            while (ret.next()) {
                String keyword = ret.getString(1);
                int type = ret.getInt(2);
                float weight = ret.getFloat(3);
                name_id_map.put(keyword, node_id);
                GexfNodeHelper node = new GexfNodeHelper(node_id++, keyword, type, weight);
                nodes.add(node);
            }//显示数据
            ret.close();
            db1.close();//关闭连接
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //保存nodes节点信息，最后转成json类型的数据。
        List<GexfNode> gn_list = new ArrayList<GexfNode>();
        List<GexfLink> gl_list = new ArrayList<>();
        Map<String, Integer> nnrelation = new HashMap<>();
        int gl_id = 0;
        int i = 0;
        for (GexfNodeHelper node : nodes) {

            System.out.println(i++ + "," + nodes.size());
            //对一个node节点信息进行赋值
            GexfNode gn = new GexfNode();
            gn.setId(node.getId());
            gn.setName(node.getKeyword());
            gn.setSymbolSize(node.getWeight());
            gn.setAttributes(new GexfNodeAttributes(node.getType() - 1));
            if (i == 1) {
                //中心	x:-300，y:-50
                gn.setX(-300);
                gn.setY(-50);
            } else if (node.getType() == 1) {
                //左上角	x:-1300，y:-600
                gn.setX(-300 - (Math.random() * 1000));
                gn.setY(-50 - (Math.random() * 500));
            } else if (node.getType() == 2) {
                //右上角	x:600，y:-600
                gn.setX(-300 + (Math.random() * 900));
                gn.setY(-50 - (Math.random() * 500));
            } else if (node.getType() == 3) {
                //左下角	x:-1300，y:500
                gn.setX(-300 - (Math.random() * 1000));
                gn.setY(-50 + (Math.random() * 500));
            } else {
                //右下角	"x":600,"y":500
                gn.setX(-300 + (Math.random() * 1000));
                gn.setY(-50 + (Math.random() * 500));
            }
//            gn.attributes.modularity_class = node.getType();
            gn_list.add(gn);
            sql = "SELECT keyword FROM policy p WHERE keyword LIKE '%" + node.getKeyword() + "%'";
            db1 = new DBHelper(sql);//创建DBHelper对象
            try {
                ret = db1.pst.executeQuery();//执行语句，得到结果集
                Map<String, Integer> map = node.getRelationWords();
                while (ret.next()) {
                    String keyword = ret.getString(1);
                    for (String word : keyword.split(",")) {
                        if (!word.equals(node.getKeyword()))
                            if (map.containsKey(word)) {
                                map.put(word, map.get(word) + 1);
                            } else {
                                map.put(word, 1);
                            }
                    }
                }
                //关联次数大于1的关键词之间建立关联
                for (String n : map.keySet()) {
                    /*if (map.get(n) > 1) {
                        GexfLink gl = new GexfLink();
                        gl.setId(gl_id++);
                        gl.setName("");
                        gl.setSource(name_id_map.get(node.getKeyword()));
                        gl.setTarget(name_id_map.get(n));
                        gl_list.add(gl);
                    }*/
                    if (map.get(n) > 10) {
                        if (name_id_map.containsKey(n)) {
                            System.out.println(map.get(n));
                            if (!nnrelation.containsKey(n + "," + node.getKeyword())) {
                                GexfLink gl = new GexfLink();
                                gl.setId(gl_id++);
                                gl.setName("");
                                gl.setSource(name_id_map.get(node.getKeyword()));
                                gl.setTarget(name_id_map.get(n));
                                gl_list.add(gl);
                                nnrelation.put(node.getKeyword() + "," + n, 0);
                            }
                        }
                    }
                }
                node.setRelationWords(map);
                ret.close();
                db1.close();//关闭连接
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        /*
        //简单输出
        for (GexfNodeHelper node : nodes) {
            System.out.print(node.getKeyword() + " " + node.getType() + " " + node.getWeight() + " ");
            for (String rw : node.getRelationWords().keySet())
                System.out.print(rw + " " + node.getRelationWords().get(rw) + ",");
            System.out.println();
            System.out.println("************************************************");
        }*/

//        for (GexfNode gn : gn_list) {
//            System.out.println(gn.getId() + " " + gn.getName());
//        }
        JSONObject json = new JSONObject();
        json.put("nodes", gn_list);
        json.put("links", gl_list);
        System.out.println(json.toString());
        CreateFileUtil.createJsonFile(json.toString(), "json_for_echarts", "policy_national_last");
    }

    //根据值对map排序，返回值最大的一半键值集合（目前版本中没有使用，由于有大量的值为1 ，所以通过 10 为门槛进行筛选）
    private static List<String> getMaxerKey(Map<String, Integer> map) {
        List<String> maxerkeys = new ArrayList<>();
        Collection<Integer> c = map.values();
        Object[] obj = c.toArray();
        Arrays.sort(obj);
        Integer maxValue;

//        for(int i = 1;i<obj.length/2;i++) {
//            System.out.println(obj[obj.length - i]);
//        }
        for (int i = 1; i < obj.length / 5; i++) {
//            System.out.println(obj[i]);
            maxValue = (Integer) obj[obj.length - i];
            System.out.println(maxValue);
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                if (maxValue == entry.getValue()) {
                    maxerkeys.add(entry.getKey());
                }
            }
        }
        return maxerkeys;
    }

}


