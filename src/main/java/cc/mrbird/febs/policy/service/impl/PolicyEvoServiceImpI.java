package cc.mrbird.febs.policy.service.impl;

import cc.mrbird.febs.policy.entity.PolicyEvo;
import cc.mrbird.febs.policy.mapper.PolicyEvoMapper;
import cc.mrbird.febs.policy.service.IPolicyEvoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PolicyEvoServiceImpI extends ServiceImpl<PolicyEvoMapper, PolicyEvo> implements IPolicyEvoService {
    @Override
    public List<PolicyEvo> getPolicyEvoListByTime(String type){return this.baseMapper.getPolicyEvoListByTime(type);}
    @Override
    public List<PolicyEvo> getPolicyEvoGroupByType(){return this.baseMapper.getPolicyEvoGroupByType();}
    @Override
    public List<PolicyEvo> getPolicyEvoList(String type){return this.baseMapper.getPolicyEvoList(type);}
    @Override
    public List<PolicyEvo> getPolicyEvoByNameList(String policyname){return this.baseMapper.getPolicyEvoByNameList(policyname);}
    @Override
    @Transactional
    public void createPolicyEvo(PolicyEvo policyEvo) {

        this.save(policyEvo);
    }

    @Override
    @Transactional
    public void updatePolicyEvo(PolicyEvo policyEvo) {

        this.baseMapper.updateById(policyEvo);
    }

    @Override
    @Transactional
    public void deletePolicyEvo(int policyid) {
        this.baseMapper.deleteById(policyid);
    }
}
