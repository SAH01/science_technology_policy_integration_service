package cc.mrbird.febs.policy.service.impl;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.common.utils.TreeUtil;
import cc.mrbird.febs.policy.entity.PolicyDept;
import cc.mrbird.febs.policy.entity.PolicyDimensionalityTree;
import cc.mrbird.febs.policy.mapper.PolicyDeptMapper;
import cc.mrbird.febs.policy.service.IPolicyDeptService;
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
public class PolicyDeptServiceImpl extends ServiceImpl<PolicyDeptMapper, PolicyDept> implements IPolicyDeptService {

    @Override
    public List<PolicyDimensionalityTree<PolicyDept>> findDepts() {
        List<PolicyDept> depts = this.baseMapper.selectList(new QueryWrapper<>());
        List<PolicyDimensionalityTree<PolicyDept>> trees = this.convertDepts(depts);
        return TreeUtil.buildPolicyDeptTree(trees);
    }

    @Override
    public List<PolicyDimensionalityTree<PolicyDept>> findDepts(PolicyDept dept) {
        QueryWrapper<PolicyDept> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(dept.getDeptName()))
            queryWrapper.lambda().eq(PolicyDept::getDeptName, dept.getDeptName());
        queryWrapper.lambda().orderByAsc(PolicyDept::getOrderNum);

        List<PolicyDept> depts = this.baseMapper.selectList(queryWrapper);
        List<PolicyDimensionalityTree<PolicyDept>> trees = this.convertDepts(depts);
        return TreeUtil.buildPolicyDeptTree(trees);
    }

    @Override
    public List<PolicyDept> findDepts(PolicyDept dept, QueryRequest request) {
        QueryWrapper<PolicyDept> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(dept.getDeptName()))
            queryWrapper.lambda().eq(PolicyDept::getDeptName, dept.getDeptName());
        SortUtil.handleWrapperSort(request, queryWrapper, "orderNum", FebsConstant.ORDER_ASC, true);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void createDept(PolicyDept dept) {
        Long parentId = dept.getParentId();
        if (parentId == null)
            dept.setParentId(0L);
        dept.setCreateTime(new Date());
        this.save(dept);
    }

    @Override
    @Transactional
    public void updateDept(PolicyDept dept) {
        dept.setModifyTime(new Date());
        this.baseMapper.updateById(dept);
    }

    @Override
    @Transactional
    public void deleteDepts(String[] deptIds) {
        this.delete(Arrays.asList(deptIds));
    }

    private List<PolicyDimensionalityTree<PolicyDept>> convertDepts(List<PolicyDept> depts) {
        List<PolicyDimensionalityTree<PolicyDept>> trees = new ArrayList<>();
        depts.forEach(dept -> {
            PolicyDimensionalityTree<PolicyDept> tree = new PolicyDimensionalityTree<>();
            tree.setId(String.valueOf(dept.getDeptId()));
            tree.setParentId(String.valueOf(dept.getParentId()));
            tree.setName(dept.getDeptName());
            tree.setGovernment_data(dept);
            trees.add(tree);
        });
        return trees;
    }

    private void delete(List<String> deptIds) {
        removeByIds(deptIds);

        LambdaQueryWrapper<PolicyDept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(PolicyDept::getParentId, deptIds);
        List<PolicyDept> depts = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(depts)) {
            List<String> deptIdList = new ArrayList<>();
            depts.forEach(d -> deptIdList.add(String.valueOf(d.getDeptId())));
            this.delete(deptIdList);
        }
    }
}
