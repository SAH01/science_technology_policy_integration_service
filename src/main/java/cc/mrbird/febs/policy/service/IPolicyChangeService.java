package cc.mrbird.febs.policy.service;

import cc.mrbird.febs.policy.entity.PolicyChange;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IPolicyChangeService extends IService<PolicyChange> {
    List<PolicyChange> getPolicyChangeList(String type);
    PolicyChange getPolicyChange(int id);
}
