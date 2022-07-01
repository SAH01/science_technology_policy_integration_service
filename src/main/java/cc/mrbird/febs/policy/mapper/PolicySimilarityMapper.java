package cc.mrbird.febs.policy.mapper;

import cc.mrbird.febs.policy.entity.PolicySimilarity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface PolicySimilarityMapper extends BaseMapper<PolicySimilarity> {
    List<PolicySimilarity> getPolicySimilarityListBystartid(int startid);
    List<PolicySimilarity> getPolicySimilarityByName(String name);
    List<PolicySimilarity> getPolicySimilarityByNameGroup(String name);
}
