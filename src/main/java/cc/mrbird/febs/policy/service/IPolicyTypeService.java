package cc.mrbird.febs.policy.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.policy.entity.PolicyType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IPolicyTypeService extends IService<PolicyType> {
    /**
     * 查找政策详细信息
     *
     * @param policytype 政策对象，用于传递查询条件
     * @return Ipage
     */
    IPage<PolicyType> findPolicyDetail(PolicyType policytype, QueryRequest request);
    PolicyType getPolicyTypeById(int id);
}
