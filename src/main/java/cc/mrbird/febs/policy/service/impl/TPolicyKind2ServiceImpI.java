package cc.mrbird.febs.policy.service.impl;

import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.Kind2Tree;
import cc.mrbird.febs.common.entity.KindTree;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.common.utils.TreeUtil;
import cc.mrbird.febs.policy.entity.TPolicyKind;
import cc.mrbird.febs.policy.entity.TPolicyKind2;
import cc.mrbird.febs.policy.mapper.TPolicyKind2Mapper;
import cc.mrbird.febs.policy.mapper.TPolicyKindMapper;
import cc.mrbird.febs.policy.service.ITPolicyKind2Service;
import cc.mrbird.febs.policy.service.ITPolicyKindService;
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
public class TPolicyKind2ServiceImpI extends ServiceImpl<TPolicyKind2Mapper, TPolicyKind2> implements ITPolicyKind2Service {
    @Override
    @Transactional
    public void deleteTPolicyKind2s(int kindid) {
        this.baseMapper.deleteById(kindid);
    }
    @Override
    public List<TPolicyKind2> getTPolicyKindListByKindoneId(int kindid){
        return this.baseMapper.getTPolicyKindListByKindoneId(kindid);}

    @Override
    public List<TPolicyKind2> getTPolicyKindListByParentId(int parentid){
        return this.baseMapper.getTPolicyKindListByParentId(parentid);}

    @Override
    public List<TPolicyKind2> getTPolicyKindByKindoneId(){
        return this.baseMapper.getTPolicyKindByKindoneId();}
    @Override
    public TPolicyKind2 getTPolicyKindByParentId(int parentid){
        return this.baseMapper.getTPolicyKindByParentId(parentid);}

    @Override
    public List<Kind2Tree<TPolicyKind2>> findTPolicyKinds() {
        List<TPolicyKind2> kinds = this.baseMapper.selectList(new QueryWrapper<>());
        List<Kind2Tree<TPolicyKind2>> trees = this.convertKinds(kinds);
        return TreeUtil.buildKind2Tree(trees);
    }

    @Override
    public List<Kind2Tree<TPolicyKind2>> findTPolicyKinds(TPolicyKind2 kind) {
        QueryWrapper<TPolicyKind2> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(kind.getKindName()))
            queryWrapper.lambda().eq(TPolicyKind2::getKindName, kind.getKindName());
        queryWrapper.lambda().orderByAsc(TPolicyKind2::getOrderNum);
        List<TPolicyKind2> depts = this.baseMapper.selectList(queryWrapper);
        List<Kind2Tree<TPolicyKind2>> trees =  this.convertKinds(depts);
        return TreeUtil.buildKind2Tree(trees);
    }


    private List<Kind2Tree<TPolicyKind2>> convertKinds(List<TPolicyKind2> kinds){
        List<Kind2Tree<TPolicyKind2>> trees = new ArrayList<>();
        kinds.forEach(kind -> {
            Kind2Tree<TPolicyKind2> tree = new Kind2Tree<>();
            tree.setId(String.valueOf(kind.getKindId()));
            tree.setParentId(String.valueOf(kind.getParentId()));
            tree.setName(kind.getKindName());
            tree.setData(kind);
            trees.add(tree);
        });
        return trees;
    }


    @Override
    @Transactional
    public void createKind(TPolicyKind2 kind) {
        Long parentId = kind.getParentId();
        if (parentId == null) {
            kind.setParentId(0L);
        }
        else
        {
            kind.setBranchId(1);
        }

        this.save(kind);
    }

    @Override
    @Transactional
    public void updateKind(TPolicyKind2 kind) {
        kind.setModifyTime(new Date());
        if(kind.getParentId().intValue()==0)
        {
            kind.setBranchId(0);
        }
        else
        {
            kind.setBranchId(1);
        }
        this.baseMapper.updateById(kind);
    }

    @Override
    @Transactional
    public void deleteKinds(String[] kindIds) {
        this.delete(Arrays.asList(kindIds));
    }



    private void delete(List<String> kindIds) {
        removeByIds(kindIds);

        LambdaQueryWrapper<TPolicyKind2> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(TPolicyKind2::getParentId, kindIds);
        List<TPolicyKind2> depts = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(depts)) {
            List<String> deptIdList = new ArrayList<>();
            depts.forEach(d -> deptIdList.add(String.valueOf(d.getKindId())));
            this.delete(deptIdList);
        }
    }
}
