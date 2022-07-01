package cc.mrbird.febs.policy.mapper;

import cc.mrbird.febs.policy.entity.IntermediateVariable;
import cc.mrbird.febs.policy.entity.PolicyStatisticalResult;
import cc.mrbird.febs.policy.entity.ScienceTechnologyEntityIndex;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ScienceTechnologyEntityIndexMapper extends BaseMapper<ScienceTechnologyEntityIndex> {

    /**
     * 根据指标id列表获得指标内容列表
     */
    List<String> getEntityIndexContentsByEntityIndexIds(@Param("entityIndexIds") List<Integer> entityIndexIds);
    /**
     * 根据指标id获得指标内容
     */
    String getEntityIndexContentByEntityIndexId(@Param("entityIndexId")Integer entityIndexId);

}
