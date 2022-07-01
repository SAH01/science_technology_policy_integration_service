package cc.mrbird.febs.policy.service.impl;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.policy.entity.IntermediateVariable;
import cc.mrbird.febs.policy.entity.Policy;
import cc.mrbird.febs.policy.entity.PolicyStatisticalResult;
import cc.mrbird.febs.policy.entity.model.OrganPolicyNum;
import cc.mrbird.febs.policy.entity.model.PolicyYearRegionName;
import cc.mrbird.febs.policy.entity.model.YearCategoryNum;
import cc.mrbird.febs.policy.mapper.Policy2Mapper;
import cc.mrbird.febs.policy.service.IPolicy2Service;
import cc.mrbird.febs.policy.utils.ClassifyKeywords;
import cc.mrbird.febs.policy.utils.CreateFileUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.*;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class Policy2ServiceImpl extends ServiceImpl<Policy2Mapper, Policy> implements IPolicy2Service {
    @Override
    public List<Policy> getPolicyListGroupForm(){return this.baseMapper.getPolicyListGroupForm();}
    @Override
    public List<Policy> getPolicyListGroupFormRank(){return this.baseMapper.getPolicyListGroupFormRank();}
    @Override
    public List<Policy> getPolicyListGroupRank(String form){return this.baseMapper.getPolicyListGroupRank(form);}
    @Override
    public List<Policy> getPolicyListByNameAll(String name){return this.baseMapper.getPolicyListByNameAll(name);}
    @Override
    public List<Policy> getPolicyListByName(String name){return this.baseMapper.getPolicyListByName(name);}
    @Override
    public List<Policy> getPolicyListSearchAll(String search){return this.baseMapper.getPolicyListSearchAll(search);}

    @Override
    public List<Policy> getPolicyListSearch(String search){return this.baseMapper.getPolicyListSearch(search);}
    @Override
    public List<Policy> getPolicyList(){return this.baseMapper.getPolicyList();}
    @Override
    public List<Policy> getPolicyMultiple(Policy policy){return this.baseMapper.getPolicyMultiple(policy);}
    @Override
    public List<Policy> getPolicyMultipleAll(Policy policy){return this.baseMapper.getPolicyMultipleAll(policy);}
    @Override
    public List<Policy> getQuestionNumByPolicy(List<String> organlist,List<String> rangelist,List<String> themelist,List<String> yearlist,List<String> namelist){return this.baseMapper.getQuestionNumByPolicy(organlist,rangelist,themelist,yearlist,namelist);}
    @Override
    public List<Policy> getQuestionNumByPolicy2(List<String> organlist,List<String> rangelist,List<String> themelist,List<String> yearlist,List<String> namelist,List<String> fanwei){return this.baseMapper.getQuestionNumByPolicy2(organlist,rangelist,themelist,yearlist,namelist,fanwei);}
    @Override
    public List<Policy> getQuestionNumByPolicy3(List<String> organlist,List<String> rangelist,List<String> themelist,List<String> yearlist,List<String> namelist,List<String> fanwei){return this.baseMapper.getQuestionNumByPolicy3(organlist,rangelist,themelist,yearlist,namelist,fanwei);}
    @Override
    public List<Policy> getQuestionNumByPolicy4(List<String> organlist,List<String> rangelist,List<String> themelist,List<String> namelist,String startyear,String endyear){return this.baseMapper.getQuestionNumByPolicy4(organlist,rangelist,themelist,namelist,startyear,endyear);}


    @Override
    public void completionProperties(Integer policyId) {
        Policy pending = this.baseMapper.findById(policyId);
        String policyName = pending.getName();
        WordVectorModel wordVectorModel = null;
        try {
            wordVectorModel = new WordVectorModel("data/msr_vectors.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> keywordList = HanLP.extractKeyword(policyName + "," + pending.getText(), 10);
        pending.setKeyword(String.join(",", keywordList));
        Map<String, Float> map = new TreeMap<String, Float>();
        for (String key : keywordList) {
            map.put(key, wordVectorModel.similarity(key, policyName));
        }
        //这里将map.entrySet()转换成list
        List<Map.Entry<String, Float>> list = new ArrayList<Map.Entry<String, Float>>(map.entrySet());
        //然后通过比较器来实现排序
        Collections.sort(list, new Comparator<Map.Entry<String, Float>>() {
            //升序排序
            public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });
        int i = 0;
        List<String> themeWords = new ArrayList<String>();
        for (Map.Entry<String, Float> mapping : list) {
            if (i++ == 4)
                break;
            themeWords.add(mapping.getKey());
        }
        pending.setTheme(String.join(",", themeWords));
        updateById(pending);
    }

    @Override
    public List<Policy> getList(Policy policy, QueryRequest request) {
        return null;
    }

    @Override
    public Long getCount(Policy policy) {
        return null;
    }

    @Override
    public IPage<Policy> findPolicyDetail(Policy policy, QueryRequest request) {
        Page<Policy> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "year", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findPolicyDetailPage(page, policy);
    }

    @Override
    public IPage<Policy> findPolicyDetail2(List<String> organlist,List<String> rangelist,List<String> themelist,List<String> yearlist,List<String> namelist, QueryRequest request) {
        Page<Policy> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "year", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findPolicyDetailPage2(page, organlist,rangelist,themelist,yearlist,namelist);
    }
    @Override
    public IPage<Policy> findPolicyDetail3(List<String> organlist,List<String> rangelist,List<String> themelist,List<String> yearlist,List<String> namelist, QueryRequest request) {
        Page<Policy> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "year", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findPolicyDetailPage3(page, organlist,rangelist,themelist,yearlist,namelist);
    }
    @Override
    public IPage<Policy> findPolicyDetail4(List<String> organlist,List<String> rangelist,List<String> themelist,List<String> yearlist,List<String> namelist, QueryRequest request) {
        Page<Policy> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "year", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findPolicyDetailPage4(page, organlist,rangelist,themelist,yearlist,namelist);
    }
    @Override
    public IPage<Policy> findPolicyDetail5(List<String> organlist,List<String> rangelist,List<String> themelist,List<String> namelist,String startyear,String endyear, QueryRequest request) {
        Page<Policy> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "year", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findPolicyDetailPage5(page, organlist,rangelist,themelist,namelist,startyear,endyear);
    }

    @Override
    public Policy findPolicyDetail(String policyname) {
        return null;
    }

    @Override
    public Policy getPolicyById(Integer policyId) {
        return this.baseMapper.findById(policyId);
    }

    @Override
    public List<String> getPolicyKeyWordsList() {
        return this.baseMapper.getPolicyKeyWordsList();
    }

    @Override
    public JSONObject getJsonData(List<Integer> regionIds, IntermediateVariable intermediate) {
        //查找符合条件的所有关键词
        List<String> keywordList = this.baseMapper.getKeyWordByPolicy(regionIds,intermediate);
        //对关键词分类并统计每个关键词出现的频率
        //保存关键词对应的id
        Map<String, Integer> name_id_map = new HashMap<>();
        //单词出现的频率
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
        //保存nodes节点信息，最后转成json类型的数据。
        List<GexfNode> gn_list = new ArrayList<>();
        List<GexfLink> gl_list = new ArrayList<>();
        //保存节点之间是否已经关联
        Map<String, Integer> nnrelation = new HashMap<>();
        //对wordFrequentMap排序，保证把出现频率最高的关键词放在中间
        List<Map.Entry<String, Integer>> list = new ArrayList<>(wordFrequentMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            //升序排序
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        int node_id = 0;
        for (Map.Entry<String, Integer> e : list) {
            name_id_map.put(e.getKey(), node_id);
            GexfNode gn = new GexfNode();
            gn.setId(node_id++);
            gn.setName(e.getKey());
            gn.setSymbolSize(e.getValue());
            //关键词所属类别，Policydafen2的方法进行计算
            int tempType = ClassifyKeywords.getTypeForKeyword(e.getKey());
            gn.setAttributes(new GexfNodeAttributes(tempType));
            //还需要优化
            if (node_id == 1) {
                //中心	x:-300，y:-50
                gn.setX(-300);
                gn.setY(-50);
            } else if (tempType == 0) {
                //左上角	x:-1300，y:-600
                gn.setX(-300 - (Math.random() * 1000));
                gn.setY(-50 - (Math.random() * 500));
            } else if (tempType == 1) {
                //右上角	x:600，y:-600
                gn.setX(-300 + (Math.random() * 900));
                gn.setY(-50 - (Math.random() * 500));
            } else if (tempType == 2) {
                //左下角	x:-1300，y:500
                gn.setX(-300 - (Math.random() * 1000));
                gn.setY(-50 + (Math.random() * 500));
            } else {
                //右下角	"x":600,"y":500
                gn.setX(-300 + (Math.random() * 1000));
                gn.setY(-50 + (Math.random() * 500));
            }
            gn_list.add(gn);
        }
        int gl_id = 0;
        for (String keyword : keywordList) {
            if (keyword == null || keyword.equals("nan"))
                continue;
            String[] test = keyword.split(",");
            if (test.length > 1) {

                for (int i = 0; i < test.length - 1; i++) {
                    for (int j = i + 1; j < test.length; j++) {
                        if (!nnrelation.containsKey(test[j] + "," + test[i]) && !nnrelation.containsKey(test[i] + "," + test[j])) {
                            GexfLink gl = new GexfLink();
                            gl.setId(gl_id++);
                            gl.setName("");
                            gl.setSource(name_id_map.get(test[i]));
                            gl.setTarget(name_id_map.get(test[j]));
                            gl_list.add(gl);
                            nnrelation.put(test[i] + "," + test[j], 0);
                        }
                    }
                }
            }

        }
        JSONObject json = new JSONObject();
        json.put("nodes", gn_list);
        json.put("links", gl_list);
        return json;
    }

    @Override
    @Transactional
    public void createPolicy(Policy policy) {
        this.baseMapper.insert(policy);
    }

    @Override
    public JSONObject getPolicyInstrumentTableJsonData(List<Integer> regionIds, IntermediateVariable intermediate) {
        //查找符合条件的所有关键词
        List<String> keywordList = this.baseMapper.getKeyWordByPolicy(regionIds,intermediate);
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
            keywords[wordInstrumentType].add(word);
            frequency[wordInstrumentType] += e.getValue();
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
        return json;
    }

    /**
     * 统计各地域不同年份出台的某一类科技政策的数量
     */
    @Override
    public List<PolicyStatisticalResult> getOneThemeManyRegionPolicyNumAndYear(List<Integer> regionIds, IntermediateVariable intermediate, List<String> formulaKeywords){

        return this.baseMapper.getOneThemeManyRegionPolicyNumAndYear(regionIds,intermediate,formulaKeywords);
    }

    @Override
    public List<YearCategoryNum> getEveryYearEveryCategoryNum(IntermediateVariable intermediate) {
        return this.baseMapper.getEveryYearEveryCategoryNum(intermediate);
    }
    @Override
    public List<OrganPolicyNum> getEveryOrganPolicyNum(IntermediateVariable intermediate){
        return this.baseMapper.getEveryOrganPolicyNum(intermediate);
    }

    @Override
    public List<PolicyYearRegionName> getPolicyYearRegionNameList(List<Integer> regionIds, IntermediateVariable intermediate, List<String> formulaKeywords) {
        return this.baseMapper.getPolicyYearRegionNameList(regionIds,intermediate,formulaKeywords);
    }
    @Override
    public List<PolicyYearRegionName> getSuperiorPolicyByPolicyName(String policyName, List<String> formulaKeywords){
        return this.baseMapper.getSuperiorPolicyByPolicyName(policyName,formulaKeywords);
    }
    @Override
    public PolicyYearRegionName getPolicyByName(String name){
        return this.baseMapper.getPolicyByName(name);
    }
    /**
     * 统计某一年一个地区不同类型政策数量
     * variable：传递参数的中间对象
     */
    @Override
    public List<YearCategoryNum> getOneYearOneRegionCategoryNum(IntermediateVariable variable){
        return this.baseMapper.getOneYearOneRegionCategoryNum(variable);
    }

    /**
     * 统计某一年国家不同类型政策数量
     * variable：传递参数的中间对象
     */
    @Override
    public List<YearCategoryNum> getOneYearCountryCategoryNum(IntermediateVariable variable){
        return this.baseMapper.getOneYearCountryCategoryNum(variable);
    }

    @Override
    public List<Policy> getOneYearOneRegionOneCategoryPolicy(IntermediateVariable variable){
        return this.baseMapper.getOneYearOneRegionOneCategoryPolicy(variable);
    }

    @Override
    public List<YearCategoryNum> getOneYearCategoryNum(IntermediateVariable variable){
        return this.baseMapper.getOneYearCategoryNum(variable);
    }
}

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


