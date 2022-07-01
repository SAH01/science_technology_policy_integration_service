package cc.mrbird.febs.policy.service.impl;

import cc.mrbird.febs.policy.entity.IntermediateVariable;
import cc.mrbird.febs.policy.entity.YearBookData;
import cc.mrbird.febs.policy.mapper.YearBookDataMapper;
import cc.mrbird.febs.policy.service.IYearBookDataService;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@DS("oracle")
public class YearBookDataServiceImpl extends ServiceImpl<YearBookDataMapper, YearBookData> implements IYearBookDataService {
    @Override
    public List<YearBookData> selectListBySQL(String SQL) {
        return baseMapper.selectListBySQL(SQL);
    }

    @Override
    public List<YearBookData> getEntityNum(List<String> regionIds, IntermediateVariable intermediate, List<String> searchValues) {
        return this.baseMapper.getEntityNum(regionIds, intermediate, searchValues);
    }

    @Override
    public List<YearBookData> getCorporateTechnologyActivities(String regionId) {
        /* 对原始的进行合并
        * 具体是对《规模以上工业企业R&D经费内部支出》和《规模以上工业企业R&D经费外部支出》合并 */
        List<YearBookData> originalData = this.baseMapper.getCorporateTechnologyActivities(regionId);
        List<YearBookData> combineData = new ArrayList<>();
        for (YearBookData tmpBookData:originalData) {
            if(tmpBookData.getName().equals("规模以上工业企业R&D经费内部支出")){
                for (YearBookData tmpData:originalData) {
                    if(tmpData.getName().equals("规模以上工业企业R&D经费外部支出")&&tmpData.getYears().equals(tmpBookData.getYears())){
                        double sum = Double.parseDouble(tmpBookData.getValue())+Double.parseDouble(tmpData.getValue());
                        YearBookData combined = new YearBookData();
                        combined.setName("规模以上工业企业R&D经费支出总额");
                        combined.setValue(String.format("%.2f",sum));
                        combined.setUnitC(tmpData.getUnitC());
                        combined.setYears(tmpData.getYears());
                        combineData.add(combined);
                        break;
                    }
                }
            }else if(!tmpBookData.getName().equals("规模以上工业企业R&D经费外部支出")){
                combineData.add(tmpBookData);
            }
        }
        return combineData;
    }

    @Override
    public List<YearBookData> getUndertakeProject(String regionId) {
        return this.baseMapper.getUndertakeProject(regionId);
    }

    @Override
    public List<YearBookData> getEnterpriseDevelopment(String regionId) {
        return this.baseMapper.getEnterpriseDevelopment(regionId);
    }
}
