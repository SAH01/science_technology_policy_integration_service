package cc.mrbird.febs.policy.helper;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.policy.utils.CreateFileUtil;
import cc.mrbird.febs.policy.utils.DBHelper;
import com.alibaba.fastjson.JSONObject;
import com.hankcs.hanlp.mining.cluster.ClusterAnalyzer;
import lombok.Data;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
class KeySentence {
    private int type;
    private String name;
    private String pubDate;
    private String organ;
    private String sentence;
}

public class GetRelatedSentenceByFormula {

    static String sql = null;
    static DBHelper db1 = null;
    static ResultSet ret = null;

    public static void main(String[] args) throws FebsException {
//        GetRelatedSentenceByFormula("2010-11-01","2019-12-01","16");
//        GetRelatedSentenceByFormula(null,null,"16");
//        GetRelatedSentenceByFormula(" ","","16");
//        GetRelatedSentenceByFormula("2010-11-01","2019-12-01","18");
//        GetRelatedSentenceByFormula("","","18");
//        GetRelatedSentenceByFormula("2010-11-01","2019-12-01","24");
//        GetRelatedSentenceByFormula("2010-11-01","2019-12-01","23");
        JSONObject json = GetRelatedSentenceByFormula("2010-11-01", "2019-12-01", "26");
        CreateFileUtil.createJsonFile(json.toString(), "json_for_GetRelatedSentenceByFormula", "Sentence1");

//        GetRelatedSentenceByFormula("2010-11-01","2019-12-01","38");
        /*String text1="（二）构建专业科技服务新优势\n" +
                "1.技术转移。鼓励技术转移机构创新服务模式，为企业提供跨领域、跨区域、全过程的技术转移集成服务，推动技术转移服务机构从“点对点”服务向综合服务模式升级。支持从事技术交易、技术评估、技术投融资等活动的技术转移服务机构发展，完善专业化、市场化、国际化的技术转移服务体系。支持科技服务机构面向军民科技融合开展综合服务，推进“军技民用、民品进军”。加强与中国技术交易所、中国国际技术转移中心、天津北方技术交易市场等互联互通，形成“京津研发、廊坊孵化转化产业化”新模式。\n" +
                "2.科技金融。依托河北省科技金融服务平台，为科技型企业融资、并购、重组、改制、上市提供个性化服务。强化科技金融服务产品创新，鼓励金融机构探索知识产权质押贷款、股权质押贷款、信用贷款、科技保险、产业链融资等新型融资服务。支持天使投资、创业投资等股权投资对科技企业进行投资和增值服务，探索投贷结合的融资模式。支持民营企业通过股权、债权及证券化融资，支持发展潜力好但尚未盈利的创新型企业上市或在新三板、科创板挂牌。探索设立廊坊市科技成果转化基金、天使基金，试行混合所有制的市场化基金管理模式。\n" +
                "3.知识产权。加快发展知识产权代理、法律、信息等基础服务，大力发展知识产权评估、价值分析、交易、转化、投融资、运营、托管、商用化、咨询等高附加值服务。大力扶持《企业知识产权管理规范》贯标认证企业、专利导航评议企业、国家级知识产权优势示范企业，支持企业运用专利开展质押贷款、作价入股和投保专利等金融项目；培育一批知识产权保护规范化专业市场，建立一批知识产权维权援助分中心、工作站，扶持一批知识产权交易平台和品牌服务机构，建设一批知识产权培训基地。强化知识产权创造、运用、保护、管理和服务。鼓励社会资本投资设立知识产权运营公司，开展知识产权收储、开发、组合、投资等服务，探索开展知识产权证券化业务，盘活知识产权资产。探索知识产权交易与综合服务线上线下融合新模式，打造一站式的知识产权交易、评估、认证等创新服务。\n" +
                "4.检验检测。以集聚服务主体、延伸服务链条为重点，推动检验检测认证服务由单一的测试、分析和认证，向全产业链、产品全生命周期的创新检测技术集成延伸。积极推进国有检验检测认证机构转企改制，着力集聚一批国内外第三方检验检测认证机构，推动形成面向全国、多元并存、错位发展、覆盖全面的检验检测认证服务体系。支持检验检测认证机构积极参与地方、行业、国家及国际标准制订，推进资质标准和检验检测结果互认。\n" +
                "5.科技咨询。支持发展战略咨询、管理咨询、工程咨询、信息咨询等专业化服务，积极培育管理服务外包、项目管理外包等新业态。鼓励科技咨询机构开展数据存储、分析、挖掘和可视化技术研究，加强行业数据库、知识库建设，探索运用新技术、新方法、新模型，开展网络化、集成化的科技咨询和知识服务。加快全市高端智库建设，为经济社会发展提供高质量咨询服务。\n" +
                "（三）培育跨界融合新业态\n" +
                "1.数字科技服务。加强大数据、云计算、人工智能等新技术与科技服务业的融合应用，探索发展线上线下融合服务（O2O）、第三方云平台服务、特种定制服务、一站式集成服务等新业态。围绕工具、方法、标准、基础数据库等，加快构建科技服务共性技术支撑体系和标准规范体系，支撑科技服务业数字化发展。\n" +
                "2.平台化服务。加快引进和培育平台型科技服务机构，整合相关科技服务资源，实现综合科技服务供需精准匹配。支持科技服务机构聚焦优势领域，参与或主导建设基于互联网、大数据等新技术应用的第三方、第四方科技服务平台。";
        String formula1 = "技术转移、知识产权";
        List<String> sequenceList = getSequenceList(text1,formula1);
        for (String seq:sequenceList) {
            System.out.println(seq);
        }*/
    }

