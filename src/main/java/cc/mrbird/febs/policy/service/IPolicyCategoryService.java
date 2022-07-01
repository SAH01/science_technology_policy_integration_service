package cc.mrbird.febs.policy.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.policy.entity.PolicyCategory;
import cc.mrbird.febs.policy.entity.PolicyDimensionalityTree;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface IPolicyCategoryService extends IService<PolicyCategory> {

    /**
     * 获取政策标准分类树（下拉选使用）
     *
     * @return 政策标准分类树集合
     */
    List<PolicyDimensionalityTree<PolicyCategory>> findDepts();

    /**
     * 获取政策标准分类列表（树形列表）
     *
     * @param dept 政策标准分类对象（传递查询参数）
     * @return 政策标准分类树
     */
    List<PolicyDimensionalityTree<PolicyCategory>> findDepts(PolicyCategory dept);

    /**
     * 获取政策标准分类树（供Excel导出）
     *
     * @param dept    政策标准分类对象（传递查询参数）
     * @param request QueryRequest
     * @return List<PolicyCategory>
     */
    List<PolicyCategory> findDepts(PolicyCategory dept, QueryRequest request);

    /**
     * 新增政策标准分类
     *
     * @param dept 政策标准分类对象
     */
    void createDept(PolicyCategory dept);

    /**
     * 修改政策标准分类
     *
     * @param dept 政策标准分类对象
     */
    void updateDept(PolicyCategory dept);

    /**
     * 删除政策标准分类
     *
     * @param deptIds 政策标准分类 ID集合
     */
    void deleteDepts(String[] deptIds);
}
