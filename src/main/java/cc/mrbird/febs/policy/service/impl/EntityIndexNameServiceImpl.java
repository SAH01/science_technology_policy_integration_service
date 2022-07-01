package cc.mrbird.febs.policy.service.impl;

import cc.mrbird.febs.policy.entity.EntityIndexName;
import cc.mrbird.febs.policy.mapper.EntityIndexNameMapper;
import cc.mrbird.febs.policy.service.IEntityIndexNameService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntityIndexNameServiceImpl extends ServiceImpl<EntityIndexNameMapper, EntityIndexName> implements IEntityIndexNameService {
    @Override
    public List<String> getSimilarityNames(String name) {
        return this.baseMapper.getSimilarityNames(name);
    }
}
