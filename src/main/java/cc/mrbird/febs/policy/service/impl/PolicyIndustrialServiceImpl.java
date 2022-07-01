package cc.mrbird.febs.policy.service.impl;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.common.utils.TreeUtil;
import cc.mrbird.febs.policy.entity.PolicyDimensionalityTree;
import cc.mrbird.febs.policy.entity.PolicyIndustrial;
import cc.mrbird.febs.policy.mapper.PolicyIndustrialMapper;
import cc.mrbird.febs.policy.service.IPolicyIndustrialService;
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
public class PolicyIndustrialServiceImpl extends ServiceImpl<PolicyIndustrialMapper, PolicyIndustrial> implements IPolicyIndustrialService {

    @Override
    public List<PolicyDimensionalityTree<PolicyIndustrial>> findDepts() {
        List<PolicyIndustrial> depts = this.baseMapper.selectList(new QueryWrapper<>());
        List<PolicyDimensionalityTree<PolicyIndustrial>> trees = this.convertDepts(depts);
        return TreeUtil.buildPolicyDeptTree(trees);
    }

    @Override
    public List<PolicyDimensionalityTree<PolicyIndustrial>> findDepts(PolicyIndustrial dept) {
        QueryWrapper<PolicyIndustrial> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(dept.getIndustrialName()))
            queryWrapper.lambda().eq(PolicyIndustrial::getIndustrialName, dept.getIndustrialName());
        queryWrapper.lambda().orderByAsc(PolicyIndustrial::getOrderNum);

        List<PolicyIndustrial> depts = this.baseMapper.selectList(queryWrapper);
        List<PolicyDimensionalityTree<PolicyIndustrial>> trees = this.convertDepts(depts);
        return TreeUtil.buildPolicyDeptTree(trees);
    }

    @Override
    public List<PolicyIndustrial> findDepts(PolicyIndustrial dept, QueryRequest request) {
        QueryWrapper<PolicyIndustrial> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(dept.getIndustrialName()))
            queryWrapper.lambda().eq(PolicyIndustrial::getIndustrialName, dept.getIndustrialName());
        SortUtil.handleWrapperSort(request, queryWrapper, "orderNum", FebsConstant.ORDER_ASC, true);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional
    public void createDept(PolicyIndustrial dept) {
        Long parentId = dept.getParentId();
        if (parentId == null)
            dept.setParentId(0L);
        dept.setCreateTime(new Date());
        this.save(dept);
    }

    @Override
    @Transactional
    public void updateDept(PolicyIndustrial dept) {
        dept.setModifyTime(new Date());
        this.baseMapper.updateById(dept);
    }

    @Override
    @Transactional
    public void deleteDepts(String[] deptIds) {
        this.delete(Arrays.asList(deptIds));
    }

    private List<PolicyDimensionalityTree<PolicyIndustrial>> convertDepts(List<PolicyIndustrial> depts) {
        List<PolicyDimensionalityTree<PolicyIndustrial>> trees = new ArrayList<>();
        depts.forEach(dept -> {
            PolicyDimensionalityTree<PolicyIndustrial> tree = new PolicyDimensionalityTree<>();
            tree.setId(String.valueOf(dept.getIndustrialId()));
            tree.setParentId(String.valueOf(dept.getParentId()));
            tree.setName(dept.getIndustrialName());
            tree.setIndustrial_data(dept);
            trees.add(tree);
        });
        return trees;
    }

    private void delete(List<String> deptIds) {
        removeByIds(deptIds);

        LambdaQueryWrapper<PolicyIndustrial> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(PolicyIndustrial::getParentId, deptIds);
        List<PolicyIndustrial> depts = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(depts)) {
            List<String> deptIdList = new ArrayList<>();
            depts.forEach(d -> deptIdList.add(String.valueOf(d.getIndustrialId())));
            this.delete(deptIdList);
        }
    }
}
