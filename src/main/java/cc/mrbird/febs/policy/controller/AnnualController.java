package cc.mrbird.febs.policy.controller;

import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.policy.entity.Annual;
import cc.mrbird.febs.policy.entity.YearBookData;
import cc.mrbird.febs.policy.entity.model.ShowData;
import cc.mrbird.febs.policy.service.IAnnualService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("annualjson")
public class AnnualController {
    @Autowired
    IAnnualService annualService;

    @GetMapping("corporateTechnologyActivities/{regionId}")
    public FebsResponse getCorporateTechnologyActivities(@NotBlank(message = "{required}") @PathVariable String regionId) {
        List<Annual> annualList=annualService.getCorporateTechnologyActivities(regionId);

        String[] names = new String[]{"科技活动人员数", "R&D人员", "规模以上工业企业R&D经费支出总额", "规模以上工业企业新产品开发经费支出"};
        JSONObject JSON=commonMethod(annualList, names, 2, new String[]{"万人","万人", "万元","万元"});
        return new FebsResponse().success().data(JSON);
    }

    @GetMapping("undertakeProject/{regionId}")
    public FebsResponse getUndertakeProject(@NotBlank(message = "{required}") @PathVariable String regionId) {
        List<Annual> annualList=annualService.getUndertakeProject(regionId);
        System.out.println(annualList.size());
        List<YearBookData> combineData=new ArrayList<>();
        String[] names = new String[]{"研究与开发机构R&D课题数", "高等学校R&D课题数", "研究与开发机构R&D课题投入人员", "高等学校R&D课题投入人员", "研究与开发机构R&D课题投入经费", "高等学校R&D课题投入经费"};
        JSONObject JSON=commonMethod(annualList, names, 4, new String[]{"项", "项","人/年","人/年","万元","万元"});
        System.out.println(JSON);
        return new FebsResponse().success().data(JSON);
    }

    @GetMapping("enterpriseDevelopment/{regionId}")
    public FebsResponse getEnterpriseDevelopment(@NotBlank(message = "{required}") @PathVariable String regionId) {
        List<Annual> annualList=annualService.getEnterpriseDevelopment(regionId);
        String[] names = new String[]{"国家高新技术产业开发区总产值", "国家高新技术产业开发区实交税金", "开发区高新技术企业数"};
        JSONObject JSON = commonMethod(annualList, names, 2, new String[]{"万元", "万元", "万个"});
        return new FebsResponse().success().data(JSON);

    }
    public int judgeTableJSON(List<Annual> annualList)
    {
        int flag=0;
        for(int i=0;i<annualList.size();i++)
        {
            if(!annualList.get(i).getData().equals("")&&!annualList.get(i).getData().equals("0.00"))
            {
                flag=1;
            }
        }

        return flag;
    }
    private JSONObject commonMethod(List<Annual> combineData, String[] names, int dividing, String[] unit_c) {
        //初始化
        String province=combineData.get(0).getProvince();
        List<String> years = new ArrayList<>();
        for (Annual tmpData : combineData) {
            if (!years.contains(tmpData.getYear())) {
                years.add(tmpData.getYear());
            }
        }
        Collections.sort(years);
        ShowData[] data = new ShowData[names.length];
        for (int i = 0; i < names.length; i++) {
            data[i] = new ShowData();
            data[i].setName(names[i]);
            if (i < dividing) {
                data[i].setType("bar");
                data[i].setUnit_c(unit_c[i]);
            } else {
                data[i].setType("line");
                data[i].setUnit_c(unit_c[i]);
            }
            data[i].setValues(new String[years.size()]);
        }
        //赋值
        for (Annual tmpData : combineData) {
            if(tmpData.getData().equals("")||tmpData.getData().equals("0.00"))
            {
                data[indexOf(names, tmpData.getName())].getValues()[years.indexOf(tmpData.getYear())] =null;
            }
            else
            {
                data[indexOf(names, tmpData.getName())].getValues()[years.indexOf(tmpData.getYear())] = tmpData.getData();
            }

        }
        JSONObject json = new JSONObject();
        json.put("names", names);
        json.put("years", years);
        json.put("data", data);
        json.put("province",province);
        return json;
    }

    private int indexOf(String[] list, String item) {
        if (item == null)
            return -1;
        for (int i = 0; i < list.length; i++) {
            if (item.equals(list[i])) {
                return i;
            }
        }
        return -1;
    }

}
