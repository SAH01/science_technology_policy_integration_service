package cc.mrbird.febs.policy.mapper;

import cc.mrbird.febs.policy.entity.PolicyDictionary;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
//import org.springframework.data.repository.query.Param;


public interface PolicyDictionaryMapper extends BaseMapper<PolicyDictionary> {
    IPage<PolicyDictionary> findPolicyDetailPage(Page page, @Param("policy") PolicyDictionary policy);
}
