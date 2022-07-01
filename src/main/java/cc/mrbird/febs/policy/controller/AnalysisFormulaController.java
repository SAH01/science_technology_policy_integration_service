package cc.mrbird.febs.policy.controller;

import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.policy.entity.AnalysisFormula;
import cc.mrbird.febs.policy.entity.FormulaAndEntityTree;
import cc.mrbird.febs.policy.service.IAnalysisFormulaService;
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
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("analysisFormula")
public class AnalysisFormulaController {
    @Autowired
    private IAnalysisFormulaService analysisFormulaService;

    @GetMapping("select/tree")
    public List<FormulaAndEntityTree<AnalysisFormula>> getFormulaTree() throws FebsException {
        try {
            return this.analysisFormulaService.findFormula();
        } catch (Exception e) {
            String message = "获取政策公式树失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("tree")
    public FebsResponse getFormulaTree(AnalysisFormula formula) throws FebsException {
        try {
            List<FormulaAndEntityTree<AnalysisFormula>> formulaTreeList = this.analysisFormulaService.findFormula(formula);
            return new FebsResponse().success().data(formulaTreeList);
        } catch (Exception e) {
            String message = "获取政策公式树失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping
    @RequiresPermissions("analysisFormula:add")
    public FebsResponse addFormula(@Valid AnalysisFormula formula) throws FebsException {
        try {
            this.analysisFormulaService.createFormula(formula);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "新增政策公式失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("delete/{formulaIds}")
    @RequiresPermissions("analysisFormula:delete")
    public FebsResponse deleteFormulas(@NotBlank(message = "{required}") @PathVariable String formulaIds) throws FebsException {
        try {
            String[] ids = formulaIds.split(StringPool.COMMA);
            this.analysisFormulaService.deleteFormulas(ids);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "删除政策公式失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("update")
    @RequiresPermissions("analysisFormula:update")
    public FebsResponse updateFormula(@Valid AnalysisFormula formula) throws FebsException {
        try {
            this.analysisFormulaService.updateFormula(formula);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "修改政策公式失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("excel")
    @RequiresPermissions("analysisFormula:export")
    public void export(AnalysisFormula formula, QueryRequest request, HttpServletResponse response) throws FebsException {
        try {
            List<AnalysisFormula> formulas = this.analysisFormulaService.findFormulas(formula, request);
            ExcelKit.$Export(AnalysisFormula.class, response).downXlsx(formulas, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
