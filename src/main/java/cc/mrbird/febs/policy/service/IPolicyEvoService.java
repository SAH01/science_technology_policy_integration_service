package cc.mrbird.febs.policy.service;

import cc.mrbird.febs.policy.entity.PolicyEvo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IPolicyEvoService extends IService<PolicyEvo> {
    List<PolicyEvo> getPolicyEvoList(String type);
    List<PolicyEvo> getPolicyEvoByNameList(String policyname);
    List<PolicyEvo> getPolicyEvoGroupByType();
    List<PolicyEvo> getPolicyEvoListByTime(String type);
    /**
     * 新增部门
     *
     * @param policyEvo 部门对象
     */
    void createPolicyEvo(PolicyEvo policyEvo);

    /**
     * 修改部门
     *
     * @param policyEvo 部门对象
     */
    void updatePolicyEvo(PolicyEvo policyEvo);

    /**
     * 删除部门
     *
     * @param policyid 部门 ID集合
     */
    void deletePolicyEvo(int policyid);
}
