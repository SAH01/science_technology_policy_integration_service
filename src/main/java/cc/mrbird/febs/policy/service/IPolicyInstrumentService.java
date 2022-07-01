package cc.mrbird.febs.policy.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.policy.entity.PolicyDimensionalityTree;
import cc.mrbird.febs.policy.entity.PolicyInstrument;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author MrBird
 */
public interface IPolicyInstrumentService extends IService<PolicyInstrument> {

    /**
     * 获取政策工具树（下拉选使用）
     *
     * @return 政策工具树集合
     */
    List<PolicyDimensionalityTree<PolicyInstrument>> findDepts();

    /**
     * 获取政策工具列表（树形列表）
     *
     * @param dept 政策工具对象（传递查询参数）
     * @return 政策工具树
     */
    List<PolicyDimensionalityTree<PolicyInstrument>> findDepts(PolicyInstrument dept);

    /**
     * 获取政策工具树（供Excel导出）
     *
     * @param dept    政策工具对象（传递查询参数）
     * @param request QueryRequest
     * @return List<PolicyInstrument>
     */
    List<PolicyInstrument> findDepts(PolicyInstrument dept, QueryRequest request);

    /**
     * 新增政策工具
     *
     * @param dept 政策工具对象
     */
    void createDept(PolicyInstrument dept);

    /**
     * 修改政策工具
     *
     * @param dept 政策工具对象
     */
    void updateDept(PolicyInstrument dept);

    /**
     * 删除政策工具
     *
     * @param deptIds 政策工具 ID集合
     */
    void deleteDepts(String[] deptIds);
}
