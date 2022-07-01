package cc.mrbird.febs.policy.utils;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.policy.entity.YearBookData;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 生成Map地图用于展示的json数据
 */
@Data
class MapData {
    private String year;
    private String name;
    private Double value;
}

/**
 * 数据下钻到一层
 */
@Data
class MapDataMore extends MapData {
    private JSONObject moreValue;
}

/**
 * 为了让数据下钻可以到最后一个叶子结点，设计此类
 */
@Data
class MapDetail extends MapData {
    private List<MapDetail> children;
}

/**
 * 实体评价指标树形结构
 */
@Data
class EntityValueTree implements Serializable {
    private String id;
    private String parentId;
    private String name;
    private String content;
    private Float weight;
    private JSONObject value;
    private Double score;
    private boolean hasParent = false;
    private boolean hasChild = false;
    private List<EntityValueTree> children = new ArrayList<>();

    public void initChildren() {
        this.children = new ArrayList<>();
    }
}

/**
 * 用于生成Map地图进行综合展示的数据，
 * 这种统计起到一个总览的作用，因此在生成数据的时候已经将下钻需要用到的所有的数据都查出来保存成json格式的数据了。
 */
public class CreateMapData {

    static String sql = null;
    static DBHelper db1 = null;
    static ResultSet ret = null;

    static String sql2 = null;
    static OracleDBHelper db2 = null;
    static ResultSet ret2 = null;
    static String[] region = {"北京", "天津", "河北", "山西", "内蒙古", "辽宁", "吉林", "黑龙江", "上海", "江苏", "浙江", "安徽", "福建", "江西", "山东", "河南", "湖北", "湖南", "广东", "广西", "海南", "重庆", "四川", "贵州", "云南", "西藏", "陕西", "甘肃", "青海", "宁夏", "新疆", "台湾"};

    static String[] year = {"2009", "2010", "2011", "2012", "2013"};

