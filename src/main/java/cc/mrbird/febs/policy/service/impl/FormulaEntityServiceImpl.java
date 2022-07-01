package cc.mrbird.febs.policy.service.impl;

import cc.mrbird.febs.policy.entity.FormulaEntity;
import cc.mrbird.febs.policy.mapper.FormulaEntityMapper;
import cc.mrbird.febs.policy.service.IFormulaEntityService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author WangRY
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class FormulaEntityServiceImpl extends ServiceImpl<FormulaEntityMapper, FormulaEntity> implements IFormulaEntityService {

    @Override
    @Transactional
    public void deleteFormulaEntitysByFormulaId(List<String> formulaIds) {
        this.baseMapper.delete(new QueryWrapper<FormulaEntity>().lambda().in(FormulaEntity::getFormulaId, formulaIds));
    }

    @Override
    @Transactional
    public void deleteFormulaEntitysByEntityIndexId(List<String> entityIndexIds) {
        this.baseMapper.delete(new QueryWrapper<FormulaEntity>().lambda().in(FormulaEntity::getFormulaId, entityIndexIds));
    }

    @Override
    @Transactional
    public void deleteFormulaEntitys(String entityIndexId) {
        this.baseMapper.deleteFormulaEntitys(entityIndexId);
    }
}
