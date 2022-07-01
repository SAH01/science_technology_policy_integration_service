package cc.mrbird.febs.policy.utils;

import java.util.*;


/**
 * 对关键词进行分类，有三种分类标准，政策工具、三大产业、科技活动类型
 */
public class ClassifyKeywords {
    public final static int SecondInstrumentNum = 14;
    public final static int FirstInstrumentNum = 3;
    public final static int SecondThreeIndustriesNum = 42;
    public final static int FirstThreeIndustriesNum = 3;
    public final static int FirstActivityTypeNum = 3;
    private final static float similarity = 0.3f;
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
//        System.out.println(getInstrumentSecondIndexByKeyword("农村"));
//        System.out.println(getInstrumentSecondIndexByKeyword("培训"));
//        System.out.println(getInstrumentSecondIndexByKeyword("贸易管制"));
        System.out.println(getThreeIndustriesKeywords().size());
        System.out.println(getThreeIndustriesSecondIndexByKeyword("互联网"));
        System.out.println(getThreeIndustriesSecondIndexByKeyword("航天事业"));
        System.out.println(getThreeIndustriesSecondIndexByKeyword("体育"));
    }

    //获取一个单词的政策工具二分类下标
    public static int getInstrumentSecondIndexByKeyword(String keyword) {
        Levenshtein lt = new Levenshtein();
        double[] scores = new double[14];
        //计算每种分类中的得分
        {
            for (String toCompareWord : InstrumentKeywords.instrument1_1) {
                scores[0] += lt.getSimilarityRatio(keyword, toCompareWord);
            }
            for (String toCompareWord : InstrumentKeywords.instrument1_2) {
                scores[1] += lt.getSimilarityRatio(keyword, toCompareWord);
            }
            for (String toCompareWord : InstrumentKeywords.instrument1_3) {
                scores[2] += lt.getSimilarityRatio(keyword, toCompareWord);
            }
            for (String toCompareWord : InstrumentKeywords.instrument1_4) {
                scores[3] += lt.getSimilarityRatio(keyword, toCompareWord);
            }
            for (String toCompareWord : InstrumentKeywords.instrument1_5) {
                scores[4] += lt.getSimilarityRatio(keyword, toCompareWord);
            }
            for (String toCompareWord : InstrumentKeywords.instrument2_1) {
                scores[5] += lt.getSimilarityRatio(keyword, toCompareWord);
            }
            for (String toCompareWord : InstrumentKeywords.instrument2_2) {
                scores[6] += lt.getSimilarityRatio(keyword, toCompareWord);
            }
            for (String toCompareWord : InstrumentKeywords.instrument2_3) {
                scores[7] += lt.getSimilarityRatio(keyword, toCompareWord);
            }
            for (String toCompareWord : InstrumentKeywords.instrument2_4) {
                scores[8] += lt.getSimilarityRatio(keyword, toCompareWord);
            }
            for (String toCompareWord : InstrumentKeywords.instrument2_5) {
                scores[9] += lt.getSimilarityRatio(keyword, toCompareWord);
            }
            for (String toCompareWord : InstrumentKeywords.instrument3_1) {
                scores[10] += lt.getSimilarityRatio(keyword, toCompareWord);
            }
            for (String toCompareWord : InstrumentKeywords.instrument3_2) {
                scores[11] += lt.getSimilarityRatio(keyword, toCompareWord);
            }
            for (String toCompareWord : InstrumentKeywords.instrument3_3) {
                scores[12] += lt.getSimilarityRatio(keyword, toCompareWord);
            }
            for (String toCompareWord : InstrumentKeywords.instrument3_4) {
                scores[13] += lt.getSimilarityRatio(keyword, toCompareWord);
            }
        }
        int maxIndex = 0;
        double max = scores[0];
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] > max) {
                max = scores[i];
                maxIndex = i;
            }
        }
//        System.out.print(keyword+" Instrument:"+max);
        if (max <= similarity) {
            return -1;
        }
        return maxIndex;
    }

    //根据政策工具二分类下标返回对应的一级分类下标
    public static int getInstrumentFirsrIndexBySecondIndex(int secondIindex) {
        if (secondIindex == -1) {
            return -1;
        }
        if (secondIindex < 5) {
            return 0;
        } else if (secondIindex < 10) {
            return 1;
        } else {
            return 2;
        }
    }

    //获取一个单词的三大产业二分类下标
    public static int getThreeIndustriesSecondIndexByKeyword(String keyword) {
        Levenshtein lt = new Levenshtein();
        double[] scores = new double[SecondThreeIndustriesNum];
        List<String[]> keywordsList = getThreeIndustriesKeywords();
        //计算每种分类中的得分
        for (int i = 0; i < keywordsList.size(); i++) {
            for (String toCompareWord : keywordsList.get(i)) {
                scores[i] += lt.getSimilarityRatio(keyword, toCompareWord);
            }
        }

        int maxIndex = 0;
        double max = scores[0];
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] > max) {
                max = scores[i];
                maxIndex = i;
            }
        }
