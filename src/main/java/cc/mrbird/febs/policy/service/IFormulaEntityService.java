package cc.mrbird.febs.policy.service;

import cc.mrbird.febs.policy.entity.FormulaEntity;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author WangRY
 */
public interface IFormulaEntityService extends IService<FormulaEntity> {

    /**
     * 通过政策公式 id 删除
     *
     * @param formulaIds 政策公式 id
     */
    void deleteFormulaEntitysByFormulaId(List<String> formulaIds);

    /**
     * 通过科技实体指标id 删除
     *
     * @param entityIndexIds 科技实体指标id
     */
    void deleteFormulaEntitysByEntityIndexId(List<String> entityIndexIds);

    /**
     * 递归删除科技实体指标
     *
     * @param entityIndexId entityIndexId
     */
    void deleteFormulaEntitys(String entityIndexId);
}
