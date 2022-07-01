package cc.mrbird.febs.policy.mapper;

import cc.mrbird.febs.policy.entity.PolicyName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface PolicyNameMapper extends BaseMapper<PolicyName> {
    List<PolicyName> getPolicyNameList(String name);
    List<PolicyName> getAllList();
    PolicyName getPolicyNameById(int id);

}
