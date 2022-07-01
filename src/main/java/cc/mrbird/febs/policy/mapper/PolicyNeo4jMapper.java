package cc.mrbird.febs.policy.mapper;

import cc.mrbird.febs.policy.entity.PolicyNeo4j;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface PolicyNeo4jMapper extends BaseMapper<PolicyNeo4j> {
    PolicyNeo4j getPolicyNeo4j(String policyname);
    List<PolicyNeo4j> getPolicyNeo4jList();
}
