package cc.mrbird.febs.policy.mapper;

import cc.mrbird.febs.policy.entity.TPolicyKind2;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface TPolicyKind2Mapper extends BaseMapper<TPolicyKind2> {
    TPolicyKind2 getTPolicyKindByParentId(int parentid);
    List<TPolicyKind2> getTPolicyKindByKindoneId();
    List<TPolicyKind2> getTPolicyKindListByParentId(int parentid);
    List<TPolicyKind2> getTPolicyKindListByKindoneId(int kindid);

}
