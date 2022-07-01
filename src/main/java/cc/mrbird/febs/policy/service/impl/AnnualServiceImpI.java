package cc.mrbird.febs.policy.service.impl;

import cc.mrbird.febs.policy.entity.Annual;
import cc.mrbird.febs.policy.mapper.AnnualMapper;
import cc.mrbird.febs.policy.service.IAnnualService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class AnnualServiceImpI extends ServiceImpl<AnnualMapper, Annual> implements IAnnualService {
    @Override
    public List<Annual> getCorporateTechnologyActivities(String province) {
        List<Annual> annualList=this.baseMapper.getCorporateTechnologyActivities(province);
        List<Annual> annuals=new ArrayList<Annual>();
        for (Annual tmpBookData:annualList) {
            if(tmpBookData.getName().equals("规模以上工业企业R&D经费内部支出")){
                for (Annual tmpData:annualList) {
                    if(tmpData.getName().equals("规模以上工业企业R&D经费外部支出")&&tmpData.getYear().equals(tmpBookData.getYear())){
                        double sum=0;
                        if(!tmpBookData.getData().equals("")&&!tmpData.getData().equals("")) {
                            sum = Double.parseDouble(tmpBookData.getData()) + Double.parseDouble(tmpData.getData());
                        }
                        else if(!tmpBookData.getData().equals("")&&tmpData.getData().equals(""))
                        {
                            sum=Double.parseDouble(tmpBookData.getData());
                        }
                        else if(tmpBookData.getData().equals("")&&!tmpData.getData().equals(""))
                        {
                            sum=Double.parseDouble(tmpData.getData());
                        }
                        Annual combined = new Annual();
                        combined.setName("规模以上工业企业R&D经费支出总额");
                        combined.setData(String.format("%.2f",sum));
                        combined.setUnit_c(tmpData.getUnit_c());
                        combined.setYear(tmpData.getYear());
                        annuals.add(combined);
                        break;
                    }
                }
            }else if(!tmpBookData.getName().equals("规模以上工业企业R&D经费外部支出")){
                annuals.add(tmpBookData);
            }
        }
        return annuals;
    }
    @Override
    public List<Annual> getUndertakeProject(String province) {
        return this.baseMapper.getUndertakeProject(province);
    }

    @Override
    public List<Annual> getEnterpriseDevelopment(String province) {
        return this.baseMapper.getEnterpriseDevelopment(province);
    }
}
