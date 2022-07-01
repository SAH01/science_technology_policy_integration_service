package cc.mrbird.febs.policy.mapper;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.policy.entity.PolicyCrawling;
import cc.mrbird.febs.policy.entity.PolicyEnter;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 *  Mapper
 *
 * @author WangRenyi
 * @date 2022-04-03 11:42:16
 */
public interface PolicyEnterMapper extends BaseMapper<PolicyEnter> {

    /**
     * 查找政策详细信息
     *
     * @param page 分页对象
     * @param policy 政策对象，用于传递查询条件
     * @return Ipage
     */
    IPage<PolicyEnter> findPolicyDetailPage(Page page, @Param("policy_enter") PolicyEnter policy);
    List<PolicyEnter> getPolicyCrawlingList();
    PolicyEnter findById(int policyId);
}
