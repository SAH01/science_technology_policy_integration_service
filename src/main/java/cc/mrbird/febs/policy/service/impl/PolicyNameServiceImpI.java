package cc.mrbird.febs.policy.service.impl;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.NameTree;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.common.utils.TreeUtil;
import cc.mrbird.febs.policy.entity.PolicyName;
import cc.mrbird.febs.policy.mapper.PolicyNameMapper;
import cc.mrbird.febs.policy.service.IPolicyNameService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PolicyNameServiceImpI extends ServiceImpl<PolicyNameMapper, PolicyName> implements IPolicyNameService {
    @Override
    public PolicyName getPolicyNameById(int id){
        return this.baseMapper.getPolicyNameById(id);
    }
    @Override
    public List<PolicyName> getAllList(){
        return this.baseMapper.getAllList();}
    @Override
    public List<PolicyName> getPolicyNameList(String name){
        return this.baseMapper.getPolicyNameList(name);}
    @Override
    public List<NameTree<PolicyName>> findPolicyNames() {
        List<PolicyName> policyNames = this.baseMapper.selectList(new QueryWrapper<>());
        List<NameTree<PolicyName>> trees = this.convertPolicyNames(policyNames);
        return TreeUtil.buildNameTree(trees);
    }

    @Override
    public List<NameTree<PolicyName>> findPolicyNames(PolicyName policyName) {
        QueryWrapper<PolicyName> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(policyName.getTypeName()))
            queryWrapper.lambda().eq(PolicyName::getTypeName, policyName.getTypeName());
        queryWrapper.lambda().orderByAsc(PolicyName::getOrderNum);

        List<PolicyName> policynames = this.baseMapper.selectList(queryWrapper);
        List<NameTree<PolicyName>> trees =  this.convertPolicyNames(policynames);
        return TreeUtil.buildNameTree(trees);
    }

    @Override
    public List<PolicyName> findPolicyNames(PolicyName policyName, QueryRequest request) {
        QueryWrapper<PolicyName> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(policyName.getTypeName()))
            queryWrapper.lambda().eq(PolicyName::getTypeName, policyName.getTypeName());
        SortUtil.handleWrapperSort(request, queryWrapper, "orderNum", FebsConstant.ORDER_ASC, true);
        return this.baseMapper.selectList(queryWrapper);
    }
    private List<NameTree<PolicyName>> convertPolicyNames(List<PolicyName> policyNames){
        List<NameTree<PolicyName>> trees = new ArrayList<>();
        policyNames.forEach(policyName -> {
            NameTree<PolicyName> tree = new NameTree<>();
            tree.setId(String.valueOf(policyName.getTypeId()));
            tree.setParentId(String.valueOf(policyName.getParentId()));
            tree.setName(policyName.getTypeName());
            tree.setData(policyName);
            trees.add(tree);
        });
        return trees;
    }

    @Override
    @Transactional
    public void createType(PolicyName type) {
        Long parentId = type.getParentId();
        if (parentId == null) {
            type.setParentId(0L);
        }
        this.save(type);
    }

    @Override
    @Transactional
    public void updateType(PolicyName type) {
        type.setModifyTime(new Date());
        this.baseMapper.updateById(type);
    }

    @Override
    @Transactional
    public void deleteTypes(String[] typeIds) {
        this.delete(Arrays.asList(typeIds));
    }


    private void delete(List<String> typeIds) {
        removeByIds(typeIds);

        LambdaQueryWrapper<PolicyName> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(PolicyName::getParentId, typeIds);
        List<PolicyName> types = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(types)) {
            List<String> typeIdList = new ArrayList<>();
            types.forEach(d -> typeIdList.add(String.valueOf(d.getTypeId())));
            this.delete(typeIdList);
        }
    }
}
