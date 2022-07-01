package cc.mrbird.febs.policy.mapper;

import cc.mrbird.febs.policy.entity.Region;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author WangRY
 */
public interface RegionMapper extends BaseMapper<Region> {
    /**
     * 获取所有省份（下拉选使用）
     *
     * @return 地域集合
     */
    List<Region> getAllProvinces();

    /**
     * 获取所有省份,包括国家各部门在内（下拉选使用）
     *
     * @return 地域集合
     */
    List<Region> getAllProvincesWithCountry();
}
