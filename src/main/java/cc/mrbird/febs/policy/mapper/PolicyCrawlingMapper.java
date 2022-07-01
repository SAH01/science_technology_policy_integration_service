package cc.mrbird.febs.policy.mapper;

import cc.mrbird.febs.policy.entity.PolicyCrawling;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PolicyCrawlingMapper extends BaseMapper<PolicyCrawling> {
    /**
     * 查找政策详细信息
     *
     * @param page 分页对象
     * @param policy 政策对象，用于传递查询条件
     * @return Ipage
     */
    IPage<PolicyCrawling> findPolicyDetailPage(Page page, @Param("policy_crawling") PolicyCrawling policy);
    List<PolicyCrawling> getPolicyCrawlingList();
    PolicyCrawling findById(int policycrawlingId);
}
