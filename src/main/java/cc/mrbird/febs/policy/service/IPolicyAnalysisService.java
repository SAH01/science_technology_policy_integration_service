package cc.mrbird.febs.policy.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.policy.entity.PolicyAnalysis;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface IPolicyAnalysisService extends IService<PolicyAnalysis> {
    /**
     * 通过政策Id查找用户详细信息
     *
     * @param policyid 政策Id
     * @return 政策信息
     */
    PolicyAnalysis getPolicyAnalysisById(int policyid);
    List<PolicyAnalysis> getPolicyAnalysisList(int policyid);
    /**
     * 查找政策详细信息
     *
     * @param policyanalysis 政策对象，用于传递查询条件
     * @return Ipage
     */
    IPage<PolicyAnalysis> findPolicyDetail(PolicyAnalysis policyanalysis, QueryRequest request);
}
