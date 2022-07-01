package cc.mrbird.febs.policy.service;

import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.policy.entity.ScienceTechnologyEntityIndex;
import cc.mrbird.febs.policy.entity.FormulaAndEntityTree;
import cc.mrbird.febs.policy.entity.ScienceTechnologyEntityIndex;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author WangRY
 */
public interface IScienceTechnologyEntityIndexService extends IService<ScienceTechnologyEntityIndex> {
    /**
     * 获取科技实体树（下拉选使用）
     *
     * @return 科技实体树集合
     */
    List<FormulaAndEntityTree<ScienceTechnologyEntityIndex>> findEntityIndex();

    /**
     * 获取科技实体列表（树形列表）
     *
     * @param entityIndex 科技实体对象（传递查询参数）
     * @return 科技实体树
     */
    List<FormulaAndEntityTree<ScienceTechnologyEntityIndex>> findEntityIndex(ScienceTechnologyEntityIndex entityIndex);

    /**
     * 获取科技实体树（供Excel导出）
     *
     * @param entityIndex    科技实体对象（传递查询参数）
     * @param request QueryRequest
     * @return List<ScienceTechnologyEntityIndex>
     */
    List<ScienceTechnologyEntityIndex> findEntityIndexs(ScienceTechnologyEntityIndex entityIndex, QueryRequest request);

    /**
     * 新增科技实体
     *
     * @param entityIndex 科技实体对象
     */
    void createEntityIndex(ScienceTechnologyEntityIndex entityIndex);

    /**
     * 修改科技实体
     *
     * @param entityIndex 科技实体对象
     */
    void updateEntityIndex(ScienceTechnologyEntityIndex entityIndex);

    /**
     * 删除科技实体
     *
     * @param entityIndexIds 科技实体 ID集合
     */
    void deleteEntityIndexs(String[] entityIndexIds);

    /**
     * 根据指标id列表获得指标内容列表
     */
    List<String> getEntityIndexContentsByEntityIndexIds(List<Integer> entityIndexIds);
    /**
     * 根据指标id获得指标内容
     */
    String getEntityIndexContentByEntityIndexId(Integer entityIndexId);
}
