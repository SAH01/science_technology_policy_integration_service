package cc.mrbird.febs.policy.mapper;

import cc.mrbird.febs.policy.entity.PolicyChange;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface PolicyChangeMapper extends BaseMapper<PolicyChange> {
    List<PolicyChange> getPolicyChangeList(String type);
    PolicyChange getPolicyChange(int id);
}
