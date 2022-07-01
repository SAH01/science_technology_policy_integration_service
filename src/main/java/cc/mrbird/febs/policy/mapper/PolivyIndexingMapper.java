package cc.mrbird.febs.policy.mapper;

import cc.mrbird.febs.policy.entity.PolicyEnter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.data.repository.query.Param;

/**
 * @BelongsProject: science
 * @BelongsPackage: cc.mrbird.febs.policy.mapper
 * @Author: Administrator
 * @CreateTime: 2022-04-28 20:43
 * @Description: 标引管理
 */
public interface PolivyIndexingMapper extends BaseMapper<PolicyEnter> {
	IPage<PolicyEnter> findPolicyDetailPage(Page page, @Param("policy_enter") PolicyEnter policy);
}
