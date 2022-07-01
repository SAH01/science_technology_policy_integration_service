package cc.mrbird.febs.policy.helper;

//import cc.mrbird.febs.policy.entity.Fieldlevelthree;
//import cc.mrbird.febs.policy.entity.Fieldleveltwo;

import cc.mrbird.febs.policy.utils.Levenshtein;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class Policydafen2 {
//    public static List<Fieldleveltwo> getDafenKeywords() {
//        List<String> list = new ArrayList<>();
//        //科技服务业各领域*******************************
//        //研究开发
//        String ResearchDevelopment = "工业设计,研发服务,研发设计,研究开发";
//        list.add(ResearchDevelopment);
//        //技术转移
//        String TechnologyTransfer = "成果转化,技术服务,技术交易,技术市场,技术转移";
//        list.add(TechnologyTransfer);
//        //创业孵化
//        String BusinessIncubation = "创业孵化,孵化器,孵化基地";
//        list.add(BusinessIncubation);
//        //检测认证
//        String DetectionAuthentication = "检验检测认证,测试,计量";
//        list.add(DetectionAuthentication);
//        //知识产权
//        String IntellectualProperty = "知识产权保护,知识产权服务,知识产权管理,知识产权交易,知识产权运营";
//        list.add(IntellectualProperty);
//        //科技咨询
//        String STConsultation = "科技咨询,管理咨询,技术咨询,科技评估";
//        list.add(STConsultation);
//        // 科技金融
//        String STFinance = "科技金融,质押融资,产权融资,股权融资";
//        list.add(STFinance);
//        // 科学技术普及
//        String STUniversal = "科普,科学技术普及";
//        list.add(STUniversal);
//        // 综合科技服务
//        String STComprehensive = "科技中介,增值服务,中介服务";
//        list.add(STComprehensive);
//
//        //政策对象*******************************
//        // 产业
//        String Industry = "产业集群,高技术服务业,战略性新兴产业";
//        list.add(Industry);
//        // 企业
//        String Enterprise = "高新技术企业,科技企业,微企业,中小企业";
//        list.add(Enterprise);
//        // 服务机构
//        String ServiceInstitutions = "生产力促进中心,服务机构,众创空间,行业协会";
//        list.add(ServiceInstitutions);
//        // 科研院所高校
//        String ScientificResearchInstitutes = "高校,科研院所";
//        list.add(ScientificResearchInstitutes);
//
//        //企业发展*******************************
//        // 并购、上市
//        String MergerListing = "并购重组,上市";
//        list.add(MergerListing);
//        // 投资
//        String Investment = "创业投资,创业风险投资,天使投资,民间资本";
//        list.add(Investment);
//        // 企业产权
//        String EnterprisePropertyRight = "品牌,专利";
//        list.add(Enterprise);
//
//        //政策措施*******************************
//        // 产业发展
//        String IndustrialDevelopment = "合作,国际化,集聚发展,人才培养,人才引进,市场化";
//        list.add(IndustrialDevelopment);
//        // 硬件环境
//        String HardwareEnvironment = "服务平台,平台建设,基地建设";
//        list.add(HardwareEnvironment);
//        // 公共服务环境
//        String PublicService = "公共服务,信息服务";
//        list.add(PublicService);
//        // 创新发展
//        String InnovateDevelopment = "技术创新,科技创新,自主创新";
//        list.add(InnovateDevelopment);
//        // 服务能力类
//        String ServiceCapability = "服务标准,服务能力,服务体系,专业化";
//        list.add(ServiceCapability);
//        // 制度措施
//        String InstitutionalMeasures = "政策扶持,政策环境,政策体系,制度";
//        list.add(InstitutionalMeasures);
//        // 行政措施
//        String AdministrationMeasures = "监管,市场准入";
//        list.add(AdministrationMeasures);
//        // 财税措施
//        String FinanceTaxMeasures = "财政资金,引导基金,专项资金,税收政策,税收优惠";
//        list.add(FinanceTaxMeasures);
//        // 政府购买与服务外包
//        String ServiceOutsourcing = "政府购买,政府采购,服务外包";
//        list.add(ServiceOutsourcing);
//        int i = 1;
//        List<Fieldleveltwo> fieldleveltwos = new ArrayList<>();
//        for (String key : list) {
//            Fieldleveltwo fieldleveltwo = new Fieldleveltwo();
//            fieldleveltwo.setId(i++);
//            fieldleveltwo.setKeywords(key);
//            fieldleveltwos.add(fieldleveltwo);
//        }
//        return fieldleveltwos;
//    }
//
//    /**
//     * 根据二级领域设置好分类的关键词，对一个关键词list中的每一个关键词分类
//     * 最终的目的是把关键词存到fieldlevelthree中，以关键词，出现次数的形势
//     *
//     * @param fieldleveltwoList 二级领域设置好分类的关键词
//     * @param keywords          关键词列表list
//     */
//    public static List<Fieldlevelthree> classifyKeywordToFieldType(List<Fieldleveltwo> fieldleveltwoList, String[] keywords) {
//        List<Fieldlevelthree> fieldlevelthreeList = new ArrayList<>();
//        Levenshtein lt = new Levenshtein();
//
//        for (String fromCompareWord : keywords) {
//
//            //一个关键词在第二级领域分类中各类目的得分
//            Map<Integer, Float> eatchTypeScore = new HashMap<>();
//            //遍历二级领域分类
//            for (Fieldleveltwo fieldleveltwo : fieldleveltwoList) {
//                float fieldLevelTowCcore = 0;
//                float temp = 0;
//                for (String toCompareWord : fieldleveltwo.getKeywords().split(",")) {
//                    temp = lt.getSimilarityRatio(fromCompareWord, toCompareWord);
//                    fieldLevelTowCcore += temp;
////                    System.out.println(fromCompareWord+"和"+toCompareWord+"相似度"+temp);
//                }
////                System.out.println(fromCompareWord+"在类目"+fieldleveltwo.getId()+"中总得分"+fieldLevelTowCcore);
//                eatchTypeScore.put(fieldleveltwo.getId(), fieldLevelTowCcore);
//            }
//            int maxKey = getMaxValueKey(eatchTypeScore);
//            if (maxKey == -1)
//                maxKey = 18;
//            Fieldlevelthree fieldlevelthree = new Fieldlevelthree();
//            fieldlevelthree.setParentId(maxKey);
//            fieldlevelthree.setWord(fromCompareWord);
//            fieldlevelthree.setFrequent(1);
//            fieldlevelthreeList.add(fieldlevelthree);
//        }
//        if (keywords.length != fieldlevelthreeList.size()) {
//            System.out.println("********************************ERROR**********************************");
//            System.out.println(keywords.length + "********" + fieldlevelthreeList.size());
//        }
//
//        return fieldlevelthreeList;
//    }

    //教育培训
    private final static String[] instrument1_1 = {
            "教育培训", "人才引进", "人才培养", "国际交往", "高中级管理人员", "技术人员", "教育资源优势", "高等院校", "中等专科学校", "扩大招生规模", "多层次培养"
            , "高级软件人才", "培养规模", "软件人才", "硕士、博士、博士后", "理工科院校", "培养复合型人才", "成人教育", "业余教育", "专业教学", "技术培训", "知识更新"
            , "再教育", "现代远程教育", "应用知识", "专项基金", "出国进修", "聘请外国专家", "来华讲学", "全球化人才战略", "高端人才培养计划", "实务人才", "人才支撑", "培育工作"

    };
    //科技信息支持
    private final static String[] instrument1_2 = {
            "科技信息支持", "信息运用", "统计制度", "信息交流与共享", "状况信息", "制度信息", "专利数据库", "动态信息", "技术动向", "追踪市场竞争", "定期发布"
    };
    //科技基础设施建设
    private final static String[] instrument1_3 = {
            "科技基础设施建设", "基本建设", "基础设施建设", "软件园区", "基础软件开发", "孵化", "平台建设", "服务平台", "基地建设", "孵化器", "众创空间"
    };
    //科技资金投入
    private final static String[] instrument1_4 = {
            "科技资金投入", "开办资金", "建设资金", "财政资金", "专项资金", "引导基金", "", "发展资金"
    };
    //公共服务
    private final static String[] instrument1_5 = {
            "公共服务", "服务标准", "服务规范", "服务内容", "服务特点", "服务体系", "服务机构", "服务资质管理", "维权援助", "管理咨询", "维权援助", "服务平台"
    };
    //目标规划
    private final static String[] instrument2_1 = {
            "目标规划", "进一步", "促进", "达到或接近", "先进水平", "发展", "产业发展", "力争", "缩小差距", "满足", "努力", "开拓市场"
    };
    //金融支持
    private final static String[] instrument2_2 = {
            "金融支持", "创业投资", "天使投资", "民间资本", "科技金融", "质押融资", "风险投资", "种子资金", "投资机制", "证券", "外汇", "资金投入"
    };
    //税收优惠
    private final static String[] instrument2_3 = {
            "税收优惠", "增值税", "法定税率", "企业所得税", "免税优惠", "即征即退", "财税支持政策", "资金保障"
    };
    //知识产权保护
    private final static String[] instrument2_4 = {
            "知识产权保护", "知识产权服务", "知识产权管理", "知识产权交易", "知识产权运营", "打击走私", "著作权人", "合法权益", "重点保护", "未经授权许可", "打击盗版", "法律保护"
    };
    //法规管制
    private final static String[] instrument2_5 = {
            "法规管制", "监管", "政策扶持", "政策环境", "重点支持", "基础性", "法律法规", "收入分配", "重奖", "作价入股", "折股分配", "认定标准", "根据规定", "配偶及未成年子女",
            "年审制度", "审核", "批准", "公布", "国家标准", "监督", "行业管理", "管理", "健康发展", "市场调查", "信息交流", "咨询评估",
            "行业自律", "知识产权保护", "资质认定", "政策研究", "支持", "公开", "公正", "公平", "企业认定", "统计", "企业", "执行", "鼓励", "按照"
    };
    //公共技术采购
    private final static String[] instrument3_1 = {
            "公共技术采购", "政府采购", "政府机构购买", "购买", "采用国产", "经费", "单独的预算"
    };
    //外包
    private final static String[] instrument3_2 = {
            "外包", "服务外包", "承担", "企业承担", "预算", "经费"
    };
    //贸易管制
    private final static String[] instrument3_3 = {
            "贸易管制", "市场准入", "免税", "进口商品", "外商投资项目", "进口技术", "出口", "信贷支持", "出口信用保险", "自营出口权", "年出口额", "保税措施", "贸易特点"
            , "外汇管理办法", "交易", "外贸", "海关", "外汇", "国际商务活动", "海关总署", "提供通关便利", "进口", "免征关税", "进口环节增值税", "外经贸部", "引进"
            , "外商投资产业", "优惠暂定税率"
    };
    //海外机构管理
    private final static String[] instrument3_4 = {
            "海外机构管理", "国外部署", "全球研发外包", "国外申请专利", "设立研发机构", "联合研发", "国际合作", "国外获取知识产权", "国外运营", "运营公司"
            , "境外注册商标", "培育国际知名商标", "国外推广应用", "国际标准制定", "维权援助", "对外投资目的地", "主要贸易目的地", "国外服务机构"
    };
    private static String[] rangeOfServices32 = {"工业设计", "研发服务", "研发设计", "研究开发", "成果转化", "技术服务", "技术交易", "技术市场", "技术转移", "创业孵化", "孵化器", "孵化基地", "检验检测认证", "测试", "计量", "知识产权保护", "知识产权服务", "知识产权管理", "知识产权交易", "知识产权运营", "科技咨询", "管理咨询", "技术咨询", "科技评估", "科技金融", "质押融资", "产权融资", "股权融资", "科普", "科学技术普及", "科技中介", "增值服务", "中介服务"};
    private static String[] policyObject12 = {"产业集群", "高技术服务业", "战略性新兴产业", "高新技术企业", "科技企业", "微企业", "中小企业", "生产力促进中心", "服务机构", "众创空间", "行业协会", "高校", "科研院所"};
    private static String[] enterpriseDevelopment7 = {"并购重组", "上市", "创业投资", "创业风险投资", "天使投资", "民间资本", "品牌", "专利"};
    private static String[] policyMeasure31 = {"合作", "国际化", "集聚发展", "人才培养", "人才引进", "市场化", "服务平台", "平台建设", "基地建设", "公共服务", "信息服务", "技术创新", "科技创新", "自主创新", "服务标准", "服务能力", "服务体系", "专业化", "政策扶持", "政策环境", "政策体系", "制度", "监管", "市场准入", "财政资金", "引导基金", "专项资金", "税收政策", "税收优惠", "政府购买", "政府采购", "服务外包"};

    //获取值最大的key
    private static int getMaxValueKey(Map<Integer, Float> map) {
        Collection<Float> c = map.values();
        Object[] obj = c.toArray();
        Arrays.sort(obj);
        Float maxValue = (Float) obj[obj.length - 1];
        if (maxValue <= 0)
            return -1;
        for (Map.Entry<Integer, Float> entry : map.entrySet()) {
            if (maxValue == entry.getValue()) {
                return entry.getKey();
            }
        }
        return -1;
    }

    //求某个关键词所属一级分类
    public static int getTypeForKeyword(String keyword) {
        //一个关键词在第二级领域分类中各类目的得分
        Levenshtein lt = new Levenshtein();
        float rangeOfServices = 0.0f;
        for (String toCompareWord : rangeOfServices32) {
            rangeOfServices += lt.getSimilarityRatio(keyword, toCompareWord);
        }
        rangeOfServices /= 32;

        float policyObject = 0.0f;
        for (String toCompareWord : policyObject12) {
            policyObject += lt.getSimilarityRatio(keyword, toCompareWord);
        }
        policyObject /= 12;

        float enterpriseDevelopment = 0.0f;
        for (String toCompareWord : enterpriseDevelopment7) {
            enterpriseDevelopment += lt.getSimilarityRatio(keyword, toCompareWord);
        }
        enterpriseDevelopment /= 7;

        float policyMeasure = 0.0f;
        for (String toCompareWord : policyMeasure31) {
            policyMeasure += lt.getSimilarityRatio(keyword, toCompareWord);
        }
        policyMeasure /= 31;

        int type = 3;
        float max = policyMeasure;
        if (rangeOfServices > max) {
            max = rangeOfServices;
            type = 0;
        }
        if (policyObject > max) {
            max = policyObject;
            type = 1;
        }
        if (enterpriseDevelopment > max) {
//            max = enterpriseDevelopment;
            type = 2;
        }
        return type;
    }

    public static void main(String[] args) {
//        String keyword = "科技企业,科技,孵化器,成果转化,人才引进,上市,印发,中华人民共和国";
//        String[] keywords = keyword.split(",");
//        for (String three :keywords) {
//            System.out.println(getTypeForKeyword(three));
//        }

        /*Map<String, Integer> wordFrequentMap = new HashMap<>();
        wordFrequentMap.put("A", 1);
        wordFrequentMap.put("D", 6);
        wordFrequentMap.put("E", 3);
        List<Map.Entry<String, Integer>> list = new ArrayList<>(wordFrequentMap.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            //升序排序
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        for (Map.Entry<String, Integer> e : list) {
            System.out.println(e.getKey() + ":" + e.getValue());
        }*/

//       System.out.println (getInstrumentTypeOfKeyword("公共服务"));
//       System.out.println (getInstrumentTypeOfKeyword("人才评价"));
//       System.out.println (getInstrumentTypeOfKeyword("平台建设"));
//       System.out.println (getInstrumentTypeOfKeyword("法规管制"));
//       System.out.println (getInstrumentTypeOfKeyword("政策扶持"));
//        double[] scores = new double[14];
//        for (double s : scores) {
//            System.out.println(s);
//        }
        //农村,培训,机构
        System.out.println(getInstrumentTypeOfKeyword("培训"));
    }

    //获取一个单词的分类
    public static int getInstrumentTypeOfKeyword(String keyword) {
        Levenshtein lt = new Levenshtein();
        double[] scores = new double[14];
        //计算每种分类中的得分
        {
            for (String toCompareWord : instrument1_1) {
                scores[0] += lt.getSimilarityRatio(keyword, toCompareWord);
            }
            for (String toCompareWord : instrument1_2) {
                scores[1] += lt.getSimilarityRatio(keyword, toCompareWord);
            }
            for (String toCompareWord : instrument1_3) {
                scores[2] += lt.getSimilarityRatio(keyword, toCompareWord);
            }
            for (String toCompareWord : instrument1_4) {
                scores[3] += lt.getSimilarityRatio(keyword, toCompareWord);
            }
            for (String toCompareWord : instrument1_5) {
                scores[4] += lt.getSimilarityRatio(keyword, toCompareWord);
            }
            for (String toCompareWord : instrument2_1) {
                scores[5] += lt.getSimilarityRatio(keyword, toCompareWord);
            }
            for (String toCompareWord : instrument2_2) {
                scores[6] += lt.getSimilarityRatio(keyword, toCompareWord);
            }
            for (String toCompareWord : instrument2_3) {
                scores[7] += lt.getSimilarityRatio(keyword, toCompareWord);
            }
            for (String toCompareWord : instrument2_4) {
                scores[8] += lt.getSimilarityRatio(keyword, toCompareWord);
            }
            for (String toCompareWord : instrument2_5) {
                scores[9] += lt.getSimilarityRatio(keyword, toCompareWord);
            }
            for (String toCompareWord : instrument3_1) {
                scores[10] += lt.getSimilarityRatio(keyword, toCompareWord);
            }
            for (String toCompareWord : instrument3_2) {
                scores[11] += lt.getSimilarityRatio(keyword, toCompareWord);
            }
            for (String toCompareWord : instrument3_3) {
                scores[12] += lt.getSimilarityRatio(keyword, toCompareWord);
            }
            for (String toCompareWord : instrument3_4) {
                scores[13] += lt.getSimilarityRatio(keyword, toCompareWord);
            }
        }
        int maxIndex =0;
        double max = scores[0];
        for(int i=0;i<scores.length;i++){
            if(scores[i]>max){
                max=scores[i];
                maxIndex=i;
            }
        }
        return maxIndex;
    }
}