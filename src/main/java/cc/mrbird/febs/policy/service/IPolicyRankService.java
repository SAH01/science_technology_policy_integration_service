package cc.mrbird.febs.policy.service;

import cc.mrbird.febs.policy.entity.PolicyRank;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IPolicyRankService extends IService<PolicyRank> {
    List<PolicyRank> getPolicyRankListBystartid(int startid);
    List<PolicyRank> getPolicyRankByName(String name);
    List<PolicyRank> getPolicyRankByNameGroup(String name);
}
