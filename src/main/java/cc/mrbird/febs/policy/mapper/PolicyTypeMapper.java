package cc.mrbird.febs.policy.mapper;

import cc.mrbird.febs.policy.entity.PolicyType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

public interface PolicyTypeMapper extends BaseMapper<PolicyType> {
    /**
     * 查找政策详细信息
     *
     * @param page 分页对象
     * @param policytype 政策对象，用于传递查询条件
     * @return Ipage
     */
    IPage<PolicyType> findPolicyDetailPage(Page page, @Param("policytype") PolicyType policytype);
    /**
     * 通过ID查找政策
     *
     * @param id id
     * @return policy
     */
    PolicyType findById(int id);
}
