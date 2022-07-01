package cc.mrbird.febs.policy.mapper;

import cc.mrbird.febs.policy.entity.Policyan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PolicyanMapper extends BaseMapper<Policyan> {
    /**
     * 查找政策详细信息
     *
     * @param page 分页对象
     * @param policyan 政策对象，用于传递查询条件
     * @return Ipage
     */
    IPage<Policyan> findPolicyDetailPage(Page page, @Param("policy") Policyan policyan);
    IPage<Policyan> getPolicyanList(int policyid);
    List<Policyan> getPolicyList(int policyid);
}
