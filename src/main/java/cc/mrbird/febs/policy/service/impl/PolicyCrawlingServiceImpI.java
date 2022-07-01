package cc.mrbird.febs.policy.service.impl;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.policy.entity.PolicyCrawling;
import cc.mrbird.febs.policy.mapper.PolicyCrawlingMapper;
import cc.mrbird.febs.policy.service.IPolicyCrawlingService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PolicyCrawlingServiceImpI  extends ServiceImpl<PolicyCrawlingMapper, PolicyCrawling> implements IPolicyCrawlingService {
    @Override
    public IPage<PolicyCrawling> findPolicyCrawlingDetail(PolicyCrawling policy, QueryRequest request) {
        Page<PolicyCrawling> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "year", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findPolicyDetailPage(page, policy);
    }
    @Override
    @Transactional
    public void createPolicyCrawling(PolicyCrawling policy) {
        this.baseMapper.insert(policy);
    }
    @Override
    @Transactional
    public void deletePolicyCrawling(int id) {
        this.baseMapper.deleteById(id);
    }
    @Override
    public List<PolicyCrawling> getPolicyCrawlingList(){return this.baseMapper.getPolicyCrawlingList();}
    @Override
    public PolicyCrawling findById(int policyId) {
        return this.baseMapper.findById(policyId);
    }
}
