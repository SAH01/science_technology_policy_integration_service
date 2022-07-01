package cc.mrbird.febs.policy.controller;


import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.policy.entity.PolicyDept;
import cc.mrbird.febs.policy.entity.PolicyDimensionalityTree;
import cc.mrbird.febs.policy.service.IPolicyDeptService;
import cc.mrbird.febs.system.entity.Dept;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author Wang Renyi
 */
@Slf4j
@RestController
@RequestMapping("policyDept")
public class PolicyDeptController {

    @Autowired
    private IPolicyDeptService deptService;

    @GetMapping("select/tree")
    public List<PolicyDimensionalityTree<PolicyDept>> getDeptTree() throws FebsException {
        try {
            return this.deptService.findDepts();
        } catch (Exception e) {
            String message = "获取政策发布部门树失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("tree")
    public FebsResponse getDeptTree(PolicyDept dept) throws FebsException {
        try {
            List<PolicyDimensionalityTree<PolicyDept>> depts = this.deptService.findDepts(dept);
            return new FebsResponse().success().data(depts);
        } catch (Exception e) {
            String message = "获取政策发布部门树失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping
    @RequiresPermissions("dept:add")
    public FebsResponse addDept(@Valid PolicyDept dept) throws FebsException {
        try {
            this.deptService.createDept(dept);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "新增政策发布部门失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("delete/{deptIds}")
    @RequiresPermissions("dept:delete")
    public FebsResponse deleteDepts(@NotBlank(message = "{required}") @PathVariable String deptIds) throws FebsException {
        try {
            String[] ids = deptIds.split(StringPool.COMMA);
            this.deptService.deleteDepts(ids);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "删除政策发布部门失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("update")
    @RequiresPermissions("dept:update")
    public FebsResponse updateDept(@Valid PolicyDept dept) throws FebsException {
        try {
            this.deptService.updateDept(dept);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "修改政策发布部门失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("excel")
    @RequiresPermissions("dept:export")
    public void export(PolicyDept dept, QueryRequest request, HttpServletResponse response) throws FebsException {
        try {
            List<PolicyDept> depts = this.deptService.findDepts(dept, request);
            ExcelKit.$Export(Dept.class, response).downXlsx(depts, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
