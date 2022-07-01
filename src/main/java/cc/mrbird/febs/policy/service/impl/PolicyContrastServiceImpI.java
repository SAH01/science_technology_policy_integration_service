package cc.mrbird.febs.policy.service.impl;

import cc.mrbird.febs.policy.entity.PolicyContrast;
import cc.mrbird.febs.policy.mapper.PolicyContrastMapper;
import cc.mrbird.febs.policy.service.IPolicyContrastService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PolicyContrastServiceImpI extends ServiceImpl<PolicyContrastMapper, PolicyContrast> implements IPolicyContrastService {
    @Override
    public PolicyContrast getPolicyContrast(int id){return this.baseMapper.getPolicyContrast(id);}
    @Override
    public List<PolicyContrast> getPolicyContrastListByCity(int kindid){return this.baseMapper.getPolicyContrastListByCity(kindid);}
    @Override
    public List<PolicyContrast> getPolicyContrastListByYear(int kindid){return this.baseMapper.getPolicyContrastListByYear(kindid);}
    @Override
    public List<PolicyContrast> getPolicyContrastListGroupKind(int kindid){return this.baseMapper.getPolicyContrastListGroupKind(kindid);}
    @Override
    public List<PolicyContrast> getPolicyContrastListByTime(int kindid){return this.baseMapper.getPolicyContrastListByTime(kindid);}
    @Override
    public List<PolicyContrast> getPolicyContrastListByName(String name){return this.baseMapper.getPolicyContrastListByName(name);}
    @Override
    public List<PolicyContrast> getPolicyContrastList(int kindid){return this.baseMapper.getPolicyContrastList(kindid);}
    @Override
    public List<PolicyContrast> getgroupList(){return this.baseMapper.getgroupList();}
    @Override
    public List<PolicyContrast> getAllsum(){return this.baseMapper.getAllsum();}
    @Override
    public List<PolicyContrast> getQuestionNumPolicyContrast(List<String> organlist,List<String> rangelist,List<String> themelist,List<String> yearlist,List<String> namelist){return this.baseMapper.getQuestionNumPolicyContrast(organlist,rangelist,themelist,yearlist,namelist);}
    @Override
    @Transactional
    public void createPolicyContrast(PolicyContrast policyContrast) {

        this.save(policyContrast);
    }

    @Override
    @Transactional
    public void updatePolicyContrast(PolicyContrast policyContrast) {

        this.baseMapper.updateById(policyContrast);
    }

    @Override
    @Transactional
    public void deletePolicyContrasts(int kindid) {
        this.baseMapper.deleteById(kindid);
    }


}
