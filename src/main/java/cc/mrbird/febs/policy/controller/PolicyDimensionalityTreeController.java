package cc.mrbird.febs.policy.controller;


import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.policy.entity.PolicyCategory;
import cc.mrbird.febs.policy.entity.PolicyDimensionalityTree;
import cc.mrbird.febs.policy.entity.PolicyIndustrial;
import cc.mrbird.febs.policy.entity.PolicyInstrument;
import cc.mrbird.febs.policy.service.IPolicyCategoryService;
import cc.mrbird.febs.policy.service.IPolicyIndustrialService;
import cc.mrbird.febs.policy.service.IPolicyInstrumentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("classify")
public class PolicyDimensionalityTreeController {

    @Autowired
    private IPolicyIndustrialService industrialService;
    @Autowired
    private IPolicyInstrumentService instrumentService;
    @Autowired
    private IPolicyCategoryService categoryService;

    @GetMapping("select/industrial")
    public List<PolicyDimensionalityTree<PolicyIndustrial>> getIndustrialTree() throws FebsException {
        try {
            return this.industrialService.findDepts();
        } catch (Exception e) {
            String message = "获取产业划分树失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
    @GetMapping("industrialTree")
    public FebsResponse getIndustrialTree(PolicyIndustrial dept) throws FebsException {
        try {
            List<PolicyDimensionalityTree<PolicyIndustrial>> depts = this.industrialService.findDepts(dept);
            return new FebsResponse().success().data(depts);
        } catch (Exception e) {
            String message = "获取产业划分树失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("select/instrument")
    public List<PolicyDimensionalityTree<PolicyInstrument>> getInstrumentTree() throws FebsException {
        try {
            return this.instrumentService.findDepts();
        } catch (Exception e) {
            String message = "获取政策工具树失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
    @GetMapping("instrumentTree")
    public FebsResponse getInstrumentTree(PolicyInstrument dept) throws FebsException {
        try {
            List<PolicyDimensionalityTree<PolicyInstrument>> depts = this.instrumentService.findDepts(dept);
            return new FebsResponse().success().data(depts);
        } catch (Exception e) {
            String message = "获取政策工具树失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("select/category")
    public List<PolicyDimensionalityTree<PolicyCategory>> getCategoryTree() throws FebsException {
        try {
            return this.categoryService.findDepts();
        } catch (Exception e) {
            String message = "获取政策分类树失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
    @GetMapping("categoryTree")
    public FebsResponse getCategoryTree(PolicyCategory dept) throws FebsException {
        try {
            List<PolicyDimensionalityTree<PolicyCategory>> depts = this.categoryService.findDepts(dept);
            return new FebsResponse().success().data(depts);
        } catch (Exception e) {
            String message = "获取政策分类树失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
