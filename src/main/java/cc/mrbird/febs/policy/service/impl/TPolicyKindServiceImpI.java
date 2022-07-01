package cc.mrbird.febs.policy.service.impl;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.entity.KindTree;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.utils.SortUtil;
import cc.mrbird.febs.common.utils.TreeUtil;
import cc.mrbird.febs.policy.entity.TPolicyKind;
import cc.mrbird.febs.policy.mapper.TPolicyKindMapper;
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
public class TPolicyKindServiceImpI extends ServiceImpl<TPolicyKindMapper, TPolicyKind> implements ITPolicyKindService {
    @Override
    public List<TPolicyKind> getList(){
        return this.baseMapper.getList();}
    @Override
    public List<TPolicyKind> getTPolicyKindList(int id){
        return this.baseMapper.getTPolicyKindList(id);}
    @Override
    public List<TPolicyKind> getPTPolicyKindList(int id){
        return this.baseMapper.getPTPolicyKindList(id);}
    @Override
    public List<KindTree<TPolicyKind>> findTPolicyKinds() {
        List<TPolicyKind> kinds = this.baseMapper.selectList(new QueryWrapper<>());
        for(int i=0;i<kinds.size();i++)
        {
            System.out.println(kinds.get(i).getKindName());
        }
        List<KindTree<TPolicyKind>> trees = this.convertKinds(kinds);
        return TreeUtil.buildKindTree(trees);
    }
    @Override
    public List<KindTree<TPolicyKind>> findTPolicyKinds(TPolicyKind kind) {
        QueryWrapper<TPolicyKind> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(kind.getKindName()))
            queryWrapper.lambda().eq(TPolicyKind::getKindName, kind.getKindName());
        queryWrapper.lambda().orderByAsc(TPolicyKind::getOrderNum);
        List<TPolicyKind> depts = this.baseMapper.selectList(queryWrapper);
        for(int i=0;i<depts.size();i++)
        {
            System.out.println(depts.get(i).getKindName());
        }
        List<KindTree<TPolicyKind>> trees =  this.convertKinds(depts);
        return TreeUtil.buildKindTree(trees);
    }
    @Override
    public List<TPolicyKind> findTPolicyKinds(TPolicyKind kind, QueryRequest request) {
        QueryWrapper<TPolicyKind> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(kind.getKindName()))
            queryWrapper.lambda().eq(TPolicyKind::getKindName, kind.getKindName());
        SortUtil.handleWrapperSort(request, queryWrapper, "orderNum", FebsConstant.ORDER_ASC, true);
        return this.baseMapper.selectList(queryWrapper);
    }
    private List<KindTree<TPolicyKind>> convertKinds(List<TPolicyKind> kinds){
        List<KindTree<TPolicyKind>> trees = new ArrayList<>();
        kinds.forEach(kind -> {
            KindTree<TPolicyKind> tree = new KindTree<>();
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
    public void createKind(TPolicyKind kind) {
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
    public void updateKind(TPolicyKind kind) {
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
    private List<KindTree<TPolicyKind>> convertkinds(List<TPolicyKind> kinds){
        List<KindTree<TPolicyKind>> trees = new ArrayList<>();
        kinds.forEach(kind -> {
            KindTree<TPolicyKind> tree = new KindTree<>();
            tree.setId(String.valueOf(kind.getKindId()));
            tree.setParentId(String.valueOf(kind.getParentId()));
            tree.setName(kind.getKindName());
            tree.setData(kind);
            trees.add(tree);
        });
        return trees;
    }
    private void delete(List<String> kindIds) {
        removeByIds(kindIds);
        LambdaQueryWrapper<TPolicyKind> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(TPolicyKind::getParentId, kindIds);
        List<TPolicyKind> depts = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(depts)) {
            List<String> deptIdList = new ArrayList<>();
            depts.forEach(d -> deptIdList.add(String.valueOf(d.getKindId())));
            this.delete(deptIdList);
        }
    }
}
