package cc.mrbird.febs.policy.mapper;

import cc.mrbird.febs.policy.entity.PolicyNew;
import cc.mrbird.febs.policy.entity.Theme;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface ThemeMapper extends BaseMapper<Theme> {
    List<Theme> getThemeList();
}
