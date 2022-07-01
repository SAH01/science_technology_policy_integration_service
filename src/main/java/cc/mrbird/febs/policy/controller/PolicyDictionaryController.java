package cc.mrbird.febs.policy.controller;



import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.policy.entity.PolicyDictionary;
import cc.mrbird.febs.policy.service.impl.PolicyDictionaryServiceImpI;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("policyDictionary")
public class PolicyDictionaryController extends BaseController {
    @Autowired
    private PolicyDictionaryServiceImpI policyDictionaryService;
    @GetMapping("list")
    @RequiresPermissions("user:view")
    public FebsResponse policyList(PolicyDictionary policy, QueryRequest request) {

        Map<String, Object> dataTable = getDataTable(this.policyDictionaryService.findPolicyDetail(policy, request));
        return new FebsResponse().success().data(dataTable);
    }

    @PostMapping
    @RequiresPermissions("role:add")
    @ControllerEndpoint(operation = "新增词语", exceptionMessage = "新增词语失败")
    public FebsResponse addRole(@Valid PolicyDictionary policy) {
        this.policyDictionaryService.createpolicyDictionary(policy);
        return new FebsResponse().success();
    }

    @GetMapping("delete/{ids}")
    @RequiresPermissions("role:delete")
    @ControllerEndpoint(operation = "删除词语", exceptionMessage = "删除词语失败")
    public FebsResponse deleteRoles(@NotBlank(message = "{required}") @PathVariable String ids) {
        System.out.println(ids);
        this.policyDictionaryService.deletepolicyDictionary(ids);
        return new FebsResponse().success();
    }

    @PostMapping("update")
    @RequiresPermissions("role:update")
    @ControllerEndpoint(operation = "修改词语", exceptionMessage = "修改词语失败")
    public FebsResponse updateRole(PolicyDictionary policy) {
        this.policyDictionaryService.updatepolicyDictionary(policy);
        return new FebsResponse().success();
    }
}
