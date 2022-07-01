package cc.mrbird.febs.policy.service;

import cc.mrbird.febs.policy.entity.PolicyEvolution;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IPolicyEvolutionService extends IService<PolicyEvolution> {
    List<PolicyEvolution> getPolicyEvolutionList(String type);
    List<PolicyEvolution> getPolicyEvolutionByNameList(String policyname);
}
