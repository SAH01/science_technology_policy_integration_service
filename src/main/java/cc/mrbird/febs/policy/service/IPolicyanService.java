package cc.mrbird.febs.policy.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.policy.entity.PolicyAnalysis;
import cc.mrbird.febs.policy.entity.Policyan;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IPolicyanService extends IService<Policyan> {
    List<Policyan> getPolicyList(int policyid);
    /**
     * 查找政策详细信息
     *
     * @param policyan 政策对象，用于传递查询条件
     * @return Ipage
     */
    IPage<Policyan> findPolicyDetail(Policyan policyan, QueryRequest request);
}
