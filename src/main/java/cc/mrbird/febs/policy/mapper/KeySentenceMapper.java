package cc.mrbird.febs.policy.mapper;

import cc.mrbird.febs.policy.entity.KeySentence;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface KeySentenceMapper extends BaseMapper<KeySentence> {
    /**
     * 根据公式id获得公式内容
     * @param formulaId
     * @return
     */
    String getFormulaContentById(Long formulaId);
    List<KeySentence> getKeyPolicyFileListBySQL(@Param("finalSQL") StringBuilder finalSQL);
}
