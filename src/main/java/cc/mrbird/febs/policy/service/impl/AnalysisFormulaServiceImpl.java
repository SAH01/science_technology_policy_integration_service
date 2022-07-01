package cc.mrbird.febs.policy.service.impl;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.common.utils.TreeUtil;
import cc.mrbird.febs.policy.entity.AnalysisFormula;
import cc.mrbird.febs.policy.entity.FormulaAndEntityTree;
import cc.mrbird.febs.policy.entity.FormulaEntity;
import cc.mrbird.febs.policy.mapper.AnalysisFormulaMapper;
import cc.mrbird.febs.policy.service.IAnalysisFormulaService;
import cc.mrbird.febs.policy.service.IFormulaEntityService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author WangRY
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AnalysisFormulaServiceImpl extends ServiceImpl<AnalysisFormulaMapper, AnalysisFormula> implements IAnalysisFormulaService {

    @Autowired
    private IFormulaEntityService formulaEntityService;

    @Override
    public List<FormulaAndEntityTree<AnalysisFormula>> findFormula() {
        List<AnalysisFormula> formulas = this.baseMapper.selectList(new QueryWrapper<>());
        List<FormulaAndEntityTree<AnalysisFormula>> trees = this.convertDepts(formulas);
        return TreeUtil.buildFormulaAndEntityTree(trees);
    }

    @Override
    public List<FormulaAndEntityTree<AnalysisFormula>> findFormula(AnalysisFormula formula) {

        List<AnalysisFormula> formulas = this.baseMapper.getAnalysisFormulas(formula);
        List<FormulaAndEntityTree<AnalysisFormula>> trees = this.convertDepts(formulas);
        return TreeUtil.buildFormulaAndEntityTree(trees);
    }

    @Override
    public List<AnalysisFormula> findFormulas(AnalysisFormula formula, QueryRequest request) {
        QueryWrapper<AnalysisFormula> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(formula.getFormulaName()))
            queryWrapper.lambda().eq(AnalysisFormula::getFormulaName, formula.getFormulaName());
        SortUtil.handleWrapperSort(request, queryWrapper, "orderNum", FebsConstant.ORDER_ASC, true);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public void createFormula(AnalysisFormula formula) {
        Long parentId = formula.getParentId();
        if (parentId == null)
            formula.setParentId(0L);

        formula.setFormulaName(formula.getFormulaName().replace("&amp;","&"));
        formula.setFormulaContent(formula.getFormulaContent().replace("&amp;","&"));
        formula.setCreateTime(new Date());
        this.save(formula);
        saveEntityIndexs(formula);
    }

    @Override
    public void updateFormula(AnalysisFormula formula) {
        formula.setFormulaName(formula.getFormulaName().replace("&amp;","&"));
        formula.setFormulaContent(formula.getFormulaContent().replace("&amp;","&"));
        formula.setModifyTime(new Date());
        this.baseMapper.updateById(formula);
        List<String> formulaIdList = new ArrayList<>();
        formulaIdList.add(String.valueOf(formula.getFormulaId()));
        this.formulaEntityService.deleteFormulaEntitysByFormulaId(formulaIdList);
        saveEntityIndexs(formula);

    }

    @Override
    public void deleteFormulas(String[] formulaIds) {
        this.delete(Arrays.asList(formulaIds));
    }

    private List<FormulaAndEntityTree<AnalysisFormula>> convertDepts(List<AnalysisFormula> formulas) {
        List<FormulaAndEntityTree<AnalysisFormula>> trees = new ArrayList<>();
        formulas.forEach(formula -> {
            FormulaAndEntityTree<AnalysisFormula> tree = new FormulaAndEntityTree<>();
            tree.setId(String.valueOf(formula.getFormulaId()));
            tree.setParentId(String.valueOf(formula.getParentId()));
            tree.setName(formula.getFormulaName());
            tree.setFormulaData(formula);
            trees.add(tree);
        });
        return trees;
    }

    private void delete(List<String> formulaIds) {
        removeByIds(formulaIds);

        LambdaQueryWrapper<AnalysisFormula> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(AnalysisFormula::getParentId, formulaIds);
        List<AnalysisFormula> formulas = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(formulas)) {
            List<String> formulaIdList = new ArrayList<>();
            formulas.forEach(d -> formulaIdList.add(String.valueOf(d.getFormulaId())));
            this.delete(formulaIdList);
        }
    }

    private void saveEntityIndexs(AnalysisFormula formula) {
        if (StringUtils.isNotBlank(formula.getEntityIndexIds())) {
            String[] entityIndexIds = formula.getEntityIndexIds().split(StringPool.COMMA);
            List<FormulaEntity> formulaEntitys = new ArrayList<>();
            Arrays.stream(entityIndexIds).forEach(entityIndexId -> {
                FormulaEntity formulaEntity = new FormulaEntity();
                formulaEntity.setEntityIndexId(Long.valueOf(entityIndexId));
                formulaEntity.setFormulaId(formula.getFormulaId());
                formulaEntitys.add(formulaEntity);
            });
            formulaEntityService.saveBatch(formulaEntitys);
        }
    }
}
