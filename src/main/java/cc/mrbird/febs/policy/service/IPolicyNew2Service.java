package cc.mrbird.febs.policy.service;

import cc.mrbird.febs.policy.entity.PolicyNew;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IPolicyNew2Service extends IService<PolicyNew> {
    List<PolicyNew> getPolicyNewList();
}
