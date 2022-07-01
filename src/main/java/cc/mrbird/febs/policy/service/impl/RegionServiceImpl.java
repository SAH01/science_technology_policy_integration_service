package cc.mrbird.febs.policy.service.impl;

import cc.mrbird.febs.common.utils.TreeUtil;
import cc.mrbird.febs.policy.entity.Region;
import cc.mrbird.febs.policy.entity.RegionTree;
import cc.mrbird.febs.policy.mapper.RegionMapper;
import cc.mrbird.febs.policy.service.IRegionService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author WangRY
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class RegionServiceImpl extends ServiceImpl<RegionMapper, Region> implements IRegionService {

    @Override
    public List<RegionTree<Region>> findRegions() {
        List<Region> regions = this.baseMapper.selectList(new QueryWrapper<>());
        List<RegionTree<Region>> trees = this.convertregions(regions);
        return TreeUtil.buildRegionTree(trees);
    }
    /**
     * 获取所有省份（下拉选使用）
     *
     * @return 地域集合
     */
    @Override
    public List<RegionTree<Region>> getAllProvinces(){
        List<Region> regions = this.baseMapper.getAllProvinces();
        List<RegionTree<Region>> trees = this.convertregions(regions);
        return TreeUtil.buildRegionTree(trees);
    }

    @Override
    public List<RegionTree<Region>> getAllProvincesWithCountry(){
        List<Region> regions = this.baseMapper.getAllProvincesWithCountry();
        List<RegionTree<Region>> trees = this.convertregions(regions);
        return TreeUtil.buildRegionTree(trees);
    }
    private List<RegionTree<Region>> convertregions(List<Region> regions) {
        List<RegionTree<Region>> trees = new ArrayList<>();
        regions.forEach(region -> {
            RegionTree<Region> tree = new RegionTree<>();

            tree.setId(String.valueOf(region.getRegionId()));
            tree.setParentId(String.valueOf(region.getParentId()));
            tree.setTitle(region.getRegionName());
            tree.setName(region.getRegionName());
            trees.add(tree);
        });
        return trees;
    }


}
