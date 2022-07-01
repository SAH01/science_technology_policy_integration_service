package cc.mrbird.febs.policy.service.impl;
import cc.mrbird.febs.policy.entity.PolicyNeo4j;
import cc.mrbird.febs.policy.mapper.PolicyNeo4jMapper;
import cc.mrbird.febs.policy.service.IPolicyNeo4jService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PolicyNeo4jServiceImpI extends ServiceImpl<PolicyNeo4jMapper, PolicyNeo4j> implements IPolicyNeo4jService {
    @Override
    public List<PolicyNeo4j> getPolicyNeo4jList(){return this.baseMapper.getPolicyNeo4jList();}
    @Override
    public PolicyNeo4j getPolicyNeo4j(String policyname){return this.baseMapper.getPolicyNeo4j(policyname);}
}
