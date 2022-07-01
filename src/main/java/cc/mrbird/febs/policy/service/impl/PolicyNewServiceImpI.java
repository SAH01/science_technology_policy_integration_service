package cc.mrbird.febs.policy.service.impl;

import cc.mrbird.febs.policy.entity.PolicyNewAnalysis;
import cc.mrbird.febs.policy.mapper.PolicyNewAnalysisMapper;
import cc.mrbird.febs.policy.service.IPolicyNewService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PolicyNewServiceImpI extends ServiceImpl<PolicyNewAnalysisMapper, PolicyNewAnalysis> implements IPolicyNewService {
    @Override
    public List<PolicyNewAnalysis> getPolicyNewList(int policyid){return this.baseMapper.getPolicyNewList(policyid);}
}
