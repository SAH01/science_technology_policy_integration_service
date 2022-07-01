package cc.mrbird.febs.policy.service;

import cc.mrbird.febs.policy.entity.Annual;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IAnnualService extends IService<Annual> {
    List<Annual> getCorporateTechnologyActivities(String province);
    List<Annual> getUndertakeProject(String province);
    List<Annual> getEnterpriseDevelopment(String province);
}
