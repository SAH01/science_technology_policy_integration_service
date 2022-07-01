package cc.mrbird.febs.policy.service;

import cc.mrbird.febs.policy.entity.PolicyContrast;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IPolicyContrastService extends IService<PolicyContrast> {
    List<PolicyContrast> getPolicyContrastList(int kindid);
    List<PolicyContrast> getPolicyContrastListByName(String name);
    List<PolicyContrast> getPolicyContrastListByTime(int kindid);
    List<PolicyContrast> getPolicyContrastListByYear(int kindid);
    List<PolicyContrast> getPolicyContrastListByCity(int kindid);
    List<PolicyContrast> getPolicyContrastListGroupKind(int kindid);
    List<PolicyContrast> getgroupList();
    List<PolicyContrast> getAllsum();
    List<PolicyContrast> getQuestionNumPolicyContrast(List<String> organlist,List<String> rangelist,List<String> themelist,List<String> yearlist,List<String> namelist);
    PolicyContrast getPolicyContrast(int id);
    /**
     * 新增部门
     *
     * @param policyContrast 部门对象
     */
    void createPolicyContrast(PolicyContrast policyContrast);

    /**
     * 修改部门
     *
     * @param policyContrast 部门对象
     */
    void updatePolicyContrast(PolicyContrast policyContrast);

    /**
     * 删除部门
     *
     * @param kindid 部门 ID集合
     */
    void deletePolicyContrasts(int kindid);
}
