package cc.mrbird.febs.policy.mapper;

import cc.mrbird.febs.policy.entity.PolicyEvolution;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface PolicyEvolutionMapper extends BaseMapper<PolicyEvolution> {
    List<PolicyEvolution> getPolicyEvolutionList(String type);
    List<PolicyEvolution> getPolicyEvolutionByNameList(String policyname);
}
