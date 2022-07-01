package cc.mrbird.febs.policy.service;

import cc.mrbird.febs.policy.entity.Region;
import cc.mrbird.febs.policy.entity.RegionTree;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author WangRY
 */
public interface IRegionService extends IService<Region> {

    /**
     * 获取地域树（下拉选使用）
     *
     * @return 地域树集合
     */
    List<RegionTree<Region>> findRegions();


    /**
     * 获取所有省份（下拉选使用）
     *
     * @return 地域集合
     */
    List<RegionTree<Region>> getAllProvinces();

    /**
     * 获取所有省份,包括国家各部门在内（下拉选使用）
     *
     * @return 地域集合
     */
    List<RegionTree<Region>> getAllProvincesWithCountry();
}
