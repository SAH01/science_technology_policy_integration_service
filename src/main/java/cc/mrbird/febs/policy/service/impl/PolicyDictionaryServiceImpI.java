package cc.mrbird.febs.policy.service.impl;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.policy.entity.PolicyDictionary;
import cc.mrbird.febs.policy.mapper.PolicyDictionaryMapper;
import cc.mrbird.febs.policy.service.PolicyDictionaryService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PolicyDictionaryServiceImpI extends ServiceImpl<PolicyDictionaryMapper, PolicyDictionary> implements PolicyDictionaryService {
    @Override
    public IPage<PolicyDictionary> findPolicyDetail(PolicyDictionary policy, QueryRequest request) {
        Page<PolicyDictionary> page = new Page<>(request.getPageNum(), request.getPageSize());
        SortUtil.handlePageSort(request, page, "id", FebsConstant.ORDER_DESC, false);
        return this.baseMapper.findPolicyDetailPage(page, policy);
    }
    @Override
    @Transactional
    public void createpolicyDictionary(PolicyDictionary role) {
        this.baseMapper.insert(role);
    }

    @Override
    @Transactional
    public void updatepolicyDictionary(PolicyDictionary role) {
        this.baseMapper.updateById(role);
    }

    @Override
    @Transactional
    public void deletepolicyDictionary(String ids) {
        int id=Integer.parseInt(ids);
        this.baseMapper.deleteById(id);
    }
}
