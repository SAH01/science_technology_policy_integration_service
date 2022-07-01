package cc.mrbird.febs.policy.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.policy.entity.PolicyDictionary;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

public interface PolicyDictionaryService extends IService<PolicyDictionary> {
    IPage<PolicyDictionary> findPolicyDetail(PolicyDictionary policy, QueryRequest request);
    void createpolicyDictionary(PolicyDictionary policy);

    void updatepolicyDictionary(PolicyDictionary policy);

    void deletepolicyDictionary(String id);
}
