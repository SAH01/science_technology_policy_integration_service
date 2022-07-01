package cc.mrbird.febs.policy.mapper;

import cc.mrbird.febs.policy.entity.FormulaEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * @author MrBird
 */
public interface FormulaEntityMapper extends BaseMapper<FormulaEntity> {
    /**
     * 递归删除实体指标
     *
     * @param formulaId formulaId
     */
    void deleteFormulaEntitys(String formulaId);
}
