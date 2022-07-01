package cc.mrbird.febs.policy.mapper;

import cc.mrbird.febs.policy.entity.PolicyContrast;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface PolicyContrastMapper extends BaseMapper<PolicyContrast> {
    List<PolicyContrast> getPolicyContrastList(int kindid);
    List<PolicyContrast> getPolicyContrastListByTime(int kindid);
    List<PolicyContrast> getPolicyContrastListGroupKind(int kindid);
    List<PolicyContrast> getPolicyContrastListByName(String name);
    List<PolicyContrast> getgroupList();
    List<PolicyContrast> getAllsum();
    List<PolicyContrast> getPolicyContrastListByYear(int kindid);
    List<PolicyContrast> getPolicyContrastListByCity(int kindid);
    List<PolicyContrast> getQuestionNumPolicyContrast(List<String> organlist,List<String> rangelist,List<String> themelist,List<String> yearlist,List<String> namelist);
    PolicyContrast getPolicyContrast(int id);
}
