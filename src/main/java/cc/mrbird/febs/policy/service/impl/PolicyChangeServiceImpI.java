package cc.mrbird.febs.policy.service.impl;

import cc.mrbird.febs.policy.entity.PolicyChange;
import cc.mrbird.febs.policy.mapper.PolicyChangeMapper;
import cc.mrbird.febs.policy.service.IPolicyChangeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PolicyChangeServiceImpI extends ServiceImpl<PolicyChangeMapper, PolicyChange> implements IPolicyChangeService {
    @Override
    public List<PolicyChange> getPolicyChangeList(String type){return this.baseMapper.getPolicyChangeList(type);}
    @Override
    public PolicyChange getPolicyChange(int id){return this.baseMapper.getPolicyChange(id);}
}
