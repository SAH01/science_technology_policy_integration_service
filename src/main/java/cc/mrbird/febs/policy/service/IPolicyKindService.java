package cc.mrbird.febs.policy.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.policy.entity.PolicyKind;
import cc.mrbird.febs.policy.entity.PolicyType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IPolicyKindService extends IService<PolicyKind> {

    IPage<PolicyKind> findPolicyDetail(PolicyKind policykind, QueryRequest request);
    PolicyKind getPolicyKindById(int id);
}