    public static void main(String[] args) throws FebsException, IOException, ClassNotFoundException {
        //createMapData2();
        //createMapData2的部分功能，从文件中将查询好的各城市各项指标取出来，生成满足echart显示需要的数据
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(new File("D:\\p.txt")));
        Object readObject = in.readObject();
        List<EntityValueTree> valueTreeList = (List<EntityValueTree>) readObject;
        JSONObject yearRegionValueJsonAll = getYearRegionValueForAll(valueTreeList);
        CreateFileUtil.createJsonFile(yearRegionValueJsonAll.toString(), "json_for_CreateMapData", "yearRegionValueAll");

    }

    //统计各项指标各地区得分情况，结果是以指标树为依托的，每个指标包含各个城市不同年份的得分情况
    //最后的结果保存到json_for_CreateMapData/y earRegionValueAll.json文件中，直接用于index页面的地图效果展示
    //仅开放createMapData2一个方法，用于更新评估指标后更新地图数据
    public static boolean createMapData2() throws IOException {
        try {
            sql = "SELECT * FROM entity_index";
            db1 = new DBHelper(sql);//创建DBHelper对象
            List<EntityValueTree> tmpValueTreeList = new ArrayList<>();
            try {
                ret = db1.pst.executeQuery();//执行语句，得到结果集
                while (ret.next()) {
                    EntityValueTree tmpValue = new EntityValueTree();
                    tmpValue.setId(ret.getString(1));
                    tmpValue.setParentId(ret.getString(2));
                    tmpValue.setName(ret.getString(3));
                    tmpValue.setContent(ret.getString(4));
                    tmpValue.setWeight(ret.getFloat(5));
                    tmpValueTreeList.add(tmpValue);
                }//显示数据
                ret.close();
                db1.close();//关闭连接
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //遍历评价指标，分别进行统计
            //对每个指标，统计该指标不同城市不同年份的数值
            for (EntityValueTree tmpVal : tmpValueTreeList) {
                if (!tmpVal.getContent().equals("")) {
                    sql2 = " SELECT DISTINCT NAME, REGION, VALUE, UNIT_C, YEARS FROM annual_cell WHERE NAME = '" + tmpVal.getContent() + "'  and (YEARS = '2009' or YEARS = '2010' or YEARS = '2011' or YEARS = '2012' or YEARS = '2013') ORDER BY REGION, YEARS";
                    //sql2 = " SELECT DISTINCT NAME, REGION, VALUE, UNIT_C, YEARS FROM annual WHERE NAME = '" + tmpVal.getContent() + "'  and (YEARS = '2009' or YEARS = '2010' or YEARS = '2011' or YEARS = '2012' or YEARS = '2013') ORDER BY REGION, YEARS";
                    System.out.println(sql2);
                    db2 = new OracleDBHelper(sql2);//创建DBHelper对象
                    List<YearBookData> tmpYearBookData = new ArrayList<>();
                    try {
                        ret2 = db2.pst.executeQuery();//执行语句，得到结果集
                        while (ret2.next()) {
                            YearBookData tmpValue = new YearBookData();
                            tmpValue.setName(ret2.getString(1));
                            tmpValue.setRegion(ret2.getString(2));
                            tmpValue.setValue(ret2.getString(3));
                            tmpValue.setUnitC(ret2.getString(4));
                            tmpValue.setYears(ret2.getString(5));
                            tmpYearBookData.add(tmpValue);
                        }//显示数据
                        ret2.close();
                        db2.close();//关闭连接
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    JSONObject tmp = getOneGroupJsonData(tmpYearBookData);
                    tmpVal.setValue(tmp);
                }

            }
            //影响力评价指标树形结构
            List<EntityValueTree> valueTreeList = new ArrayList<>();
            tmpValueTreeList.forEach(children -> {
                String pid = children.getParentId();
                if (pid == null || "0".equals(pid)) {
                    valueTreeList.add(children);
                    return;
                }
                for (EntityValueTree n : tmpValueTreeList) {
                    String id = n.getId();
                    if (id != null && id.equals(pid)) {
                        if (n.getChildren() == null)
                            n.initChildren();
                        n.getChildren().add(children);
                        children.setHasParent(true);
                        n.setHasChild(true);
                        return;
                    }
                }
            });

            //valueTreeList 是一个数据完整（包括每一项叶子指标不同地域各个年份的数据值占比）、标准的树形指标结构
            //将valueTreeList 保存到文件中 下次用在读取
//        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File("D:\\p.txt")));
//        out.writeObject(valueTreeList);
//        //拿出来
//        ObjectInputStream in=new ObjectInputStream(new FileInputStream(new File("D:\\p.txt")));
//        Object readObject = in.readObject();
            JSONObject result = new JSONObject();
            result.put("data", valueTreeList);
            CreateFileUtil.createJsonFile(result.toString(), "json_for_CreateMapData", "valueTreeList");
            //构造前台地图展示需要的“【年，地域，数值】的数据格式
            JSONObject yearRegionValueJson = getYearRegionValueForAll(valueTreeList);
            CreateFileUtil.createJsonFile(yearRegionValueJson.toString(), "json_for_CreateMapData", "yearRegionValueAll");

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    /**
     * 根据已经赋值的指标树，返回年份、地域、值的数据格式（第二版 下钻全部）
     *
     * @param valueTreeList 已经赋值的指标树
     */
    private static JSONObject getYearRegionValueForAll(List<EntityValueTree> valueTreeList) {
        MapDetail[][] mapDetails = new MapDetail[year.length][region.length];
        for (int i = 0; i < year.length; i++) {
            for (int j = 0; j < region.length; j++) {
                mapDetails[i][j] = new MapDetail();
                mapDetails[i][j].setYear(year[i]);
                mapDetails[i][j].setName(region[j]);
                List<MapDetail> children = getChildrenNameAndValue(i, region[j], valueTreeList);
                double value = 0;
                if (children != null) {
                    for (MapDetail tmpMap : children) {
                        value += tmpMap.getValue();
                    }
                }
                mapDetails[i][j].setValue(Math.round(value * 1000) / 1000.0);
                mapDetails[i][j].setChildren(children);

            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mapDetails", mapDetails);
        return jsonObject;

    }


    //统计各地区各年政策出台数量（废弃）
    private static JSONObject createMapData() {
        String[] year = {"2014", "2015", "2016", "2017", "2018"};
        String[] region = {"北京", "天津", "河北", "山西", "内蒙古", "辽宁", "吉林", "黑龙江", "上海", "江苏", "浙江", "安徽", "福建", "江西", "山东", "河南", "湖北", "湖南", "广东", "广西", "海南", "重庆", "四川", "贵州", "云南", "西藏", "陕西", "甘肃", "青海", "宁夏", "新疆", "台湾"};
        Map<String, Integer> yearMap = new HashMap<>();
        Map<String, Integer> regionMap = new HashMap<>();
        for (int i = 0; i < year.length; i++) {
            yearMap.put(year[i], i);
        }
        for (int i = 0; i < region.length; i++) {
            regionMap.put(region[i], i);
        }

        MapData[][] mapData = new MapData[year.length][region.length];
        for (int i = 0; i < year.length; i++) {
            for (int j = 0; j < region.length; j++) {
                mapData[i][j] = new MapData();
            }
        }
        sql = "SELECT o.REGION_NAME '地区',YEAR(p.pubdata) '年份' ,count(*) '数量' FROM policy p LEFT JOIN province_city o ON (p.type = o.REGION_ID)\n" +
                "WHERE p.type != 1 AND p.type LIKE '__0000' AND (YEAR(p.pubdata) = '2014' OR YEAR(p.pubdata) = '2015' OR YEAR(p.pubdata) = '2016' OR YEAR(p.pubdata) = '2017' OR YEAR(p.pubdata) = '2018')\n" +
                "GROUP BY o.REGION_NAME,YEAR(p.pubdata) ORDER BY o.REGION_ID,YEAR(p.pubdata)";
        db1 = new DBHelper(sql);//创建DBHelper对象
        try {
            ret = db1.pst.executeQuery();//执行语句，得到结果集
            while (ret.next()) {
                String tmpRegion = ret.getString(1).replaceAll("省|市|自治区|回族|维吾尔|壮族", "");
                String tmpYear = ret.getString(2);
                Double tmpNum = ret.getDouble(3);
                int yearIndex = yearMap.get(tmpYear);
                try {
                    int regionIndex = regionMap.get(tmpRegion);
                    mapData[yearIndex][regionIndex].setYear(tmpYear);
                    mapData[yearIndex][regionIndex].setName(tmpRegion);
                    mapData[yearIndex][regionIndex].setValue(tmpNum);
                } catch (NullPointerException e) {
                    System.out.println(tmpRegion);
                    e.getMessage();
                }

            }//显示数据
            ret.close();
            db1.close();//关闭连接
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JSONObject json = new JSONObject();
        json.put("mapData", mapData);
        return json;
    }

    /**
     * 根据已经赋值的指标树，返回年份、地域、值的数据格式（第一版 下钻一层，废弃）
     *
     * @param valueTreeList 已经赋值的指标树
     */
    private static JSONObject getYearRegionValue(List<EntityValueTree> valueTreeList) {
        String[] year = {"2009", "2010", "2011", "2012", "2013"};
        MapDataMore[][] mapData = new MapDataMore[year.length][region.length];
        for (int i = 0; i < year.length; i++) {
            for (int j = 0; j < region.length; j++) {
                mapData[i][j] = new MapDataMore();
                mapData[i][j].setYear(year[i]);
                mapData[i][j].setName(region[j]);
                JSONObject tmpJson = getMoreValue(i, region[j], valueTreeList);
                double value = 0;
                for (String key : tmpJson.keySet()) {
                    value += (double) tmpJson.get(key);
                }
                mapData[i][j].setValue(value * 2000);
                mapData[i][j].setMoreValue(tmpJson);
            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mapData", mapData);
        return jsonObject;

    }

    /**
     * 获取某一年一个地区的 各项指标（children）
     * 每一个指标都可能包含子指标；一个指标的value等于该指标的children的value之和乘以改指标的权重weight
     *
     * @param yearIndex     年份下标
     * @param region        地域名（简短）
     * @param valueTreeList 指标树和不同地区各年份的值
     * @return 某指标的children
     */
    private static List<MapDetail> getChildrenNameAndValue(int yearIndex, String region, List<EntityValueTree> valueTreeList) {
        if (valueTreeList != null) {
            List<MapDetail> children = new ArrayList<>();
            for (EntityValueTree tree : valueTreeList) {
                MapDetail child = new MapDetail();
                child.setName(tree.getName());
                if (tree.isHasChild()) {
                    child.setChildren(getChildrenNameAndValue(yearIndex, region, tree.getChildren()));
                    double value = 0;
                    if (child.getChildren() != null) {
                        for (MapDetail tmpMap : child.getChildren()) {
                            //有可能一个指标下没有数据值
                            if (tmpMap != null && tmpMap.getValue() != null) {
                                value += tmpMap.getValue();
                            }

                        }
                    }
                    //保留三位小数
                    child.setValue(Math.round(value * tree.getWeight() * 1000) / 1000.0);
                } else {
                    JSONObject value = tree.getValue();
                    double yearRegionValue = 0;
                    if (value != null) {
                        List<JSONObject> regionValuesList = (List<JSONObject>) value.get("series");
                        //有些指标只有少量城市有数据，为了公平，设置阈值5，当一个指标，有5个以上的城市有数据时才加入分析指标
                        if (regionValuesList.size() > 5) {
                            for (JSONObject tmpJson : regionValuesList) {
                                if (((String) tmpJson.get("name")).contains(region)) {
                                    String strYearRegionValue = ((String[]) tmpJson.get("data"))[yearIndex];
                                    if (strYearRegionValue != null && !"".equals(strYearRegionValue)) {
                                        yearRegionValue = Double.parseDouble(strYearRegionValue);
                                    }
                                    break;
                                }
                            }
                        }
                    }
                    //比重在原来的基础上扩大1W倍，为了更好的展示效果
                    child.setValue(Math.round(yearRegionValue * tree.getWeight() * 10000000) / 1000.0);
                    child.setChildren(null);
                }
                children.add(child);
            }
            return children;
        } else {
            return null;
        }
    }

    /**
     * 获取某一年份某一地域的指标树（第一版 下钻一层）
     *
     * @param yearIndex     年份下标
     * @param region        地域名（简短）
     * @param valueTreeList 指标树和不同地区各年份的值
     * @return 详细计算了权值的详细值
     */
    private static JSONObject getMoreValue(int yearIndex, String region, List<EntityValueTree> valueTreeList) {
        JSONObject moreValue = new JSONObject();
        for (EntityValueTree tree : valueTreeList) {
            moreValue.put(tree.getName(), getScore(yearIndex, region, tree.getChildren()));
        }
        return moreValue;
    }

    /**
     * 负责求出一个父亲指标的score（第一版 下钻一层）
     * 父亲指标的score等于儿子指标score的和
     * 这个valueTreeList为一个父亲指标的儿子指标集合
     * 儿子指标score等于儿子的儿子的score的和
     *
     * @param yearIndex     年份下标
     * @param region        地区名（简短）
     * @param valueTreeList 一个父亲指标的儿子指标集合
     * @return 父亲指标的score
     */
    private static double getScore(int yearIndex, String region, List<EntityValueTree> valueTreeList) {
        if (valueTreeList.get(0).isHasChild()) {
            double value = 0;
            for (EntityValueTree childNode : valueTreeList) {
                value += getScore(yearIndex, region, childNode.getChildren());
            }
            return value;
        } else {
            double fatherValue = 0;
            //父亲节点的score等于儿子节点的score乘以各自权重的和
            for (EntityValueTree childNode : valueTreeList) {
                double yearRegionValue = 0;
                JSONObject value = childNode.getValue();

                if (value != null) {
                    List<JSONObject> regionValuesList = (List<JSONObject>) value.get("series");
                    if (regionValuesList.size() > 5) {
                        for (JSONObject tmpJson : regionValuesList) {
                            if (((String) tmpJson.get("name")).contains(region)) {
                                String strYearRegionValue = ((String[]) tmpJson.get("data"))[yearIndex];
                                if (strYearRegionValue != null && !"".equals(strYearRegionValue)) {
                                    yearRegionValue = Double.parseDouble(strYearRegionValue);
                                }
                                break;
                            }
                        }
                        fatherValue += (yearRegionValue * childNode.getWeight());
                    }

                }

            }
            return fatherValue;
        }

    }


    /*
     * *******************工具方法*************************************
     *
     */

    /**
     * 格式化一个指标不同地区各年度数据---针对实体指标的设计
     *
     * @return json
     */
    private static JSONObject getOneGroupJsonData(List<YearBookData> yearBookDataList) {
        if (yearBookDataList == null || yearBookDataList.size() < 1) {
            return null;
        }
        //清洗去重
        List<YearBookData> newYearBookDataList = new ArrayList<>();

        YearBookData compareWith = yearBookDataList.get(0);
        String unitC = yearBookDataList.get(yearBookDataList.size() - 1).getUnitC();
        if (unitC == null || "".equals(unitC.trim())) {
            unitC = yearBookDataList.get(0).getUnitC();
        }
        newYearBookDataList.add(compareWith);
        double nowValue = 0;
        List<String> unifyRegionName = new ArrayList<>();
        for (YearBookData tmpBookData : yearBookDataList) {
            //统一单位
            String nowUnitC = tmpBookData.getUnitC();
            if (!unitC.equals(nowUnitC)) {
//                System.out.println("化单位：" + nowUnitC + "  " + unitC);

                //判断哪个单位大
                //首先判断当前的单位不是null，如果不是null则统一单位，是null，则根据值的大小设置单位
                //目前可以处理的单位：十、百、...万...千亿，（元、个、件）
                if (nowUnitC != null && !"".equals(nowUnitC.trim())) {
                    try {
                        nowValue = Double.parseDouble(tmpBookData.getValue());
                    } catch (Exception e) {
                        e.getMessage();
                    }
                    int compareResult = compareUnit(nowUnitC, unitC);
                    if (compareResult > 0) {
                        //说明当前的单位大于选取的单位，需要调整当前结果的数值为选取单位对应的数值
                        tmpBookData.setValue(String.valueOf(Math.round(nowValue * Math.pow(10, compareResult))));
                    } else if (compareResult < 0) {
                        tmpBookData.setValue(String.format("%.2f", nowValue / Math.pow(10, (-1) * compareResult)));
                    }
                }
                //else 当前的单位是null(小概率事件) 根据数量级大小很难把控，因此暂时不做处理

                tmpBookData.setUnitC(unitC);
            }
            //标准化地域名称  如北京  北京市
            String oldRegion = tmpBookData.getRegion();
            if (!unifyRegionName.contains(oldRegion)) {
                unifyRegionName.add(oldRegion);
            }

            //去重，
            if (!tmpBookData.equals(compareWith)) {
                newYearBookDataList.add(tmpBookData);
                compareWith = tmpBookData;
            }

        }
        //分组

        JSONObject json = new JSONObject();
        //进行数据补齐
        String[] years = {"2009", "2010", "2011", "2012", "2013"};
        for (YearBookData result : newYearBookDataList) {
            String oldRegion = result.getRegion();
            for (String tmpRegion : unifyRegionName) {
                if (!tmpRegion.equals(oldRegion) && tmpRegion.contains(oldRegion)) {
                    result.setRegion(tmpRegion);
                }
            }
        }
        //resultMap: 某地区，各年份的值。
        Map<String, String[]> resultMap = new HashMap<>();
        for (YearBookData result : newYearBookDataList) {
            if (resultMap.containsKey(result.getRegion())) {
                String[] numList = resultMap.get(result.getRegion());
                if (result.getYears() != null) {
                    numList[getIndex(years, result.getYears())] = result.getValue();
                    numList[getIndex(years, result.getYears())] = result.getValue();
                }

            } else {
                if (result.getYears() != null) {
                    String[] numList = new String[years.length];
                    numList[getIndex(years, result.getYears())] = result.getValue();
                    resultMap.put(result.getRegion(), numList);
                }
            }

        }
        //先把各年的和求出来  list
        double[] sumList = new double[years.length];
        for (int i = 0; i < years.length; i++) {
            double tmpSum = 0;
            for (String key : resultMap.keySet()) {
                String[] tmpList = resultMap.get(key);
                if (tmpList != null && tmpList[i] != null) {
                    tmpSum += Double.parseDouble(tmpList[i]);
                }
            }
            sumList[i] = tmpSum;
        }

        //再遍历 除各年的和，求占比
        for (String key : resultMap.keySet()) {
            String[] tmpList = resultMap.get(key);
            if (tmpList != null) {
                for (int i = 0; i < tmpList.length; i++) {
                    if (tmpList[i] != null) {
                        double doubleValue = Double.parseDouble(tmpList[i]);
                        tmpList[i] = String.format("%.5f", doubleValue / sumList[i]);
                    }
                }
            }
        }

        List<JSONObject> jsonObjectList = new ArrayList<>();
        for (String key : resultMap.keySet()) {
            JSONObject tmpJson = new JSONObject();
            tmpJson.put("name", key);
            tmpJson.put("data", resultMap.get(key));
            jsonObjectList.add(tmpJson);
        }
        json.put("chartName", newYearBookDataList.get(0).getName());
        json.put("unit_c", newYearBookDataList.get(0).getUnitC());
        json.put("categories", years);
        json.put("series", jsonObjectList);
        return json;
    }

    /**
     * 计算常见单位大小
     *
     * @param unit1 单位1
     * @param unit2 单位2
     * @return 第一个单位大返回正数、等于返回0 、小于返回负数
     */
    private static int compareUnit(String unit1, String unit2) {
        String[] UNIT = {"十", "百", "千", "万", "十万", "百万", "千万", "亿", "十亿", "百亿", "千亿"};
        if (unit1 == null || unit2 == null) {
            return 0;
        }
        int unit1Index = -1;
        int unit2Index = -1;
        unit1 = unit1.replaceAll("人|元|件", "");
        unit2 = unit2.replaceAll("人|元|件", "");

        for (int i = 0; i < UNIT.length; i++) {
            if (unit1.equals(UNIT[i])) {
                unit1Index = i;
            }
            if (unit2.equals(UNIT[i])) {
                unit2Index = i;
            }
        }
        return unit1Index - unit2Index;
    }

    private static int getIndex(String[] arr, String value) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(value)) {
                return i;
            }
        }
        return -1;//如果未找到返回-1
    }
}
