package cc.mrbird.febs.policy.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.policy.entity.PolicyDept;
import cc.mrbird.febs.policy.entity.PolicyDimensionalityTree;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface IPolicyDeptService extends IService<PolicyDept> {

    /**
     * 获取部门树（下拉选使用）
     *
     * @return 部门树集合
     */
    List<PolicyDimensionalityTree<PolicyDept>> findDepts();

    /**
     * 获取部门列表（树形列表）
     *
     * @param dept 部门对象（传递查询参数）
     * @return 部门树
     */
    List<PolicyDimensionalityTree<PolicyDept>> findDepts(PolicyDept dept);

    /**
     * 获取部门树（供Excel导出）
     *
     * @param dept    部门对象（传递查询参数）
     * @param request QueryRequest
     * @return List<PolicyDept>
     */
    List<PolicyDept> findDepts(PolicyDept dept, QueryRequest request);

    /**
     * 新增部门
     *
     * @param dept 部门对象
     */
    void createDept(PolicyDept dept);

    /**
     * 修改部门
     *
     * @param dept 部门对象
     */
    void updateDept(PolicyDept dept);

    /**
     * 删除部门
     *
     * @param deptIds 部门 ID集合
     */
    void deleteDepts(String[] deptIds);
}