//        System.out.print(" Industries:"+max);
        if (max <= similarity) {
            return -1;
        }
        return maxIndex;
    }

    //根据三大产业二分类下标返回对应的一级分类下标
    public static int getThreeIndustriesFirsrIndexBySecondIndex(int secondIindex) {
        if (secondIindex == -1) {
            return -1;
        }
        if (secondIindex < 5) {
            return 0;
        } else if (secondIindex < 15) {
            return 1;
        } else {
            return 2;
        }
    }

    //获取一个单词的科技活动类型分类下标
    public static int getActivityTypeIndexByKeyword(String keyword) {
        Levenshtein lt = new Levenshtein();
        double[] scores = new double[FirstActivityTypeNum];
        List<String[]> keywordsList = getActivityTypeKeywords();
        //计算每种分类中的得分
        for (int i = 0; i < keywordsList.size(); i++) {
            for (String toCompareWord : keywordsList.get(i)) {
                scores[i] += lt.getSimilarityRatio(keyword, toCompareWord);
            }
        }
        int maxIndex = 0;
        double max = scores[0];
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] > max) {
                max = scores[i];
                maxIndex = i;
            }
        }
        if (max <= similarity) {
            return -1;
        }
//        System.out.println(" Activity:"+max);
        return maxIndex;
    }

    //获取科技活动类型下标对应的含义
    public static String getActivityNameByIndex(int index) {
        if (index == -1) {
            return null;
        }
        if (index == 0) {
            return "基础研究";
        } else if (index == 1) {
            return "研究开发";
        } else {
            return "产业化";
        }
    }


    //三大产业关键词
    private static List<String[]> getThreeIndustriesKeywords() {
        /**
         * ① 第一产业为第二三产业奠定基础；
         * ② 第二产业是三大产业的核心，对第一产业有带动作用；
         * ③ 第一二产业为第三产业创造条件，第三产业发展促进第一二产业的进步。
         */
        List<String[]> threeIndustriesKeywordsList = new ArrayList<>();

        //农业
        String[] agriculture1 = {"种植业", "粮食", "水稻", "麦子", "油料", "花生", "油菜", "芝麻", "大豆", "蔬菜", "萝卜", "白菜", "芹菜", "冬瓜", "南瓜", "土豆", "西红柿", "黄瓜", "豇豆", "扁豆", "辣椒", "红薯", "菜花", "洋葱", "香椿", "蘑菇", "水果", "苹果", "梨", "香蕉", "西瓜", "香瓜", "榴莲", "石榴", "葡萄", "桔子", "橙子", "菠萝", "甘蔗", "哈密瓜", "荔枝", "龙眼", "柚子", "柿子", "樱桃", "猕猴桃", "干果", "核桃", "开心果", "榛子", "腰果", "榧子", "花生", "松子", "栗子", "莲子", "白瓜子", "葵花子", "芝麻", "胡桃", "喀什巴丹木", "扁桃", "无花果", "花", "玫瑰花", "火鹤", "牡丹花", "旋转玫瑰", "罂粟花", "郁金香", "百合花", "旋转铃", "兜兰花", "牵牛花", "雪莲花", "绣球花", "康乃馨", "德甘菊", "迎春花", "梅花", "仙人掌花", "荷花", "睡莲", "卡特兰", "千日红", "不老菊", "长寿菊", "向日葵", "鱼尾菊", "嘉宝菊", "石榴花", "粉菠萝", "小菊花", "大丽花", "山茶花", "非洲菊", "蝴蝶兰", "大花蕙兰", "健康花", "荷包花", "牵牛花**", "马蹄莲", "大富贵", "水仙花", "棉花", "甘草", "烟叶"};
        String[] agriculture2 = {"畜牧业", "猪", "山羊", "牛", "马", "猫", "狗", "骆驼", "大象", "老鼠", "松鼠", "老虎", "狮子", "熊猫", "长颈鹿", "袋鼠", "河马", "白兔", "鸡", "鸭", "鹅"};
        String[] agriculture3 = {"林业", "梧桐树", "杨树", "松树", "石榴树", "苹果树", "葡萄园"};
        String[] agriculture4 = {"渔业", "鲫鱼", "鲢鱼", "白条", "火头", "虾", "乌龟"};
        String[] agriculture5 = {"副业", "编席子", "采药"};
        //工业
        String[] industrial1 = {"矿石", "煤矿", "煤厂", "铁矿", "铁厂", "铁的器具用具", "钢材", "矿石", "水泥厂"};
        String[] industrial2 = {"钢铁", "", "机器制造业", "飞机", "轮船", "汽车", "轿车", "自行车", "摩托车", "机器设备", "水泵", "门厂", "防盗门"};
        String[] industrial3 = {"石油", "炼油厂", "汽油柴油", "塑料厂", "塑料的盆", "袋", "笔", "牙刷", "燃气厂", "煤气"};
        String[] industrial4 = {"木材", "家具厂", "沙发", "桌子", "椅子", "凳子", "纸厂", "印刷厂", "铅笔厂"};
        String[] industrial5 = {"棉花", "纺织厂", "纺织业", "服装厂", "提包皮包", "上衣", "裤子", "帽子", "袜子", "毛巾", "围巾", "被子被单"};
        String[] industrial6 = {"皮革橡胶", "动物皮", "皮鞋", "橡树胶", "胶盆", "运动鞋", "轮胎"};
        String[] industrial7 = {"化学工业", "化肥厂", "添加剂", "色素", "牙膏", "洗头膏", "啫喱水", "奶粉厂"};
        String[] industrial8 = {"食品", "油厂", "花生油", "菜子油", "色拉油", "香油", "大规模的称榨油厂", "盐厂", "酱油厂", "醋厂", "茶厂", "糖厂调料厂", "面厂", "方便面酒厂", "各种酒制药厂卷烟厂", "各种烟饲料厂", "饼干厂", "各种食品饺子厂", "饺子馒头厂", "馒头"};
        String[] industrial9 = {"电器厂", "电视", "VCD冰箱", "洗衣机", "空调", "电灯", "电灯泡", "电棒", "台灯", "电风扇", "跑步机", "电脑", "手机", "充电器", "各种元器件", "配件", "音像制品厂", "碟片", "磁带"};
        String[] industrial10 = {"电", "发电厂", "水", "自来水厂", "兴于城市", "瓷器", "碗厂", "地板砖", "花瓶", "玻璃厂", "玻璃杯", "酒瓶", "油漆涂料厂"};
        //服务业
        String[] service1 = {"商业", "服装店超市批发部", "汽车配件", "食品零售", "糕点", "茶叶铺", "简单的商业形式", "销售"};
        String[] service2 = {"餐饮业", "酒店", "饭店小吃店"};
        String[] service3 = {"建筑业", "房地产开发装潢公司宾馆"};
        String[] service4 = {"交通运输业", "公交车", "出租车", "飞机", "火车", "轮船"};
        String[] service5 = {"邮电通信", "邮局", "寄信", "汇包裹", "中国移动通信", "联通通信"};
        String[] service6 = {"金融", "各种银行", "储蓄所", "证券中心"};
        String[] service7 = {"保险", "保险公司", "人寿保险", "意外保险", "财产保险"};
        String[] service8 = {"公共安全", "各级人民法院", "公安局", "警察", "军队", "管理行业", "行政管理", "国家主席", "省长", "市长", "局长", "乡长", "村干部"};
        String[] service9 = {"广播电视", "各省电视台", "广播电视台"};
        String[] service10 = {"航天事业", "天气预报", "地理勘探"};
        String[] service11 = {"教育", "学校", "小学", "中学", "大学技校", "人才培养", "人才引进"};
        String[] service12 = {"文化", "杂志社", "报纸", "书籍出版社"};
        String[] service13 = {"体育", "舞蹈", "击剑", "乒乓球", "篮球", "足球", "橄榄球", "高尔夫球"};
        String[] service14 = {"福利", "福利", "体育彩票", "股票"};
        String[] service15 = {"健康医疗", "整形医院"};
        String[] service16 = {"美容美发业"};
        String[] service17 = {"旅游业", "旅行社", "名胜古迹公园"};
        String[] service18 = {"宣传", "广告公司"};
        String[] service19 = {"摄影", "婚纱摄影照相"};
        String[] service20 = {"打字复印", "影印"};
        String[] service21 = {"健身仪器"};
        String[] service22 = {"信息", "计算机编制内容", "网站", "工业设计", "研发服务", "研发设计", "研究开发", "成果转化", "技术服务", "技术交易", "技术市场", "技术转移", "创业孵化", "孵化器", "孵化基地", "检验检测认证", "测试", "计量", "知识产权保护", "知识产权服务", "知识产权管理", "知识产权交易", "知识产权运营", "科技咨询", "管理咨询", "技术咨询", "科技评估", "科技金融", "质押融资", "产权融资", "股权融资", "科普", "科学技术普及", "科技中介", "增值服务", "中介服务"};
        String[] service23 = {"艺术", "绘画", "画家", "美术家雕塑", "雕塑家音乐", "歌手"};
        String[] service24 = {"科学", "汉语外国语", "英语", "数学", "物理", "化学", "生物", "历史", "政治", "地理"};
        String[] service25 = {"娱乐行业", "电影院", "明星艺人", "演员", "电视台主持人", "主播以较高的文化水平为基础"};
        String[] service26 = {"考古", "博物院", "文物展"};
        String[] service27 = {"工作职称", "高等职称", "董事长", "经理", "教师", "律师", "作家", "记者", "主持人", "导演", "编辑", "会计", "警察", "医生", "厨师", "修理工", "司机", "商人", "军人", "工人", "农民", "学生", "读者", "演员", "观众", "顾客"};
        threeIndustriesKeywordsList.add(agriculture1);
        threeIndustriesKeywordsList.add(agriculture2);
        threeIndustriesKeywordsList.add(agriculture3);
        threeIndustriesKeywordsList.add(agriculture4);
        threeIndustriesKeywordsList.add(agriculture5);

        threeIndustriesKeywordsList.add(industrial1);
        threeIndustriesKeywordsList.add(industrial2);
        threeIndustriesKeywordsList.add(industrial3);
        threeIndustriesKeywordsList.add(industrial4);
        threeIndustriesKeywordsList.add(industrial5);
        threeIndustriesKeywordsList.add(industrial6);
        threeIndustriesKeywordsList.add(industrial7);
        threeIndustriesKeywordsList.add(industrial8);
        threeIndustriesKeywordsList.add(industrial9);
        threeIndustriesKeywordsList.add(industrial10);

        threeIndustriesKeywordsList.add(service1);
        threeIndustriesKeywordsList.add(service2);
        threeIndustriesKeywordsList.add(service3);
        threeIndustriesKeywordsList.add(service4);
        threeIndustriesKeywordsList.add(service5);
        threeIndustriesKeywordsList.add(service6);
        threeIndustriesKeywordsList.add(service7);
        threeIndustriesKeywordsList.add(service8);
        threeIndustriesKeywordsList.add(service9);
        threeIndustriesKeywordsList.add(service10);
        threeIndustriesKeywordsList.add(service11);
        threeIndustriesKeywordsList.add(service12);
        threeIndustriesKeywordsList.add(service13);
        threeIndustriesKeywordsList.add(service14);
        threeIndustriesKeywordsList.add(service15);
        threeIndustriesKeywordsList.add(service16);
        threeIndustriesKeywordsList.add(service17);
        threeIndustriesKeywordsList.add(service18);
        threeIndustriesKeywordsList.add(service19);
        threeIndustriesKeywordsList.add(service20);
        threeIndustriesKeywordsList.add(service21);
        threeIndustriesKeywordsList.add(service22);
        threeIndustriesKeywordsList.add(service23);
        threeIndustriesKeywordsList.add(service24);
        threeIndustriesKeywordsList.add(service25);
        threeIndustriesKeywordsList.add(service26);
        threeIndustriesKeywordsList.add(service27);

        return threeIndustriesKeywordsList;
    }

    //科技活动类型关键词
    private static List<String[]> getActivityTypeKeywords() {
        List<String[]> activityTypeKeywordsList = new ArrayList<>();
        //基础研究
        String[] activity1 = {"基础研究", "数学", "物理", "化学", "天文", "地学", "生物科学", "基础学科", "农业", "能源", "资源", "环境", "健康", "信息", "材料", "海洋", "空间", "蛋白质科学", "量子科学", "纳米科学技术", "发育与生殖生物学", "论文", "创新环境", "鼓励创新", "科学数据", "自然科技资源", "科技文献采集", "科技文献加工", "科技文献集成", "科技文献共享", "科技文献服务", "国家野外科学", "国家重点实验室", "国家实验室"};
        //研究开发
        String[] activity2 = {"研究开发", "研究开发新材料", "研究开发新产品", "研究开发新装置", "研究开发新工艺", "研究开发新系统", "研究开发新服务", "专利", "专有知识", "研究与开发", "研究与开发中心", "发明创造", "科技成果", "专利技术"};
        //产业化
        String[] activity3 = {"产业化", "科学论文", "专著", "原理性模型", "发明专利", "市场连接型", "龙头企业带动", "农科教结合", "专业协会带动", "主导产业，区域布局，发展规模经营，实行市场牵龙头，龙头带动基地，基地连农户", "国内外大市场", "规模经营", "专业化分工", "科技进步"};
        activityTypeKeywordsList.add(activity1);
        activityTypeKeywordsList.add(activity2);
        activityTypeKeywordsList.add(activity3);
        return activityTypeKeywordsList;
    }

    //政策工具关键词
    private static class InstrumentKeywords {
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
    }


}