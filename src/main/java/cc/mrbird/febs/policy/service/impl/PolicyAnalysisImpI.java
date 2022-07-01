package cc.mrbird.febs.policy.service.impl;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.policy.entity.PolicyAnalysis;
import cc.mrbird.febs.policy.mapper.PolicyAnalysisMapper;
import cc.mrbird.febs.policy.service.IPolicyAnalysisService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PolicyAnalysisImpI extends ServiceImpl<PolicyAnalysisMapper, PolicyAnalysis> implements IPolicyAnalysisService {
    @Override
    public PolicyAnalysis getPolicyAnalysisById(int policyid) {
        return this.baseMapper.findById(policyid);
    }
    @Override
    public List<PolicyAnalysis> getPolicyAnalysisList(int policyid){return this.baseMapper.getPolicyAnalysisList(policyid);}
    @Override
    public IPage<PolicyAnalysis> findPolicyDetail(PolicyAnalysis policyanalysis, QueryRequest request) {
        Page<PolicyAnalysis> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findPolicyDetailPage(page, policyanalysis);
    }

}
