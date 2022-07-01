package cc.mrbird.febs.policy.mapper;

import cc.mrbird.febs.policy.entity.PolicyNewAnalysis;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface PolicyNewAnalysisMapper extends BaseMapper<PolicyNewAnalysis> {
    List<PolicyNewAnalysis> getPolicyNewList(int policyid);
}
