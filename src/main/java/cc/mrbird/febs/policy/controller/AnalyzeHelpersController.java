package cc.mrbird.febs.policy.controller;


import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.policy.entity.IntermediateVariable;
import cc.mrbird.febs.policy.entity.Policy;
import cc.mrbird.febs.policy.entity.Theme;
import cc.mrbird.febs.policy.service.impl.Policy2ServiceImpl;
import cc.mrbird.febs.policy.service.impl.ThemeServiceImpI;
import cc.mrbird.febs.policy.utils.CreateFileUtil;
import cc.mrbird.febs.policy.utils.CreateJsonForEcharts;
import cc.mrbird.febs.policy.utils.CreateMapData;
import cc.mrbird.febs.policy.utils.CreateTableJson;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * 辅助分析工具数据来源
 * 返回辅助分析工具（政策工具，语义网络，政策导向）所需的json数据以及更新。
 */
@Slf4j
@RestController
@RequestMapping("helpers")
public class AnalyzeHelpersController {
    @Autowired
    private Policy2ServiceImpl policy2Service;
    @Autowired
    private ThemeServiceImpI themeServiceImpI;

    //政策导向
    @GetMapping("getPolicySteeringData")
    public FebsResponse getPolicySteeringData() {
        JSONObject json = CreateFileUtil.readJsonFile("D:/two2/idea/code/science_technology_policy_integration_service/json_for_helpers/dimensionOfKeywords.json");
        if (json != null)
            return new FebsResponse().success().data(json);
        return new FebsResponse().message("未找到响应数据，请联系管理员。");
    }
    @GetMapping("updatePolicySteeringData")
    public FebsResponse updatePolicySteeringData(IntermediateVariable variable) {
        JSONObject json = CreateJsonForEcharts.getDimensionOfKeywordsToJson(variable);
        if (json != null)
            return new FebsResponse().success().data(json);
        return new FebsResponse().message("未找到响应数据，请联系管理员。");
    }
    //政策工具
    @GetMapping("getInstrumentTableData")
    public FebsResponse getInstrumentTableData() {
        JSONObject json = CreateFileUtil.readJsonFile("D:/two2/idea/code/science_technology_policy_integration_service/json_for_helpers/instrumentTableData.json");
        if (json != null)
            return new FebsResponse().success().data(json);
        return new FebsResponse().message("未找到响应数据，请联系管理员。");
    }
    @GetMapping("updateInstrumentTableData")
    public FebsResponse updateInstrumentTableData(IntermediateVariable variable) {
        JSONObject json = CreateJsonForEcharts.getPolicyInstrumentTableJsonData(variable);
        if (json != null)
            return new FebsResponse().success().data(json);
        return new FebsResponse().message("未找到响应数据，请联系管理员。");
    }

    @GetMapping("updateThemeTableData")
    public FebsResponse updateThemeTableData(IntermediateVariable variable) {
        String type=null;
        String createTimeFrom=null;
        String createTimeTo=null;
        if(variable.getRegionId()!=null&&!"".equals(variable.getRegionId().trim())){
            type=variable.getRegionId();
        }
        if(variable.getCreateTimeFrom()!=null&&!"".equals(variable.getCreateTimeFrom().trim())){
            createTimeFrom=variable.getCreateTimeFrom();
        }
        if(variable.getCreateTimeTo()!=null&&!"".equals(variable.getCreateTimeTo().trim())){
            createTimeTo=variable.getCreateTimeTo();
        }
        Policy policy=new Policy();
        policy.setType(type);
        policy.setCreateTimeFrom(createTimeFrom);
        policy.setCreateTimeTo(createTimeTo);
        List<Policy> policyList=policy2Service.getPolicyMultiple(policy);
        List<Theme> themeList=themeServiceImpI.getThemeList();
        JSONObject json = CreateTableJson.getPolicyInstrumentTableJsonData(policyList,themeList);
        if (json != null)
            return new FebsResponse().success().data(json);
        return new FebsResponse().message("未找到响应数据，请联系管理员。");
    }
    //政策语义网络关键词
    @GetMapping("getKeywords")
    public FebsResponse getKeywords() {
        JSONObject json = CreateFileUtil.readJsonFile("D:/two2/idea/code/science_technology_policy_integration_service/json_for_helpers/policyKeywords.json");
        if (json != null)
            return new FebsResponse().success().data(json);
        return new FebsResponse().message("未找到响应数据，请联系管理员。");
    }
}
