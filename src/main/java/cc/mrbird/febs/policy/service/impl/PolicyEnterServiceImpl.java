package cc.mrbird.febs.policy.service.impl;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.policy.entity.PolicyEnter;
import cc.mrbird.febs.policy.mapper.PolicyEnterMapper;
import cc.mrbird.febs.policy.service.IPolicyEnterService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PolicyEnterServiceImpl extends ServiceImpl<PolicyEnterMapper, PolicyEnter> implements IPolicyEnterService {
    @Override
    public IPage<PolicyEnter> findPolicyCrawlingDetail(PolicyEnter policy, QueryRequest request) {
        Page<PolicyEnter> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findPolicyDetailPage(page, policy);
    }
    @Override
    @Transactional
    public void createPolicyCrawling(PolicyEnter policy) {
        this.baseMapper.insert(policy);
    }
    @Override
    @Transactional
    public void deletePolicyCrawling(int id) {
        this.baseMapper.deleteById(id);
    }

    @Override
    public void updatePolicyCrawling(PolicyEnter id) {
        this.baseMapper.updateById(id);
    }

    @Override
    public List<PolicyEnter> getPolicyCrawlingList(){return this.baseMapper.getPolicyCrawlingList();}
    @Override
    public PolicyEnter findById(int policyId) {
        return this.baseMapper.findById(policyId);
    }
}
