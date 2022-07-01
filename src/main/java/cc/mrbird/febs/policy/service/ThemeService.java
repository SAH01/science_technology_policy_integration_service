package cc.mrbird.febs.policy.service;

import cc.mrbird.febs.policy.entity.Theme;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface ThemeService extends IService<Theme> {
    List<Theme> getThemeList();
}
