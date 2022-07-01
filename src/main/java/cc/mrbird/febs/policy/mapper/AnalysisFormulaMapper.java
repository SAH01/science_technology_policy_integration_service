package cc.mrbird.febs.policy.mapper;

import cc.mrbird.febs.policy.entity.AnalysisFormula;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AnalysisFormulaMapper extends BaseMapper<AnalysisFormula> {

    List<AnalysisFormula> getAnalysisFormulas(@Param("formula") AnalysisFormula formula);
}
