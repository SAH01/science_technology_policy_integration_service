package cc.mrbird.febs.policy.mapper;

import cc.mrbird.febs.policy.entity.PolicyNew;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface PolicyNewMapper extends BaseMapper<PolicyNew> {
    List<PolicyNew> getPolicyNewList();
}
