package cc.mrbird.febs.policy.service;
import cc.mrbird.febs.policy.entity.PolicyNeo4j;
import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
public interface IPolicyNeo4jService extends IService<PolicyNeo4j> {
    PolicyNeo4j getPolicyNeo4j(String policyname);
    List<PolicyNeo4j> getPolicyNeo4jList();
}
