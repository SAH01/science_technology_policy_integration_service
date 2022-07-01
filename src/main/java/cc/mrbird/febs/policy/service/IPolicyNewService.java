package cc.mrbird.febs.policy.service;

import cc.mrbird.febs.policy.entity.PolicyNewAnalysis;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IPolicyNewService extends IService<PolicyNewAnalysis> {
    List<PolicyNewAnalysis> getPolicyNewList(int policyid);
}
