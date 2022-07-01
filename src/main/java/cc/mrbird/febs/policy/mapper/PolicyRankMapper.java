package cc.mrbird.febs.policy.mapper;

import cc.mrbird.febs.policy.entity.PolicyRank;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface PolicyRankMapper  extends BaseMapper<PolicyRank> {
    List<PolicyRank> getPolicyRankListBystartid(int startid);
    List<PolicyRank> getPolicyRankByName(String name);
    List<PolicyRank> getPolicyRankByNameGroup(String name);

}
