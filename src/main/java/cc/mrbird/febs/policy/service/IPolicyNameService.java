package cc.mrbird.febs.policy.service;

import cc.mrbird.febs.common.entity.NameTree;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.policy.entity.PolicyName;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IPolicyNameService extends IService<PolicyName> {
    List<PolicyName> getPolicyNameList(String name);
    List<PolicyName> getAllList();
    PolicyName getPolicyNameById(int id);
    /**
     * 获取类型树（下拉选使用）
     *
     * @return 分类树集合
     */
    List<NameTree<PolicyName>> findPolicyNames();

    /**
     * 获取部门列表（树形列表）
     *
     * @param policyName 类型对象（传递查询参数）
     * @return 分类树
     */
    List<NameTree<PolicyName>> findPolicyNames(PolicyName policyName);

    /**
     * 获取类型树（供Excel导出）
     *
     * @param policyName    类型对象（传递查询参数）
     * @param request QueryRequest
     * @return List<Dept>
     */
    List<PolicyName> findPolicyNames(PolicyName policyName, QueryRequest request);
    /**
     * 新增部门
     *
     * @param type 部门对象
     */
    void createType(PolicyName type);

    /**
     * 修改部门
     *
     * @param type 部门对象
     */
    void updateType(PolicyName type);

    /**
     * 删除部门
     *
     * @param typeIds 部门 ID集合
     */
    void deleteTypes(String[] typeIds);
}
