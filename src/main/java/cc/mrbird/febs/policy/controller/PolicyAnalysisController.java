package cc.mrbird.febs.policy.controller;

import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.policy.entity.PolicyAnalysis;
import cc.mrbird.febs.policy.service.IPolicy2Service;
import cc.mrbird.febs.policy.service.IPolicyAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("policyanalysis")
public class PolicyAnalysisController extends BaseController {
    @Autowired
    private IPolicyAnalysisService policyAnalysisService;
    @GetMapping("list1")
    @RequiresPermissions("user:view")
    public FebsResponse policyanalysisList(PolicyAnalysis policyanalysis, QueryRequest request) {
        Map<String, Object> dataTable = getDataTable(this.policyAnalysisService.findPolicyDetail(policyanalysis, request));
        return new FebsResponse().success().data(dataTable);
    }
}
