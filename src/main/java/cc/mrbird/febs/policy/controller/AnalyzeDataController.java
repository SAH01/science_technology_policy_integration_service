package cc.mrbird.febs.policy.controller;


import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.policy.entity.*;
import cc.mrbird.febs.policy.entity.model.OrganPolicyNum;
import cc.mrbird.febs.policy.entity.model.PolicyYearRegionName;
import cc.mrbird.febs.policy.entity.model.ShowData;
import cc.mrbird.febs.policy.entity.model.YearCategoryNum;
import cc.mrbird.febs.policy.service.IAnnualService;
import cc.mrbird.febs.policy.service.IPolicy2Service;
import cc.mrbird.febs.policy.service.IScienceTechnologyEntityIndexService;
import cc.mrbird.febs.policy.service.IYearBookDataService;
import cc.mrbird.febs.policy.utils.Levenshtein;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.*;

/**
 * 返回前台展示所需要的分析结果，
 */
@Slf4j
@RestController
@RequestMapping("analyze")
public class AnalyzeDataController {
    @Autowired
    IPolicy2Service policy2Service;
    @Autowired
    IScienceTechnologyEntityIndexService scienceTechnologyEntityIndexService;
    @Autowired
    IYearBookDataService yearBookDataService;
    @Autowired
    IAnnualService annualService;

