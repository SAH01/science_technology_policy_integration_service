package cc.mrbird.febs.policy.service.impl;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.policy.entity.PolicyType;
import cc.mrbird.febs.policy.mapper.PolicyTypeMapper;
import cc.mrbird.febs.policy.service.IPolicyTypeService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PolicyTypeImpI extends ServiceImpl<PolicyTypeMapper, PolicyType> implements IPolicyTypeService {
    @Override
    public IPage<PolicyType> findPolicyDetail(PolicyType policytype, QueryRequest request) {
        Page<PolicyType> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findPolicyDetailPage(page, policytype);
    }
    @Override
    public PolicyType getPolicyTypeById(int id) {
        return this.baseMapper.findById(id);
    }
}
