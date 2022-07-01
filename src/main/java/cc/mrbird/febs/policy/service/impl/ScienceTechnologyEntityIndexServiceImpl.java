package cc.mrbird.febs.policy.service.impl;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.common.utils.TreeUtil;
import cc.mrbird.febs.policy.entity.ScienceTechnologyEntityIndex;
import cc.mrbird.febs.policy.entity.FormulaAndEntityTree;
import cc.mrbird.febs.policy.mapper.ScienceTechnologyEntityIndexMapper;
import cc.mrbird.febs.policy.service.IScienceTechnologyEntityIndexService;
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
public class ScienceTechnologyEntityIndexServiceImpl extends ServiceImpl<ScienceTechnologyEntityIndexMapper, ScienceTechnologyEntityIndex> implements IScienceTechnologyEntityIndexService {
    @Override
    public List<FormulaAndEntityTree<ScienceTechnologyEntityIndex>> findEntityIndex() {
        List<ScienceTechnologyEntityIndex> entityIndexs = this.baseMapper.selectList(new QueryWrapper<>());
        List<FormulaAndEntityTree<ScienceTechnologyEntityIndex>> trees = this.convertDepts(entityIndexs);
        return TreeUtil.buildFormulaAndEntityTree(trees);
    }

    @Override
    public List<FormulaAndEntityTree<ScienceTechnologyEntityIndex>> findEntityIndex(ScienceTechnologyEntityIndex entityIndex) {
        QueryWrapper<ScienceTechnologyEntityIndex> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(entityIndex.getEntityIndexName()))
            queryWrapper.lambda().eq(ScienceTechnologyEntityIndex::getEntityIndexName, entityIndex.getEntityIndexName());
        queryWrapper.lambda().orderByAsc(ScienceTechnologyEntityIndex::getOrderNum);

        List<ScienceTechnologyEntityIndex> entityIndexs = this.baseMapper.selectList(queryWrapper);
        List<FormulaAndEntityTree<ScienceTechnologyEntityIndex>> trees = this.convertDepts(entityIndexs);
        return TreeUtil.buildFormulaAndEntityTree(trees);
    }

    @Override
    public List<ScienceTechnologyEntityIndex> findEntityIndexs(ScienceTechnologyEntityIndex entityIndex, QueryRequest request) {
        QueryWrapper<ScienceTechnologyEntityIndex> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(entityIndex.getEntityIndexName()))
            queryWrapper.lambda().eq(ScienceTechnologyEntityIndex::getEntityIndexName, entityIndex.getEntityIndexName());
        SortUtil.handleWrapperSort(request, queryWrapper, "orderNum", FebsConstant.ORDER_ASC, true);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    public void createEntityIndex(ScienceTechnologyEntityIndex entityIndex) {
        Long parentId = entityIndex.getParentId();
        if (parentId == null)
            entityIndex.setParentId(0L);
        this.save(entityIndex);
    }

    @Override
    public void updateEntityIndex(ScienceTechnologyEntityIndex entityIndex) {
        this.baseMapper.updateById(entityIndex);
    }

    @Override
    public void deleteEntityIndexs(String[] entityIndexIds) {
        this.delete(Arrays.asList(entityIndexIds));
    }

    @Override
    public List<String> getEntityIndexContentsByEntityIndexIds(List<Integer> entityIndexIds) {
        return this.baseMapper.getEntityIndexContentsByEntityIndexIds(entityIndexIds);
    }

    @Override
    public String getEntityIndexContentByEntityIndexId(Integer entityIndexId) {
        return this.baseMapper.getEntityIndexContentByEntityIndexId(entityIndexId);
    }

    private List<FormulaAndEntityTree<ScienceTechnologyEntityIndex>> convertDepts(List<ScienceTechnologyEntityIndex> entityIndexs) {
        List<FormulaAndEntityTree<ScienceTechnologyEntityIndex>> trees = new ArrayList<>();
        entityIndexs.forEach(entityIndex -> {
            FormulaAndEntityTree<ScienceTechnologyEntityIndex> tree = new FormulaAndEntityTree<>();
            tree.setId(String.valueOf(entityIndex.getEntityIndexId()));
            tree.setParentId(String.valueOf(entityIndex.getParentId()));
            tree.setName(entityIndex.getEntityIndexName());
            tree.setEntityIndexData(entityIndex);
            trees.add(tree);
        });
        return trees;
    }

    private void delete(List<String> entityIndexIds) {
        removeByIds(entityIndexIds);

        LambdaQueryWrapper<ScienceTechnologyEntityIndex> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ScienceTechnologyEntityIndex::getParentId, entityIndexIds);
        List<ScienceTechnologyEntityIndex> entityIndexs = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(entityIndexs)) {
            List<String> entityIndexIdList = new ArrayList<>();
            entityIndexs.forEach(d -> entityIndexIdList.add(String.valueOf(d.getEntityIndexId())));
            this.delete(entityIndexIdList);
        }
    }
}
