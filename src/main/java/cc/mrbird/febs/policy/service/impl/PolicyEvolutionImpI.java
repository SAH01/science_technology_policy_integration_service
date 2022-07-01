package cc.mrbird.febs.policy.service.impl;

import cc.mrbird.febs.policy.entity.PolicyEvolution;
import cc.mrbird.febs.policy.mapper.PolicyEvolutionMapper;
import cc.mrbird.febs.policy.service.IPolicyEvolutionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PolicyEvolutionImpI extends ServiceImpl<PolicyEvolutionMapper, PolicyEvolution> implements IPolicyEvolutionService {
    @Override
    public List<PolicyEvolution> getPolicyEvolutionList(String type){return this.baseMapper.getPolicyEvolutionList(type);}
    @Override
    public List<PolicyEvolution> getPolicyEvolutionByNameList(String name){return this.baseMapper.getPolicyEvolutionByNameList(name);}
}
