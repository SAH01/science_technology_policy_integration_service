package cc.mrbird.febs.policy.service.impl;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.policy.entity.PolicyEnter;
import cc.mrbird.febs.policy.mapper.PolicyIndexingMapper;
import cc.mrbird.febs.policy.service.IPolicyIndexingService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Administrator
 * @date 2022-04-28 20:38
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PolicyIndexingServiceImpl extends ServiceImpl<PolicyIndexingMapper, PolicyEnter> implements IPolicyIndexingService {
	@Override
	public IPage<PolicyEnter> findPolicyIndexingDetail(PolicyEnter policy, QueryRequest request) {
		Page<PolicyEnter> page = new Page<>(request.getPageNum(), request.getPageSize());
		SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
		return this.baseMapper.findPolicyDetailPage(page, policy);
	}

	@Override
	public IPage<PolicyEnter> findPolicyIndexingDetailgById(String policyName,String policyId,String policyOrgan,String policyTime,String policyType,
	                                                        String policyPublisher, QueryRequest request) {
		Page<PolicyEnter> page = new Page<>(request.getPageNum(), request.getPageSize());
		SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
		return this.baseMapper.findPolicyById(page, policyName,policyId,policyOrgan,policyTime,policyType,policyPublisher);
	}

	@Override
	@Transactional
	public void deletePolicyIndexing(int id) {
		this.baseMapper.deleteById(id);
	}

	@Override
	public void updatePolicyIndexing(PolicyEnter id) {
		this.baseMapper.updateById(id);
	}

	@Override
	public PolicyEnter findById(int policyIndexingId) {
		return this.baseMapper.findById(policyIndexingId);
	}
}