package cc.mrbird.febs.policy.service;


import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.policy.entity.PolicyEnter;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *  Service接口
 *
 * @author WangRenyi
 * @date 2022-04-03 11:42:16
 */
public interface IPolicyEnterService extends IService<PolicyEnter> {
    IPage<PolicyEnter> findPolicyCrawlingDetail(PolicyEnter policy, QueryRequest request);
    void createPolicyCrawling(PolicyEnter policy);
    void deletePolicyCrawling(int id);
    void updatePolicyCrawling(PolicyEnter id);
    List<PolicyEnter> getPolicyCrawlingList();
    PolicyEnter findById(int policycrawlingId);
}
