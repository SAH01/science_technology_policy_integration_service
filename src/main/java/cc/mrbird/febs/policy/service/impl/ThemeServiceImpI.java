package cc.mrbird.febs.policy.service.impl;

import cc.mrbird.febs.policy.entity.PolicyType;
import cc.mrbird.febs.policy.entity.Theme;
import cc.mrbird.febs.policy.mapper.ThemeMapper;
import cc.mrbird.febs.policy.service.ThemeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ThemeServiceImpI extends ServiceImpl<ThemeMapper, Theme> implements ThemeService {
    @Override
    public List<Theme> getThemeList() {
        return this.baseMapper.getThemeList();
    }
}
