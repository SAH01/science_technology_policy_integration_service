package cc.mrbird.febs.policy.mapper;

import cc.mrbird.febs.policy.entity.Policy;
import cc.mrbird.febs.policy.entity.PolicyAnalysis;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PolicyAnalysisMapper extends BaseMapper<PolicyAnalysis> {
    /**
     * 通过ID查找政策
     *
     * @param policyid id
     * @return policy
     */
    PolicyAnalysis findById(int policyid);
    List<PolicyAnalysis> getPolicyAnalysisList(int policyid);
    /**
     * 查找政策详细信息
     *
     * @param page 分页对象
     * @param policyanalysis 政策对象，用于传递查询条件
     * @return Ipage
     */
    IPage<PolicyAnalysis> findPolicyDetailPage(Page page, @Param("policy") PolicyAnalysis policyanalysis);
}
