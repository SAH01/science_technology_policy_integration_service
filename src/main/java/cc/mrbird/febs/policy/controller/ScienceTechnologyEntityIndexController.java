package cc.mrbird.febs.policy.controller;

import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.policy.entity.ScienceTechnologyEntityIndex;
import cc.mrbird.febs.policy.entity.FormulaAndEntityTree;
import cc.mrbird.febs.policy.service.IScienceTechnologyEntityIndexService;
import cc.mrbird.febs.policy.service.IScienceTechnologyEntityIndexService;
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
@RequestMapping("entityIndex")
public class ScienceTechnologyEntityIndexController {
    @Autowired
    private IScienceTechnologyEntityIndexService scienceTechnologyEntityIndexService;

    @GetMapping("select/tree")
    public List<FormulaAndEntityTree<ScienceTechnologyEntityIndex>> getFormulaTree() throws FebsException {
        try {
            return this.scienceTechnologyEntityIndexService.findEntityIndex();
        } catch (Exception e) {
            String message = "获取科技实体树失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("tree")
    public FebsResponse getFormulaTree(ScienceTechnologyEntityIndex entityIndex) throws FebsException {
        try {
            List<FormulaAndEntityTree<ScienceTechnologyEntityIndex>> entityIndexTreeList = this.scienceTechnologyEntityIndexService.findEntityIndex(entityIndex);
            return new FebsResponse().success().data(entityIndexTreeList);
        } catch (Exception e) {
            String message = "获取科技实体树失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping
    @RequiresPermissions("scienceTechnologyEntityIndex:add")
    public FebsResponse addFormula(@Valid ScienceTechnologyEntityIndex entityIndex) throws FebsException {
        try {
            entityIndex.setEntityIndexName(entityIndex.getEntityIndexName().replace("&amp;","&"));
            entityIndex.setEntityIndexContent(entityIndex.getEntityIndexContent().replaceAll("&amp;","&"));
            this.scienceTechnologyEntityIndexService.createEntityIndex(entityIndex);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "新增科技实体失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("delete/{entityIndexIds}")
    @RequiresPermissions("scienceTechnologyEntityIndex:delete")
    public FebsResponse deleteFormulas(@NotBlank(message = "{required}") @PathVariable String entityIndexIds) throws FebsException {
        try {
            String[] ids = entityIndexIds.split(StringPool.COMMA);
            this.scienceTechnologyEntityIndexService.deleteEntityIndexs(ids);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "删除科技实体失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @PostMapping("update")
    @RequiresPermissions("scienceTechnologyEntityIndex:update")
    public FebsResponse updateFormula(@Valid ScienceTechnologyEntityIndex entityIndex) throws FebsException {
        try {
            entityIndex.setEntityIndexName(entityIndex.getEntityIndexName().replace("&amp;","&"));
            entityIndex.setEntityIndexContent(entityIndex.getEntityIndexContent().replaceAll("&amp;","&"));
            this.scienceTechnologyEntityIndexService.updateEntityIndex(entityIndex);
            return new FebsResponse().success();
        } catch (Exception e) {
            String message = "修改科技实体失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("excel")
    @RequiresPermissions("scienceTechnologyEntityIndex:export")
    public void export(ScienceTechnologyEntityIndex entityIndex, QueryRequest request, HttpServletResponse response) throws FebsException {
        try {
            List<ScienceTechnologyEntityIndex> entityIndexs = this.scienceTechnologyEntityIndexService.findEntityIndexs(entityIndex, request);
            ExcelKit.$Export(ScienceTechnologyEntityIndex.class, response).downXlsx(entityIndexs, false);
        } catch (Exception e) {
            String message = "导出Excel失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