    public static JSONObject GetRelatedSentenceByFormula(String createTimeFrom, String createTimeTo, String formulaId) throws FebsException {
        sql = "SELECT FORMULA_CONTENT '公式内容' FROM policy_analysis_formula WHERE FORMULA_ID = '" + formulaId + "'";
        db1 = new DBHelper(sql);//创建DBHelper对象
        String formulaContent = null;
        try {
            ret = db1.pst.executeQuery();//执行语句，得到结果集
            if (ret.next()) {
                formulaContent = ret.getString(1);
            }//显示数据
            ret.close();
            db1.close();//关闭连接
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        System.out.println(formulaContent);
        String[] segmentation = formulaContent.split("-");
        //获取公式中的关键词，作为返回值的一部分
        List<String> formulaKeywords = new ArrayList<>();
        for (String temp1 : segmentation) {
            for (String temp2 : temp1.split("、")) {
                if (!"".equals(temp2.trim())) {
                    formulaKeywords.add(temp2.trim());
                }
            }
        }
        String textMiningSQL = "SELECT `name` '政策名称',pubdata '发布时间',organ '发布机构',text FROM policy_old WHERE 1 = 2 ";
        //由于 StringBuilder 相较于 StringBuffer 有速度优势，所以多数情况下建议使用 StringBuilder 类。然而在应用程序要求线程安全的情况下，则必须使用 StringBuffer 类。此处没有多线程。
        StringBuilder appendSQL = new StringBuilder("OR ");
        if (createTimeFrom != null && !"".equals(createTimeFrom.trim()) && createTimeTo != null && !"".equals(createTimeTo.trim())) {
            appendSQL.append("(pubdata > '").append(createTimeFrom).append("' AND pubdata < '").append(createTimeTo).append("' ) AND ");
        }
        //只有关键词  不包含主语
        if (segmentation.length == 1) {
            String[] keywords = formulaContent.split("、");
            appendSQL.append("(`text` LIKE '%").append(keywords[0]).append("%' ");
            if (keywords.length > 1) {
                for (int i = 1; i < keywords.length; i++) {
                    appendSQL.append("OR text LIKE '%").append(keywords[i]).append("%' ");
                }
            }
            appendSQL.append(") ");
        } else if (segmentation.length == 2) {
            String[] frontPartOfContent = segmentation[0].split("、");
            String[] rearPartOfContent = segmentation[1].split("、");
            appendSQL.append("(`text` LIKE '%").append(frontPartOfContent[0]).append("%").append(rearPartOfContent[0]).append("%' ");
            if (rearPartOfContent.length > 1) {
                for (int i = 1; i < rearPartOfContent.length; i++) {
                    appendSQL.append("OR text LIKE '%").append(frontPartOfContent[0]).append("%").append(rearPartOfContent[i]).append("%' ");
                }
            }
            if (frontPartOfContent.length > 1) {
                for (int i = 1; i < frontPartOfContent.length; i++) {
                    for (int j = 0; j < rearPartOfContent.length; j++) {
                        appendSQL.append("OR text LIKE '%").append(frontPartOfContent[i]).append("%").append(rearPartOfContent[j]).append("%' ");
                    }
                }
            }
            appendSQL.append(") ");
        } else {
            throw new FebsException("公式错误");
        }
        appendSQL.append("ORDER BY pubdata DESC");
        //最终生成的SQL
        String finalSQL = new StringBuilder(textMiningSQL).append(appendSQL).toString();
        System.out.println(finalSQL);

        List<KeySentence> sentencesList = new ArrayList<>();
        ClusterAnalyzer<String> analyzer = new ClusterAnalyzer<String>();
        //时间格式化
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        db1 = new DBHelper(finalSQL);//创建DBHelper对象
        try {
            ret = db1.pst.executeQuery();//执行语句，得到结果集
            while (ret.next()) {
                String name = ret.getString(1);
                Date pubDate = ret.getDate(2);
                String organ = ret.getString(3);
                String text = ret.getString(4);
                for (String sequence : getSequenceList(text, formulaKeywords)) {
                    KeySentence keySentence = new KeySentence();
                    keySentence.setName(name);
                    keySentence.setPubDate(simpleDateFormat.format(pubDate));
                    keySentence.setOrgan(organ);
                    keySentence.setSentence(sequence);
                    sentencesList.add(keySentence);
                    analyzer.addDocument(sequence, sequence);
                }
            }//显示数据
            ret.close();
            db1.close();//关闭连接
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //按分类结果分组的句子信息结果集
        List<KeySentence>[] keySentenceList = null;

        //获得句子聚类的结果
        List<Set<String>> clusteringSentences;
        final int TYPES = 3;
        try {
            clusteringSentences = analyzer.repeatedBisection(TYPES);
            keySentenceList=new ArrayList[clusteringSentences.size()];
            for(int i=0;i<clusteringSentences.size();i++){
                keySentenceList[i]=new ArrayList<>();
            }
            for (KeySentence keySentence : sentencesList) {
                for (int i = 0; i < clusteringSentences.size(); i++) {
                    if (clusteringSentences.get(i).contains(keySentence.getSentence())) {
                        keySentence.setType(i);
                        keySentenceList[i].add(keySentence);
                        System.out.println(i + "\t" + keySentence.getSentence());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("没有找到满足该公式的政策。");
            e.getMessage();
        }

        //把所有的句子信息放到一个集合里，返回最展示，按照类别和时间排序
        sentencesList.clear();
        if(keySentenceList!=null){
            for(int i=0;i<keySentenceList.length;i++){
                for (KeySentence keySentence : keySentenceList[i]) {
                    sentencesList.add(keySentence);
                }
            }
        }
        JSONObject json = new JSONObject();
        json.put("total", sentencesList.size());
        json.put("rows", sentencesList);
        json.put("formulaKeywords", formulaKeywords);
        json.put("formulaContent", formulaContent);
        json.put("types", TYPES);
        return json;
    }

    private static List<String> getSequenceList(String text, List<String> formulaKeywords) {
        List<String> containKeywordSequenceList = new ArrayList<>();

        String[] sequenceList = text.split("。|；|\n");
        for (String sequence : sequenceList) {
            boolean canAdd = false;
            for (String keyword : formulaKeywords) {
                if (sequence.contains(keyword) && sequence.length() > 2 * keyword.length()) {
                    canAdd = true;
                    break;
                }
            }
            if (canAdd) {
                containKeywordSequenceList.add(sequence);
            }
        }
        return containKeywordSequenceList;
    }
}
