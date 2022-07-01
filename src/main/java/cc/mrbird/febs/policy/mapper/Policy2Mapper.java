package cc.mrbird.febs.policy.mapper;

import cc.mrbird.febs.policy.entity.IntermediateVariable;
import cc.mrbird.febs.policy.entity.Policy;
import cc.mrbird.febs.policy.entity.PolicyStatisticalResult;
import cc.mrbird.febs.policy.entity.model.OrganPolicyNum;
import cc.mrbird.febs.policy.entity.model.PolicyYearRegionName;
import cc.mrbird.febs.policy.entity.model.YearCategoryNum;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface Policy2Mapper extends BaseMapper<Policy> {
    List<Policy> getPolicyListSearchAll(String search);
    List<Policy> getPolicyListSearch(String search);
    List<Policy> getPolicyListByName(String name);
    List<Policy> getPolicyListByNameAll(String search);
    List<Policy> getPolicyListGroupForm();
    List<Policy> getPolicyListGroupFormRank();
    List<Policy> getPolicyListGroupRank(String form);
    List<Policy> getPolicyList();
    List<Policy> getPolicyMultiple(@Param("policy")Policy policy);
    List<Policy> getPolicyMultipleAll(@Param("policy")Policy policy);
    /**
     * 通过ID查找政策
     *
     * @param policyId id
     * @return policy
     */
    Policy findById(Integer policyId);


    /**
     * 查找政策详细信息
     *
     * @param page 分页对象
     * @param policy 政策对象，用于传递查询条件
     * @return Ipage
     */
    IPage<Policy> findPolicyDetailPage(Page page, @Param("policy") Policy policy);
    IPage<Policy> findPolicyDetailPage2(Page page, List<String> organlist,List<String> rangelist,List<String> themelist,List<String> yearlist,List<String> namelist);
    IPage<Policy> findPolicyDetailPage3(Page page, List<String> organlist,List<String> rangelist,List<String> themelist,List<String> yearlist,List<String> namelist);
    IPage<Policy> findPolicyDetailPage4(Page page, List<String> organlist,List<String> rangelist,List<String> themelist,List<String> yearlist,List<String> namelist);
    IPage<Policy> findPolicyDetailPage5(Page page, List<String> organlist,List<String> rangelist,List<String> themelist,List<String> namelist,String startyear,String endyear);

    /**
     * 查找所有政策的关键词
     * @return
     */
    List<String> getPolicyKeyWordsList();

    List<Policy> getQuestionNumByPolicy(List<String> organlist,List<String> rangelist,List<String> themelist,List<String> yearlist,List<String> namelist);
    List<Policy> getQuestionNumByPolicy2(List<String> organlist,List<String> rangelist,List<String> themelist,List<String> yearlist,List<String> namelist,List<String> fanwei);
    List<Policy> getQuestionNumByPolicy3(List<String> organlist,List<String> rangelist,List<String> themelist,List<String> yearlist,List<String> namelist,List<String> fanwei);
    List<Policy> getQuestionNumByPolicy4(List<String> organlist,List<String> rangelist,List<String> themelist,List<String> namelist,String startyear,String endyear);


    List<String> getKeyWordByPolicy(List<Integer> regionIds, @Param("intermediate") IntermediateVariable intermediate);

    /**
     * 统计各地域不同年份出台的某一类科技政策的数量
     */
    List<PolicyStatisticalResult> getOneThemeManyRegionPolicyNumAndYear(List<Integer> regionIds, @Param("intermediate") IntermediateVariable intermediate, List<String> formulaKeywords);
    /**
     * 统计各年不同类型政策数量，用堆叠条形图展示
     */
    List<YearCategoryNum> getEveryYearEveryCategoryNum(@Param("intermediate") IntermediateVariable intermediate);
    /**
     * 统计各部门出台的政策数量
     */
    List<OrganPolicyNum> getEveryOrganPolicyNum(@Param("intermediate") IntermediateVariable intermediate);

    /**
     * 用于辅助政策公式 1.挖掘政策文件  2.按时间轴显示
     * @param regionIds 地域ids
     * @param intermediate 中间量
     * @param formulaKeywords 公式关键词（已经组织好的）
     * @return 集合
     */
    List<PolicyYearRegionName> getPolicyYearRegionNameList(List<Integer> regionIds, @Param("intermediate") IntermediateVariable intermediate, List<String> formulaKeywords);

    /**
     * 获取上位政策
     * @param policyName 文件名
     * @param formulaKeywords 关键词
     * @return 上位政策年份等信息
     */
    List<PolicyYearRegionName> getSuperiorPolicyByPolicyName(String policyName, List<String> formulaKeywords);

    /**
     * 根据文件名获取文件
     * @param name
     * @return
     */
    PolicyYearRegionName getPolicyByName(String name);

    /**
     * 统计某一年一个地区不同类型政策数量
     * variable：传递参数的中间对象
     */
    List<YearCategoryNum> getOneYearOneRegionCategoryNum(@Param("intermediate") IntermediateVariable variable);

    /**
     * 统计某一年国家不同类型政策数量
     * variable：传递参数的中间对象
     */
    List<YearCategoryNum> getOneYearCountryCategoryNum(@Param("intermediate") IntermediateVariable variable);

    /**
     * 统计某一年某一地区某一类型的政策
     * @param variable 中间变量
     * @return list
     */
    List<Policy> getOneYearOneRegionOneCategoryPolicy(@Param("intermediate") IntermediateVariable variable);

    /**
     * 统计某一年国家和地方不同类型政策数量
     * variable：传递参数的中间对象
     */
    List<YearCategoryNum> getOneYearCategoryNum(@Param("intermediate") IntermediateVariable variable);
}
