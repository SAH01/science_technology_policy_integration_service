package cc.mrbird.febs.policy.controller;

import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.policy.entity.Region;
import cc.mrbird.febs.policy.entity.RegionTree;
import cc.mrbird.febs.policy.service.IRegionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author WangRY
 */
@Slf4j
@RestController
@RequestMapping("region")
public class RegionController {
    @Autowired
    IRegionService regionService;

    @GetMapping("tree")
    public FebsResponse getIndustrialTree() throws FebsException {
        try {
            return new FebsResponse().success().data(this.regionService.findRegions());
        } catch (Exception e) {
            String message = "获取地域树失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
    @GetMapping("province")
    public List<RegionTree<Region>> getAllProvinces() throws FebsException {
        try {
            return this.regionService.getAllProvinces();
        } catch (Exception e) {
            String message = "获取省份失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }

    @GetMapping("provinceWithCountry")
    public List<RegionTree<Region>> getAllProvincesWithCountry() throws FebsException {
        try {
            return this.regionService.getAllProvincesWithCountry();
        } catch (Exception e) {
            String message = "获取省份失败";
            log.error(message, e);
            throw new FebsException(message);
        }
    }
}
