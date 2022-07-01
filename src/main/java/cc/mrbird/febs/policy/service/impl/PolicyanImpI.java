package cc.mrbird.febs.policy.service.impl;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.policy.entity.Policyan;
import cc.mrbird.febs.policy.mapper.PolicyanMapper;
import cc.mrbird.febs.policy.service.IPolicyanService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PolicyanImpI extends ServiceImpl<PolicyanMapper, Policyan> implements IPolicyanService {
    @Override
    public List<Policyan> getPolicyList(int policyid){return this.baseMapper.getPolicyList(policyid);}
    @Override
    public IPage<Policyan> findPolicyDetail(Policyan policyan, QueryRequest request) {
        Page<Policyan> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findPolicyDetailPage(page, policyan);
    }
}
