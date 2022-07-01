package cc.mrbird.febs.policy.mapper;

import cc.mrbird.febs.policy.entity.PolicyEvo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface PolicyEvoMapper extends BaseMapper<PolicyEvo> {
    List<PolicyEvo> getPolicyEvoList(String type);
    List<PolicyEvo> getPolicyEvoByNameList(String policyname);
    List<PolicyEvo> getPolicyEvoGroupByType();
    List<PolicyEvo> getPolicyEvoListByTime(String type);
}