    /**
     * 科技政策数量简单统计
     *
     * @param variable 传输数据的中间对象
     * @return 绘制图像的json
     */
    @GetMapping("policyNum")
    public FebsResponse policyNum(IntermediateVariable variable) {
        List<Integer> regionIds;
        List<String> formulaKeywords = getFormulaSqlKeywordByContent(variable.getFormulaContent());
        //最终返回的json对象
        JSONObject json = new JSONObject();
        String regionIdStr = variable.getRegionIds();
        //要进行查找，地区不能为空
        if (formulaKeywords == null || formulaKeywords.size() == 0 || regionIdStr == null || "".equals(regionIdStr.trim())) {
            json.put("categories", null);
            json.put("series", null);
            return new FebsResponse().success().data(json);
        }
        regionIds = new ArrayList<>();
        for (String regionTmp : regionIdStr.split(",")) {
            regionIds.add(Integer.parseInt(regionTmp));
        }
        List<PolicyStatisticalResult> policyStatisticalResultList = this.policy2Service.getOneThemeManyRegionPolicyNumAndYear(regionIds, variable, formulaKeywords);
        //进行数据补齐
        Map<String, Integer[]> resultMap = new HashMap<>();
        List<Integer> years = new ArrayList<>();
        for (PolicyStatisticalResult result : policyStatisticalResultList) {
            if (result.getYear() != null) {
                if (!years.contains(result.getYear())) {
                    years.add(result.getYear());
                }
            }
        }

        Collections.sort(years);
        for (PolicyStatisticalResult result : policyStatisticalResultList) {
            if (result.getRegion() == null) {
                result.setRegion("国家相关部门");
            }
            if (resultMap.containsKey(result.getRegion())) {
                Integer[] numList = resultMap.get(result.getRegion());
                if (result.getYear() != null) {
                    numList[years.indexOf(result.getYear())] = result.getNum();
                }

            } else {
                if (result.getYear() != null) {
                    Integer[] numList = new Integer[years.size()];
                    Arrays.fill(numList, 0);
                    numList[years.indexOf(result.getYear())] = result.getNum();
                    resultMap.put(result.getRegion(), numList);
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
        json.put("categories", years);
        json.put("series", jsonObjectList);
        return new FebsResponse().success().data(json);
    }

    /**
     * 实体指标数据简单统计
     *
     * @param variable 传输数据的中间对象
     * @return 绘制图像的json
     */
    @GetMapping("entityIndexNum")
    public FebsResponse entityIndexNum(IntermediateVariable variable) {
        String tmpEID = variable.getEntityIndexIds();
        if (tmpEID == null || "".equals(tmpEID.trim())) {
            return new FebsResponse().message("请选择叶子节点进行分析");
        }
        List<Integer> entityIndexIds = new ArrayList<>();
        for (String strID : tmpEID.split(",")) {
            if (!"".equals(strID)) {
                entityIndexIds.add(Integer.parseInt(strID));
            }
        }
        List<String> entityIndexContentList = this.scienceTechnologyEntityIndexService.getEntityIndexContentsByEntityIndexIds(entityIndexIds);
        String tmpRegionIds = variable.getRegionIds();
        //要进行查找，地区不能为空
        if (tmpRegionIds == null || "".equals(tmpRegionIds.trim())) {
            return new FebsResponse().message("请选择要比较的地区！");
        }
        List<String> regionIds = new ArrayList<>(Arrays.asList(tmpRegionIds.split(",")));
        /*for (String tmpID : tmpRegionIds.split(",")) {
            regionIds.add(tmpID);
        }*/
        //多个指标，每个指标下有多个多个地域多个年份
        List<YearBookData> yearBookDataList = this.yearBookDataService.getEntityNum(regionIds, variable, entityIndexContentList);
        //不同指标的数据放在不同的list里
        List<List<YearBookData>> yearBookDataThemeList = new ArrayList<>();
        String chartName = "";
        List<YearBookData> tmpOneThemeData = new ArrayList<>();
        for (YearBookData yearBookData : yearBookDataList) {
            if (!chartName.equals(yearBookData.getName())) {
                if (!"".equals(chartName)) {
                    yearBookDataThemeList.add(tmpOneThemeData);
                    tmpOneThemeData = new ArrayList<>();
                }
                chartName = yearBookData.getName();
            }
            tmpOneThemeData.add(yearBookData);
        }
        //最后一个指标的数据加进来
        yearBookDataThemeList.add(tmpOneThemeData);
        List<JSONObject> result = new ArrayList<>();
        for (List<YearBookData> oneThemeData : yearBookDataThemeList) {
            JSONObject tmpJson = getOneGroupJsonData(oneThemeData);
            if (tmpJson != null)
                result.add(tmpJson);
        }
        return new FebsResponse().success().data(result);
    }

    @GetMapping("everyYearEveryCategoryNum")
    public FebsResponse everyYearEveryCategoryNum(IntermediateVariable variable) {
        List<YearCategoryNum> yearCategoryNumList = this.policy2Service.getEveryYearEveryCategoryNum(variable);
        String regionName = yearCategoryNumList.get(0).getRegionName();
        int index = yearCategoryNumList.size() - 1;
        while (regionName.equals("国家相关部门")) {
            regionName = yearCategoryNumList.get(index).getRegionName();
            index--;
            if (index < 0)
                break;
        }

        List<String> categories = new ArrayList<>();
        List<String> years = new ArrayList<>();
        for (YearCategoryNum tmpData : yearCategoryNumList) {
            if (!years.contains(tmpData.getYear())) {
                years.add(tmpData.getYear());
            }
            if (!categories.contains(tmpData.getCategory())) {
                categories.add(tmpData.getCategory());
            }
        }
        Collections.sort(years);
        /*
         * 结果格式：
         * [{category:"",values:[]},{category:"",values:[]},...]
         * 备注：数组不支持泛型，所以警告是不可避免的。初始化即可。
         */
        Map<String, Integer[]>[] data = new Map[categories.size()];
        //完成初始化
        for (int i = 0; i < categories.size(); i++) {
            data[i] = new HashMap<>();
            data[i].put(categories.get(i), new Integer[years.size()]);
//            Arrays.fill(data[i].get(categories.get(i)),0);
        }
        //赋值
        for (YearCategoryNum tmpData : yearCategoryNumList) {
            data[categories.indexOf(tmpData.getCategory())].get(tmpData.getCategory())[years.indexOf(tmpData.getYear())] = tmpData.getNum();
        }
        JSONObject json = new JSONObject();
        json.put("regionName", regionName);
        json.put("categories", categories);
        json.put("years", years);
        json.put("data", data);
        return new FebsResponse().success().data(json);
    }

    @GetMapping("everyOrganPolicyNum")
    public FebsResponse everyOrganPolicyNum(IntermediateVariable intermediate) {
        List<OrganPolicyNum> organPolicyNumList = this.policy2Service.getEveryOrganPolicyNum(intermediate);
        List<String> organs = new ArrayList<>();
        List<JSONObject> dataList = new ArrayList<>();
        for (OrganPolicyNum tmpData : organPolicyNumList) {
            organs.add(tmpData.getOrgan());
            JSONObject tmpJson = new JSONObject();
            tmpJson.put("name", tmpData.getOrgan());
            tmpJson.put("value", tmpData.getNum());
            dataList.add(tmpJson);
        }

        JSONObject json = new JSONObject();
        json.put("organs", organs);
        json.put("dataList", dataList);
        return new FebsResponse().success().data(json);
    }

    @GetMapping("corporateTechnologyActivities/{regionId}")
    public FebsResponse getCorporateTechnologyActivities(@NotBlank(message = "{required}") @PathVariable String regionId) {
        List<Annual> annualList=annualService.getCorporateTechnologyActivities("河北省");
        System.out.println(annualList.size());
        //List<YearBookData> combineData = this.yearBookDataService.getCorporateTechnologyActivities(regionId);
        List<YearBookData> combineData=new ArrayList<>();
        //初始化
        String[] names = new String[]{"科技活动人员数", "R&D人员", "规模以上工业企业R&D经费支出总额", "规模以上工业企业新产品开发经费支出"};
//        CreateFileUtil.createJsonFile(commonMethod(combineData,names,2,new String[]{"人","万元"}).toString(), "defaultEffect", "corporateTechnologyActivities");
        return new FebsResponse().success().data(commonMethod(combineData, names, 2, new String[]{"人", "万元"}));
    }

    @GetMapping("undertakeProject/{regionId}")
    public FebsResponse getUndertakeProject(@NotBlank(message = "{required}") @PathVariable String regionId) {
       // List<YearBookData> combineData = this.yearBookDataService.getUndertakeProject(regionId);
        List<YearBookData> combineData=new ArrayList<>();
        //初始化
        String[] names = new String[]{"研究与开发机构R&D课题数", "高等学校R&D课题数", "研究与开发机构R&D课题投入人员", "高等学校R&D课题投入人员", "研究与开发机构R&D课题投入经费", "高等学校R&D课题投入经费"};

//        CreateFileUtil.createJsonFile(commonMethod(combineData,names,4,new String[]{"个","万元"}).toString(), "defaultEffect", "undertakeProject");
        return new FebsResponse().success().data(commonMethod(combineData, names, 4, new String[]{"个", "万元"}));
    }

    @GetMapping("enterpriseDevelopment/{regionId}")
    public FebsResponse getEnterpriseDevelopment(@NotBlank(message = "{required}") @PathVariable String regionId) {
        //List<YearBookData> combineData = this.yearBookDataService.getEnterpriseDevelopment(regionId);
        List<YearBookData> combineData=new ArrayList<>();
        //初始化
        String[] names = new String[]{"国家高新技术产业开发区总产值", "国家高新技术产业开发区实交税金", "开发区高新技术企业数"};
//        CreateFileUtil.createJsonFile(commonMethod(combineData,names,2,new String[]{"万元","个"}).toString(), "defaultEffect", "enterpriseDevelopment");
        return new FebsResponse().success().data(commonMethod(combineData, names, 2, new String[]{"万元", "个"}));
    }

    /**
     * 用于辅助政策公式 1.挖掘政策文件  2.按时间轴显示
     *
     * @param variable 传输数据的中间对象
     * @return 绘制图像的json
     */
    @GetMapping("policyYearRegionName")
    public FebsResponse policyYearRegionName(IntermediateVariable variable) {
        List<Integer> regionIds;
        List<String> formulaKeywords = getFormulaSqlKeywordByContent(variable.getFormulaContent());
        //最终返回的json对象
        JSONObject json = new JSONObject();
        String regionIdStr = variable.getRegionIds();
        //要进行查找，地区不能为空
        if (formulaKeywords == null || formulaKeywords.size() == 0 || regionIdStr == null || "".equals(regionIdStr.trim())) {
            json.put("categories", null);
            json.put("series", null);
            return new FebsResponse().success().data(json);
        }
        regionIds = new ArrayList<>();
        for (String regionTmp : regionIdStr.split(",")) {
            regionIds.add(Integer.parseInt(regionTmp));
        }

        List<PolicyYearRegionName> policyStatisticalResultList = this.policy2Service.getPolicyYearRegionNameList(regionIds, variable, formulaKeywords);

        List<String> keyWords = new ArrayList<>();
        if (formulaKeywords != null) {
            for (String tmpWord : formulaKeywords) {
                if (!tmpWord.contains("%")) {
                    keyWords.add(tmpWord);
                } else {
                    keyWords.addAll(Arrays.asList(tmpWord.split("%")));
                }
            }
        }
        List<String> regions = new ArrayList<>();

        Map<String, List<PolicyYearRegionName>> regionValues = new HashMap<>();
        //设置关键句子
        for (PolicyYearRegionName tmpData : policyStatisticalResultList) {
            if (!regions.contains(tmpData.getRegion())) {
                regions.add(tmpData.getRegion());
            }
            tmpData.setSentenceList(getSentenceListFromTextByFormulaKeywords(tmpData.getText(), keyWords));
            if (regionValues.containsKey(tmpData.getRegion())) {
                regionValues.get(tmpData.getRegion()).add(tmpData);
            } else {
                List<PolicyYearRegionName> tmpValues = new ArrayList<>();
                tmpValues.add(tmpData);
                regionValues.put(tmpData.getRegion(), tmpValues);
            }
        }
//        因为取出来的数据集是有序的，按照年份排序了，ArrayList是有序的，所以不需要再排序
//        Collections.sort(years);
        json.put("regions", regions);
        json.put("formulaKeywords", keyWords);
        json.put("total", policyStatisticalResultList.size());
        json.put("regionValues", regionValues);
        return new FebsResponse().success().data(json);
    }


    /**
     * 用于获取一篇科技政策相关的额科技政策
     *
     * @param variable 传输数据的中间对象
     * @return 绘制图像的json
     */
    @GetMapping("getRelationPolicy")
    public FebsResponse getRelationPolicy(IntermediateVariable variable) {
        List<JSONObject> nodesData = new ArrayList<>();
        List<JSONObject> linksData = new ArrayList<>();
        String[] categorys = new String[]{"选择的政策", "上位政策", "前驱政策", "后继政策", "相似政策", "其他地区相似政策"};
        //获取和用户选择的政策具有相同或相似名字的科技政策，上位或同级政策
        List<String> formulaSqlContent = getFormulaSqlKeywordByContent(variable.getFormulaContent());
        List<String> keyWords = new ArrayList<>();
        if (formulaSqlContent != null) {
            for (String tmpWord : formulaSqlContent) {
                if (!tmpWord.contains("%")) {
                    keyWords.add(tmpWord);
                } else {
                    keyWords.addAll(Arrays.asList(tmpWord.split("%")));
                }
            }
        }
        System.out.println(formulaSqlContent);
        List<PolicyYearRegionName> relationPolicyList = policy2Service.getSuperiorPolicyByPolicyName(variable.getPolicyName(), formulaSqlContent);
        System.out.println(relationPolicyList);
        //维护一个唯一的PolicyYearRegionName数组，用于去重
        List<String> onlyOne = new ArrayList<>();
        List<PolicyYearRegionName> policyYearRegionNameList = new ArrayList<>();
        PolicyYearRegionName nowPointPolicy = policy2Service.getPolicyByName(variable.getPolicyName());
        onlyOne.add(nowPointPolicy.getName());
        nowPointPolicy.setSentenceList(getSentenceListFromTextByFormulaKeywords(nowPointPolicy.getText(), keyWords));
        nowPointPolicy.setRelation("选择的政策");
        policyYearRegionNameList.add(nowPointPolicy);
        JSONObject pointName = new JSONObject();
        pointName.put("name", nowPointPolicy.getName());
        pointName.put("category", "选择的政策");
        nodesData.add(pointName);
        String tmpCategory;
        for (PolicyYearRegionName tmpData : relationPolicyList) {
            if (onlyOne.contains(tmpData.getName()))
                continue;
//            System.out.println(tmpData.getName());
            onlyOne.add(tmpData.getName());
            tmpData.setSentenceList(getSentenceListFromTextByFormulaKeywords(tmpData.getText(), keyWords));
            JSONObject tmpNode = new JSONObject();
            JSONObject tmpLink = new JSONObject();
            if (tmpData.getType().equals("1")) {
                tmpCategory = "上位政策";
            } else if (tmpData.getType().substring(0, 2).equals(nowPointPolicy.getType().substring(0, 2))) {
                if (tmpData.getYear() != null && !"".equals(tmpData.getYear()) && nowPointPolicy.getYear() != null && !"".equals(nowPointPolicy.getYear())) {
                    if (Integer.parseInt(tmpData.getYear()) > Integer.parseInt(nowPointPolicy.getYear())) {
                        tmpCategory = "后继政策";
                    } else {
                        tmpCategory = "前驱政策";
                    }
                } else {
                    tmpCategory = "前驱政策";
                }
            } else {
                tmpCategory = "相似政策";
            }
            tmpData.setRelation(tmpCategory);
            policyYearRegionNameList.add(tmpData);
            tmpNode.put("name", tmpData.getName());
            tmpNode.put("category", tmpCategory);

            tmpLink.put("target", tmpData.getName());
            tmpLink.put("source", nowPointPolicy.getName());
            tmpLink.put("category", tmpCategory);
            nodesData.add(tmpNode);
            linksData.add(tmpLink);
        }
        //筛选具有相似描述的政策。
        List<Integer> regionIds = null;
        //最终返回的json对象
        String regionIdStr = variable.getRegionIds();
        //要进行查找，地区不能为空
        if (regionIdStr != null && !"".equals(regionIdStr.trim())) {
            regionIds = new ArrayList<>();
            for (String regionTmp : regionIdStr.split(",")) {
                regionIds.add(Integer.parseInt(regionTmp));
            }
        }
        List<PolicyYearRegionName> similarityList = this.policy2Service.getPolicyYearRegionNameList(regionIds, variable, formulaSqlContent);
//        System.out.println("similarityList：*******************");
//        System.out.println(similarityList);
        Levenshtein levenshtein = new Levenshtein();
        boolean add;
        for (PolicyYearRegionName tmpData : similarityList) {
            add = false;
//            System.out.println(tmpData.getName());
            if (onlyOne.contains(tmpData.getName()))
                continue;
            onlyOne.add(tmpData.getName());
            tmpData.setSentenceList(getSentenceListFromTextByFormulaKeywords(tmpData.getText(), keyWords));
            JSONObject tmpNode = new JSONObject();
            JSONObject tmpLink = new JSONObject();
            tmpCategory = "其他地区相似政策";
            for (String sentence : tmpData.getSentenceList()) {
                if (add) {
                    break;
                }
                for (String policySentence : nowPointPolicy.getSentenceList()) {
                    if (levenshtein.getSimilarityRatio(sentence, policySentence) > 0.5) {
                        System.out.println(levenshtein.getSimilarityRatio(sentence, policySentence));
                        add = true;
                        break;
                    }
                }
            }

            if (add) {
                tmpData.setRelation(tmpCategory);
                policyYearRegionNameList.add(tmpData);
                tmpNode.put("name", tmpData.getName());
                tmpNode.put("category", tmpCategory);

                tmpLink.put("target", tmpData.getName());
                tmpLink.put("source", nowPointPolicy.getName());
                tmpLink.put("category", tmpCategory);
                nodesData.add(tmpNode);
                linksData.add(tmpLink);
            }
        }
        JSONObject json = new JSONObject();
        json.put("nodes", nodesData);
        json.put("links", linksData);
        json.put("categorys", categorys);
        json.put("policyYearRegionNameList", policyYearRegionNameList);
        return new FebsResponse().success().data(json);
    }


    /**
     * 统计某一年一个地区不同类型政策数量
     *
     * @param variable 传输数据的中间对象
     * @return 绘制图像的json
     */
    @GetMapping("getOneYearOneRegionCategoryNum")
    public FebsResponse getOneYearOneRegionCategoryNum(IntermediateVariable variable) {
        List<YearCategoryNum> regionCategoryNum = policy2Service.getOneYearOneRegionCategoryNum(variable);
        List<YearCategoryNum> countryCategoryNum = policy2Service.getOneYearCountryCategoryNum(variable);
        System.out.println(regionCategoryNum.size());
        String[] categories = new String[]{"支撑企业创新", "服务人才创业", "激发院所激情", "调整优化机制"};
        int[] regionNumList = new int[]{0, 0, 0, 0};
        int[] countryNumList = new int[]{0, 0, 0, 0};
        float[] regionProportionList = new float[]{0, 0, 0, 0};
        float[] countryProportionList = new float[]{0, 0, 0, 0};
        int regionSum = 0;
        int countrySum = 0;
        String regionName="";
        String yearData="";
        if(regionCategoryNum.size()>0) {
            regionName = regionCategoryNum.get(0).getRegionName();
            yearData = regionCategoryNum.get(0).getYear();
            for (YearCategoryNum tmpC : regionCategoryNum) {
                int index = getIndex(tmpC.getCategory(), categories);
                if (index != -1) {
                    regionNumList[index] = tmpC.getNum();
                    regionSum += tmpC.getNum();
                }
            }
        }
        for (YearCategoryNum tmpC : countryCategoryNum) {
            int index = getIndex(tmpC.getCategory(), categories);
            if (index != -1) {
                countryNumList[index] = tmpC.getNum();
                countrySum += tmpC.getNum();
            }
        }
        for (int i = 0; i < 4; i++) {
            regionProportionList[i] = (float) regionNumList[i] / regionSum;
            countryProportionList[i] = (float) countryNumList[i] / countrySum;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("regionName", regionName);
        jsonObject.put("yearData", yearData);
        jsonObject.put("categories", categories);
        jsonObject.put("regionNumList", regionNumList);
        jsonObject.put("regionProportionList", regionProportionList);
        jsonObject.put("countryNumList", countryNumList);
        jsonObject.put("countryProportionList", countryProportionList);
        return new FebsResponse().success().data(jsonObject);
    }


    /**
     * 统计某一年某一地区某一类型的政策
     *
     * @param variable 传输数据的中间对象
     * @return 绘制图像的json
     */
    @GetMapping("getOneYearOneRegionOneCategoryPolicy")
    public FebsResponse getOneYearOneRegionOneCategoryPolicy(IntermediateVariable variable) {
        List<Policy> policyList = policy2Service.getOneYearOneRegionOneCategoryPolicy(variable);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("policyList", policyList);
        return new FebsResponse().success().data(jsonObject);
    }

    /**
     * 统计某一年某一地区某一类型的所有政策，包括国家和地方
     *
     * @param variable 传输数据的中间对象
     * @return 绘制图像的json
     */
    @GetMapping("getOneYearCategoryNum")
    public FebsResponse getOneYearCategoryNum(IntermediateVariable variable) {
        List<YearCategoryNum> nowYearList = policy2Service.getOneYearCategoryNum(variable);
        variable.setYearData(String.valueOf(Integer.parseInt(variable.getYearData()) - 1));
        List<YearCategoryNum> lastYearList = policy2Service.getOneYearCategoryNum(variable);
        float[] increaseList = new float[]{0, 0, 0, 0};
        String[] categories = new String[]{"支撑企业创新", "服务人才创业", "激发院所激情", "调整优化机制"};
        int[] nowYearNumList = new int[]{0, 0, 0, 0};
        int[] lastYearNumList = new int[]{0, 0, 0, 0};
        for (YearCategoryNum tmpC : nowYearList) {
            int index = getIndex(tmpC.getCategory(), categories);
            if (index != -1) {
                nowYearNumList[index] = tmpC.getNum();
            }
        }
        for (YearCategoryNum tmpC : lastYearList) {
            int index = getIndex(tmpC.getCategory(), categories);
            if (index != -1) {
                lastYearNumList[index] = tmpC.getNum();
            }
        }
        for (int i = 0; i < nowYearNumList.length; i++) {
            if (lastYearNumList[i] == 0) {
                increaseList[i] = 1;
            } else {
                increaseList[i] = (float) (nowYearNumList[i] - lastYearNumList[i]) / lastYearNumList[i];

            }
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("categories", categories);
        jsonObject.put("increaseList", increaseList);
        jsonObject.put("nowYearNumList", nowYearNumList);
        jsonObject.put("lastYearNumList", lastYearNumList);
        return new FebsResponse().success().data(jsonObject);
    }


    private int getIndex(String name, String[] nameList) {
        if (name == null || nameList == null || nameList.length == 0) {
            return -1;
        }
        int i = 0;
        for (String tmpName : nameList) {
            if (name.equals(tmpName)) {
                return i;
            }
            i++;
        }
        return -1;
    }


    /**
     * @param text            政策文件内容
     * @param formulaKeywords 关键词（纯关键词，没有%号的）
     * @return 包含关键词的句子
     */
    private List<String> getSentenceListFromTextByFormulaKeywords(String text, List<String> formulaKeywords) {
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
                containKeywordSequenceList.add(sequence.replaceAll(" ", ""));
            }
        }
        return containKeywordSequenceList;
    }

    /**
     * 统计多项指标数据通用方法
     *
     * @param combineData 查询出来的指标数据
     * @param names       指标名称数组
     * @param dividing    单位的分界点
     * @return json
     */
    private JSONObject commonMethod(List<YearBookData> combineData, String[] names, int dividing, String[] unit_c) {
        //初始化
        List<String> years = new ArrayList<>();
        for (YearBookData tmpData : combineData) {
            if (!years.contains(tmpData.getYears())) {
                years.add(tmpData.getYears());
            }
        }
        Collections.sort(years);
        ShowData[] data = new ShowData[names.length];
        for (int i = 0; i < names.length; i++) {
            data[i] = new ShowData();
            data[i].setName(names[i]);
            if (i < dividing) {
                data[i].setType("bar");
                data[i].setUnit_c(unit_c[0]);
            } else {
                data[i].setType("line");
                data[i].setUnit_c(unit_c[1]);
            }
            data[i].setValues(new String[years.size()]);
        }
        //赋值
        for (YearBookData tmpData : combineData) {
            data[indexOf(names, tmpData.getName())].getValues()[years.indexOf(tmpData.getYears())] = tmpData.getValue();
        }
        JSONObject json = new JSONObject();
        json.put("names", names);
        json.put("years", years);
        json.put("data", data);
        return json;
    }

    private int indexOf(String[] list, String item) {
        if (item == null)
            return -1;
        for (int i = 0; i < list.length; i++) {
            if (item.equals(list[i])) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 格式化一个指标不同地区各年度数据---针对实体指标的设计
     *
     * @return json
     */
    private JSONObject getOneGroupJsonData(List<YearBookData> yearBookDataList) {
        if (yearBookDataList == null || yearBookDataList.size() < 1) {
            return null;
        }
        //清洗去重
        List<YearBookData> newYearBookDataList = new ArrayList<>();

        YearBookData compareWith = yearBookDataList.get(0);
        String unitC = yearBookDataList.get(yearBookDataList.size() - 1).getUnitC();
        String wellValue = yearBookDataList.get(yearBookDataList.size() - 1).getValue();
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
        Map<String, String[]> resultMap = new HashMap<>();
        List<String> years = new ArrayList<>();
        for (YearBookData result : newYearBookDataList) {
            if (result.getYears() != null) {
                if (!years.contains(result.getYears())) {
                    years.add(result.getYears());
                }
            }
            String oldRegion = result.getRegion();
            for (String tmpRegion : unifyRegionName) {
                if (!tmpRegion.equals(oldRegion) && tmpRegion.contains(oldRegion)) {
                    result.setRegion(tmpRegion);
                }
            }
        }

        Collections.sort(years);
        for (YearBookData result : newYearBookDataList) {
            if (resultMap.containsKey(result.getRegion())) {
                String[] numList = resultMap.get(result.getRegion());
                if (result.getYears() != null) {
                    numList[years.indexOf(result.getYears())] = result.getValue();
                }

            } else {
                if (result.getYears() != null) {
                    String[] numList = new String[years.size()];
                    numList[years.indexOf(result.getYears())] = result.getValue();
                    resultMap.put(result.getRegion(), numList);
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
    private int compareUnit(String unit1, String unit2) {
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

    /**
     * 根据政策挖掘公式内容生成响应的可用于检索的关键词
     *
     * @param formulaContent 政策挖掘公式内容
     * @return 词列表
     */
    private List<String> getFormulaSqlKeywordByContent(String formulaContent) {
        if(formulaContent==null||"".equals(formulaContent.trim())){
            return null;
        }
        List<String> formulaKeywords = new ArrayList<>();
        if (formulaContent != null) {
            formulaContent = formulaContent.replace("&amp;", "&");

            String[] segmentation = formulaContent.split("-");
            if (segmentation.length == 1) {
                formulaKeywords.addAll(Arrays.asList(formulaContent.split("、")));
            } else if (segmentation.length == 2) {
                String[] frontPartOfContent = segmentation[0].split("、");
                String[] rearPartOfContent = segmentation[1].split("、");
                for (String entity : frontPartOfContent) {
                    for (String keyword : rearPartOfContent) {
                        formulaKeywords.add(entity + "%" + keyword);
                    }
                }
            }
        }
        return formulaKeywords;
    }
}
