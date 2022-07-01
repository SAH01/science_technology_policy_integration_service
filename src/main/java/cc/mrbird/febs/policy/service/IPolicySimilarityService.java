package cc.mrbird.febs.policy.service;
import cc.mrbird.febs.policy.entity.PolicySimilarity;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
public interface IPolicySimilarityService extends IService<PolicySimilarity> {
    List<PolicySimilarity> getPolicySimilarityListBystartid(int startid);
    List<PolicySimilarity> getPolicySimilarityByName(String name);
    List<PolicySimilarity> getPolicySimilarityByNameGroup(String name);
}
