package cc.mrbird.febs.policy.service.impl;

import cc.mrbird.febs.policy.entity.PolicyRank;
import cc.mrbird.febs.policy.mapper.PolicyRankMapper;
import cc.mrbird.febs.policy.service.IPolicyRankService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PolicyRankServiceImpI extends ServiceImpl<PolicyRankMapper, PolicyRank> implements IPolicyRankService {
    @Override
    public List<PolicyRank> getPolicyRankByName(String name){return this.baseMapper.getPolicyRankByName(name);}
    @Override
    public List<PolicyRank> getPolicyRankByNameGroup(String name){return this.baseMapper.getPolicyRankByNameGroup(name);}
    @Override
    public List<PolicyRank> getPolicyRankListBystartid(int startid){return this.baseMapper.getPolicyRankListBystartid(startid);}
}
