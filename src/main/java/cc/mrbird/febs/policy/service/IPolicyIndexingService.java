package cc.mrbird.febs.policy.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.policy.entity.PolicyEnter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @BelongsProject: science
 * @BelongsPackage: cc.mrbird.febs.policy.service
 * @Author: Administrator
 * @CreateTime: 2022-04-28 20:28
 * @Description: 标引管理接口
 */
public interface IPolicyIndexingService extends IService<PolicyEnter> {
	IPage<PolicyEnter> findPolicyIndexingDetail(PolicyEnter policy, QueryRequest request);
	void deletePolicyIndexing(int id);
	void updatePolicyIndexing(PolicyEnter id);
	PolicyEnter findById(int policyIndexingId);
	IPage<PolicyEnter> findPolicyIndexingDetailgById(String policyName,String policyId,
	                                                 String policyOrgan,String policyTime,
	                                                 String policyType,String policyPublisher, QueryRequest request);
}
