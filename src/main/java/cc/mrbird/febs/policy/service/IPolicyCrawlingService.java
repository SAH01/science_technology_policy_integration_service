package cc.mrbird.febs.policy.service;


import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.policy.entity.PolicyCrawling;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IPolicyCrawlingService extends IService<PolicyCrawling> {
    IPage<PolicyCrawling> findPolicyCrawlingDetail(PolicyCrawling policy, QueryRequest request);
    void createPolicyCrawling(PolicyCrawling policy);
    void deletePolicyCrawling(int id);
    List<PolicyCrawling> getPolicyCrawlingList();
    PolicyCrawling findById(int policycrawlingId);
}
