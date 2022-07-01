package cc.mrbird.febs.policy.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.policy.entity.PolicyDimensionalityTree;
import cc.mrbird.febs.policy.entity.PolicyIndustrial;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author WangRY
 */
public interface IPolicyIndustrialService extends IService<PolicyIndustrial> {

    /**
     * 获取部门树（下拉选使用）
     *
     * @return 部门树集合
     */
    List<PolicyDimensionalityTree<PolicyIndustrial>> findDepts();

    /**
     * 获取部门列表（树形列表）
     *
     * @param dept 部门对象（传递查询参数）
     * @return 部门树
     */
    List<PolicyDimensionalityTree<PolicyIndustrial>> findDepts(PolicyIndustrial dept);

    /**
     * 获取部门树（供Excel导出）
     *
     * @param dept    部门对象（传递查询参数）
     * @param request QueryRequest
     * @return List<PolicyIndustrial>
     */
    List<PolicyIndustrial> findDepts(PolicyIndustrial dept, QueryRequest request);

    /**
     * 新增部门
     *
     * @param dept 部门对象
     */
    void createDept(PolicyIndustrial dept);

    /**
     * 修改部门
     *
     * @param dept 部门对象
     */
    void updateDept(PolicyIndustrial dept);

    /**
     * 删除部门
     *
     * @param deptIds 部门 ID集合
     */
    void deleteDepts(String[] deptIds);
}
