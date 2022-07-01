package cc.mrbird.febs.policy.service.impl;

import cc.mrbird.febs.policy.entity.PolicyNew;
import cc.mrbird.febs.policy.mapper.PolicyNewMapper;
import cc.mrbird.febs.policy.service.IPolicyNew2Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PolicyNew2ServiceImpI extends ServiceImpl<PolicyNewMapper, PolicyNew> implements IPolicyNew2Service {
    @Override
    public List<PolicyNew> getPolicyNewList(){return this.baseMapper.getPolicyNewList();}
}
