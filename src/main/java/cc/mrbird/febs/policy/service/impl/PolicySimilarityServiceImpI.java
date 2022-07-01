package cc.mrbird.febs.policy.service.impl;

import cc.mrbird.febs.policy.entity.PolicySimilarity;
import cc.mrbird.febs.policy.mapper.PolicySimilarityMapper;
import cc.mrbird.febs.policy.service.IPolicySimilarityService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PolicySimilarityServiceImpI extends ServiceImpl<PolicySimilarityMapper, PolicySimilarity> implements IPolicySimilarityService {
    @Override
    public List<PolicySimilarity> getPolicySimilarityByName(String name){return this.baseMapper.getPolicySimilarityByName(name);}
    @Override
    public List<PolicySimilarity> getPolicySimilarityByNameGroup(String name){return this.baseMapper.getPolicySimilarityByNameGroup(name);}
    @Override
    public List<PolicySimilarity> getPolicySimilarityListBystartid(int startid){return this.baseMapper.getPolicySimilarityListBystartid(startid);}
}
