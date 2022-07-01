package cc.mrbird.febs.policy.controller;

import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.policy.entity.PolicyKind;
import cc.mrbird.febs.policy.entity.PolicyType;
import cc.mrbird.febs.policy.service.IPolicyKindService;
import cc.mrbird.febs.policy.service.IPolicyTypeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("policykind")
public class PolicyKindControler extends BaseController {
    @Autowired
    private IPolicyKindService policyKindService;
    @GetMapping("list")
    @RequiresPermissions("user:view")
    public FebsResponse policyKindList(PolicyKind policytype, QueryRequest request) {
        Map<String, Object> dataTable = getDataTable(this.policyKindService.findPolicyDetail(policytype, request));
        return new FebsResponse().success().data(dataTable);
    }
}
