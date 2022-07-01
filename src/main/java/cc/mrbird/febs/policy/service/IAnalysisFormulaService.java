package cc.mrbird.febs.policy.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.policy.entity.AnalysisFormula;
import cc.mrbird.febs.policy.entity.FormulaAndEntityTree;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author WangRY
 */
public interface IAnalysisFormulaService extends IService<AnalysisFormula> {
    /**
     * 获取公式树（下拉选使用）
     *
     * @return 公式树集合
     */
    List<FormulaAndEntityTree<AnalysisFormula>> findFormula();

    /**
     * 获取公式列表（树形列表）
     *
     * @param formula 公式对象（传递查询参数）
     * @return 公式树
     */
    List<FormulaAndEntityTree<AnalysisFormula>> findFormula(AnalysisFormula formula);

    /**
     * 获取公式树（供Excel导出）
     *
     * @param formula    公式对象（传递查询参数）
     * @param request QueryRequest
     * @return List<AnalysisFormula>
     */
    List<AnalysisFormula> findFormulas(AnalysisFormula formula, QueryRequest request);

    /**
     * 新增公式
     *
     * @param formula 公式对象
     */
    void createFormula(AnalysisFormula formula);

    /**
     * 修改公式
     *
     * @param formula 公式对象
     */
    void updateFormula(AnalysisFormula formula);

    /**
     * 删除公式
     *
     * @param formulaIds 公式 ID集合
     */
    void deleteFormulas(String[] formulaIds);


}
