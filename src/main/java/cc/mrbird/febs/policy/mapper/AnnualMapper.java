package cc.mrbird.febs.policy.mapper;

import cc.mrbird.febs.policy.entity.Annual;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface AnnualMapper extends BaseMapper<Annual> {
    List<Annual> getCorporateTechnologyActivities(String province);
    List<Annual> getUndertakeProject(String province);
    List<Annual> getEnterpriseDevelopment(String province);

}
