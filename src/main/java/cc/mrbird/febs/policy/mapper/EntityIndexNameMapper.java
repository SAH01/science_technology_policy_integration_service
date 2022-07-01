package cc.mrbird.febs.policy.mapper;

import cc.mrbird.febs.policy.entity.EntityIndexName;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @author WangRY
 */
public interface EntityIndexNameMapper extends BaseMapper<EntityIndexName> {
    /**
     * 自动补全
     * @param name 用户输入
     * @return 实体名集合
     */
    List<String> getSimilarityNames(String name);
}
