package cc.mrbird.febs.policy.service.impl;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.common.utils.TreeUtil;
import cc.mrbird.febs.policy.entity.PolicyCategory;
import cc.mrbird.febs.policy.entity.PolicyDimensionalityTree;
import cc.mrbird.febs.policy.mapper.PolicyCategoryMapper;
import cc.mrbird.febs.policy.service.IPolicyCategoryService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author WangRY
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class PolicyCategoryServiceImpl extends ServiceImpl<PolicyCategoryMapper, PolicyCategory> implements IPolicyCategoryService {

    @Override
    public List<PolicyDimensionalityTree<PolicyCategory>> findDepts() {
        List<PolicyCategory> depts = this.baseMapper.selectList(new QueryWrapper<>());
        List<PolicyDimensionalityTree<PolicyCategory>> trees = this.convertDepts(depts);
        return TreeUtil.buildPolicyDeptTree(trees);
    }

    @Override
    public List<PolicyDimensionalityTree<PolicyCategory>> findDepts(PolicyCategory dept) {
        QueryWrapper<PolicyCategory> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(dept.getCategoryName()))
            queryWrapper.lambda().eq(PolicyCategory::getCategoryName, dept.getCategoryName());
        queryWrapper.lambda().orderByAsc(PolicyCategory::getOrderNum);

        List<PolicyCategory> depts = this.baseMapper.selectList(queryWrapper);
        List<PolicyDimensionalityTree<PolicyCategory>> trees = this.convertDepts(depts);
        return TreeUtil.buildPolicyDeptTree(trees);
    }

    @Override
    public List<PolicyCategory> findDepts(PolicyCategory dept, QueryRequest request) {
        QueryWrapper<PolicyCategory> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(dept.getCategoryName()))
            queryWrapper.lambda().eq(PolicyCategory::getCategoryName, dept.getCategoryName());
        SortUtil.handleWrapperSort(request, queryWrapper, "orderNum", FebsConstant.ORDER_ASC, true);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void createDept(PolicyCategory dept) {
        Long parentId = dept.getParentId();
        if (parentId == null)
            dept.setParentId(0L);
        dept.setCreateTime(new Date());
        this.save(dept);
    }

    @Override
    @Transactional
    public void updateDept(PolicyCategory dept) {
        dept.setModifyTime(new Date());
        this.baseMapper.updateById(dept);
    }

    @Override
    @Transactional
    public void deleteDepts(String[] deptIds) {
        this.delete(Arrays.asList(deptIds));
    }

    private List<PolicyDimensionalityTree<PolicyCategory>> convertDepts(List<PolicyCategory> depts) {
        List<PolicyDimensionalityTree<PolicyCategory>> trees = new ArrayList<>();
        depts.forEach(dept -> {
            PolicyDimensionalityTree<PolicyCategory> tree = new PolicyDimensionalityTree<>();
            tree.setId(String.valueOf(dept.getCategoryId()));
            tree.setParentId(String.valueOf(dept.getParentId()));
            tree.setName(dept.getCategoryName());
            tree.setCategory_data(dept);
            trees.add(tree);
        });
        return trees;
    }

    private void delete(List<String> deptIds) {
        removeByIds(deptIds);

        LambdaQueryWrapper<PolicyCategory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(PolicyCategory::getParentId, deptIds);
        List<PolicyCategory> depts = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(depts)) {
            List<String> deptIdList = new ArrayList<>();
            depts.forEach(d -> deptIdList.add(String.valueOf(d.getCategoryId())));
            this.delete(deptIdList);
        }
    }
}
