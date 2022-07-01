package cc.mrbird.febs.policy.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.policy.entity.IntermediateVariable;
import cc.mrbird.febs.policy.entity.Policy;
import cc.mrbird.febs.policy.entity.PolicyStatisticalResult;
import cc.mrbird.febs.policy.entity.model.OrganPolicyNum;
import cc.mrbird.febs.policy.entity.model.PolicyYearRegionName;
import cc.mrbird.febs.policy.entity.model.YearCategoryNum;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IPolicy2Service extends IService<Policy> {
    List<Policy> getPolicyListSearchAll(String search);
    List<Policy> getPolicyListByName(String name);
    List<Policy> getPolicyListSearch(String search);
    List<Policy> getPolicyListByNameAll(String search);
    List<Policy> getPolicyListGroupForm();
    List<Policy> getPolicyListGroupRank(String form);
    List<Policy> getPolicyList();
    List<Policy> getPolicyListGroupFormRank();
    List<Policy> getPolicyMultiple(Policy policy);
    List<Policy> getPolicyMultipleAll(Policy policy);
    List<Policy> getQuestionNumByPolicy2(List<String> organlist,List<String> rangelist,List<String> themelist,List<String> yearlist,List<String> namelist,List<String> fanwei);
    List<Policy> getQuestionNumByPolicy3(List<String> organlist,List<String> rangelist,List<String> themelist,List<String> yearlist,List<String> namelist,List<String> fanwei);
    List<Policy> getQuestionNumByPolicy4(List<String> organlist,List<String> rangelist,List<String> themelist,List<String> namelist,String startyear,String endyear);
    /**
     * 新增用户
     *
     * @param policy policy
     */
    void createPolicy(Policy policy);

    /**
     * 补全政策属性
     *
     * @param policyId
     */
    void completionProperties(Integer policyId);

    /**
     * 查询所有科技政策
     *
     * @param policy
     * @param request
     * @return
     */
    List<Policy> getList(Policy policy, QueryRequest request);

    /**
     * 统计总数
     *
     * @param policy
     * @return
     */
    Long getCount(Policy policy);

    /**
     * 查找政策详细信息
     *
     * @param request request
     * @param policy  政策对象，用于传递查询条件
     * @return IPage
     */
    IPage<Policy> findPolicyDetail(Policy policy, QueryRequest request);
    IPage<Policy> findPolicyDetail2(List<String> organlist,List<String> rangelist,List<String> themelist,List<String> yearlist,List<String> namelist, QueryRequest request);
    IPage<Policy> findPolicyDetail3(List<String> organlist,List<String> rangelist,List<String> themelist,List<String> yearlist,List<String> namelist, QueryRequest request);
    IPage<Policy> findPolicyDetail4(List<String> organlist,List<String> rangelist,List<String> themelist,List<String> yearlist,List<String> namelist, QueryRequest request);
    IPage<Policy> findPolicyDetail5(List<String> organlist,List<String> rangelist,List<String> themelist,List<String> namelist,String startyear,String endyear, QueryRequest request);

    /**
     * 通过政策名查找用户详细信息
     *
     * @param policyname 政策名
     * @return 政策信息
     */
    Policy findPolicyDetail(String policyname);

    /**
     * 通过政策Id查找用户详细信息
     *
     * @param policyId 政策Id
     * @return 政策信息
     */
    Policy getPolicyById(Integer policyId);


    /**
     * 查询所有Policy
     *
     * @return
     */
    List<String> getPolicyKeyWordsList();

    /**
     * 查询关键字用于更新政策语义网络图
     * 顺便将结果保存到文件中，便于下次显示。
     *
     * @param regionIds    多个地域id
     * @param intermediate 传递参数的中间类
     * @return json
     */
    JSONObject getJsonData(List<Integer> regionIds, IntermediateVariable intermediate);
    List<Policy> getQuestionNumByPolicy(List<String> organlist,List<String> rangelist,List<String> themelist,List<String> yearlist,List<String> namelist);

    /**
     * 获取政策工具信息，为政策工具表赋值(已经弃用的一种写法，后来直接调用一个方法，使用jdbc方式请求数据库获得数据，将结果写入json文件中。)
     * 可以结合实际需要，这种方法更加复杂，但是数据源配更改比较容易。
     */
    JSONObject getPolicyInstrumentTableJsonData(List<Integer> regionIds, IntermediateVariable intermediate);

    /**
     * 统计各地域不同年份出台的某一类科技政策的数量
     */
    List<PolicyStatisticalResult> getOneThemeManyRegionPolicyNumAndYear(List<Integer> regionIds, IntermediateVariable intermediate, List<String> formulaKeywords);

    /**
     * 统计各年不同类型政策数量，用堆叠条形图展示
     * variable：传递参数的中间对象
     */
    List<YearCategoryNum> getEveryYearEveryCategoryNum(IntermediateVariable variable);

    /**
     * 统计各部门出台的政策数量
     */
    List<OrganPolicyNum> getEveryOrganPolicyNum(IntermediateVariable intermediate);

    /**
     * 用于辅助政策公式 1.挖掘政策文件  2.按时间轴显示
     *
     * @param regionIds       地域ids
     * @param intermediate    中间量
     * @param formulaKeywords 公式关键词（已经组织好的）
     * @return 集合
     */
    List<PolicyYearRegionName> getPolicyYearRegionNameList(List<Integer> regionIds, IntermediateVariable intermediate, List<String> formulaKeywords);

    /**
     * 获取上位政策
     *
     * @param policyName      文件名
     * @param formulaKeywords 关键词
     * @return 上位政策年份等信息
     */
    List<PolicyYearRegionName> getSuperiorPolicyByPolicyName(String policyName, List<String> formulaKeywords);

    /**
     * 根据政策名查政策
     *
     * @param name 名
     * @return
     */
    PolicyYearRegionName getPolicyByName(String name);

    /**
     * 统计某一年一个地区不同类型政策数量
     * variable：传递参数的中间对象
     */
    List<YearCategoryNum> getOneYearOneRegionCategoryNum(IntermediateVariable variable);

    /**
     * 统计某一年国家不同类型政策数量
     * variable：传递参数的中间对象
     */
    List<YearCategoryNum> getOneYearCountryCategoryNum(IntermediateVariable variable);

    /**
     * 统计某一年某一地区某一类型的政策
     *
     * @param variable 中间变量
     * @return list
     */
    List<Policy> getOneYearOneRegionOneCategoryPolicy(IntermediateVariable variable);

    /**
     * 统计某一年国家和地方不同类型政策数量
     * variable：传递参数的中间对象
     */
    List<YearCategoryNum> getOneYearCategoryNum(IntermediateVariable variable);
}
