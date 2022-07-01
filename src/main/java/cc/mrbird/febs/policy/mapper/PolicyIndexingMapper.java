package cc.mrbird.febs.policy.mapper;

import cc.mrbird.febs.policy.entity.PolicyEnter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.neo4j.cypher.internal.frontend.v2_3.ast.functions.Str;

/**
 * @BelongsProject: science
 * @BelongsPackage: cc.mrbird.febs.policy.mapper
 * @Author: Administrator
 * @CreateTime: 2022-04-28 20:50
 * @Description: 标引管理数据mapper
 */
public interface PolicyIndexingMapper extends BaseMapper<PolicyEnter> {
	IPage<PolicyEnter> findPolicyDetailPage(Page page, @Param("policy_enter") PolicyEnter policy);

	IPage<PolicyEnter> findPolicyById(Page page
			, @Param("policyName") String policyName
			, @Param("policyId") String policyId
			, @Param("policyOrgan") String policyOrgan
			, @Param("policyTime") String policyTime
			, @Param("policyType") String policyType
			, @Param("policyPublisher") String policyPublisher
	);
	PolicyEnter findById(int policyIndexingId);
}
