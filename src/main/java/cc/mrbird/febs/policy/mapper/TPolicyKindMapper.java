package cc.mrbird.febs.policy.mapper;

import cc.mrbird.febs.policy.entity.TPolicyKind;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface TPolicyKindMapper extends BaseMapper<TPolicyKind> {
    List<TPolicyKind> getTPolicyKindList(int id);
    List<TPolicyKind> getPTPolicyKindList(int id);
    List<TPolicyKind> getList();
    void deleteByKindoneId(int kindoneId);
}
