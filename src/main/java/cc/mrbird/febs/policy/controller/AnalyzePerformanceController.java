package cc.mrbird.febs.policy.controller;


import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.policy.entity.Policy;
import cc.mrbird.febs.policy.entity.PolicyChange;
import cc.mrbird.febs.policy.entity.PolicyContrast;
import cc.mrbird.febs.policy.entity.TPolicyKind;
import cc.mrbird.febs.policy.service.IPolicyChangeService;
import cc.mrbird.febs.policy.service.IPolicyContrastService;
import cc.mrbird.febs.policy.service.ITPolicyKindService;
import cc.mrbird.febs.policy.service.impl.Policy2ServiceImpl;
import cc.mrbird.febs.policy.utils.CreateFileUtil;
import cc.mrbird.febs.policy.utils.CreateMapData;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 政策绩效分析控制器
 * 主要用于返回index页面的地图所需的数据，以及更新地图数据响应的操作
 */
@Slf4j
@RestController
@RequestMapping("performance")
public class AnalyzePerformanceController {
    @Autowired
    private IPolicyContrastService policyContrastService;
    @Autowired
    private ITPolicyKindService itPolicyKindService;
    @Autowired
    private Policy2ServiceImpl policy2Service;
    @Autowired
    private IPolicyChangeService policyChangeService;




    /**
     * 返回index页面的地图所需的数据
     */
    @GetMapping("getMapData")
    public FebsResponse getMapData() {
        JSONObject json = CreateFileUtil.readJsonFile("G:\\A科技政策系统\\science_technology_policy_integration_service (3)/json_for_CreateMapData/yearRegionValueAll.json");
        //JSONObject json = CreateFileUtil.readJsonFile("json_for_CreateMapData/xinjian.json");
        if (json != null){
            return new FebsResponse().success().data(json);
        }
        return new FebsResponse().message("未找到响应数据，请联系管理员。");
    }

    @GetMapping("getSearch/{policySearch}")
    public FebsResponse getMap(@PathVariable String policySearch) {
        List<Policy> policyList=this.policy2Service.getPolicyListSearch(policySearch);
        List<Policy> policyList1=this.policy2Service.getPolicyListSearchAll(policySearch);
        String[] list2=new String[] {"国家级","省级/直辖市","市级"};
        JSONObject q1 = new JSONObject();
        JSONObject q2 = new JSONObject();
        JSONObject q3 = new JSONObject();
        JSONObject q4 = new JSONObject();
        JSONObject q5 = new JSONObject();
        JSONArray array = new JSONArray();
        JSONArray array2 = new JSONArray();
        JSONArray array3 = new JSONArray();
        JSONArray array4 = new JSONArray();
        JSONArray array5 = new JSONArray();
        JSONArray array6 = new JSONArray();
        for(int i=policyList1.size()-1;i>=0;i--)
        {
            q3 = new JSONObject();
            array6 = new JSONArray();
            array4 = new JSONArray();
            array.add(policyList1.get(i).getAllsum());
            array2.add(policyList1.get(i).getForm());
            q3.put("title",policyList1.get(i).getForm()+" ("+policyList1.get(i).getAllsum()+")");
            q3.put("id",1);
            for(int n=0;n<list2.length;n++)
            {
                int num=0;
                q5 = new JSONObject();
                array4 = new JSONArray();
                for(int j=0;j<policyList.size();j++)
                {
                    if(policyList.get(j).getForm().equals(policyList1.get(i).getForm())&&policyList.get(j).getRank().equals(list2[n]))
                    {
//                        q4 = new JSONObject();
//                        q4.put("title",policyList.get(j).getName());
//                        q4.put("id",policyList.get(j).getId());
//                        array4.add(q4);
                        num+=1;
                    }
                }
                q5.put("title",list2[n]+" ("+num+")");
                q5.put("id",0);

               // q5.put("children",array4);
                array6.add(q5);
            }
//            for(int j=0;j<policyList.size();j++)
//            {
//                if(policyList.get(j).getForm().equals(policyList1.get(i).getForm()))
//                {
//                    q4 = new JSONObject();
//                    q4.put("title",policyList.get(j).getName());
//                    q4.put("id",policyList.get(j).getId());
//                    array4.add(q4);
//                }
//            }
            q3.put("children",array6);
            array5.add(q3);
        }
        q1.put("name", "政策数量");
        q1.put("data", array);
        array3.add(q1);
        q2.put("data", array3);
        q2.put("type", array2);
        q2.put("shu",array5);

        return new FebsResponse().success().data(q2);
    }



    @GetMapping("getMap/{kindid}")
    public FebsResponse getMap(@PathVariable int kindid) {
        JSONObject json=new JSONObject();
        json = CreateFileUtil.readJsonFile("G:\\A科技政策系统\\science_technology_policy_integration_service (3)/json_for_CreateMapData/"+kindid+".json");
        if (json != null){
            return new FebsResponse().success().data(json);
        }
        return new FebsResponse().message("未找到响应数据，请联系管理员。");
    }
    @GetMapping("getYan/{kindid}")
    public FebsResponse getYan(@PathVariable int kindid) {
        JSONObject q2 = new JSONObject();
        JSONArray array=new JSONArray();
        array=CreateFileUtil.readJSONArrayFile("shiyan/policynew.json");
        return new FebsResponse().success().data(array);
    }
    @GetMapping("getT/{kindid}")
    public FebsResponse getT(@PathVariable int kindid) {
        JSONObject q = new JSONObject();
        JSONArray array=new JSONArray();

        q=CreateFileUtil.readJsonFile("shiyan/tu.json");

        return new FebsResponse().success().data(q);
    }

    @GetMapping("getEvolution/{typeid}")
    public FebsResponse getEvolution(@PathVariable int typeid) {
        JSONObject q = new JSONObject();
        if(typeid==0)
        {
            q=CreateFileUtil.readJsonFile("G:\\A科技政策系统\\science_technology_policy_integration_service (3)/json_for_evolution/evolution.json");
        }
        else
        {
            q=CreateFileUtil.readJsonFile("G:\\A科技政策系统\\science_technology_policy_integration_service (3)/json_for_evolution/"+typeid+".json");
        }
        JSONArray array=new JSONArray();


        return new FebsResponse().success().data(q);
    }
    @GetMapping("getTime/{kindid}")
    public FebsResponse getTime(@PathVariable int kindid) {
        JSONArray array=new JSONArray();
        JSONArray array2=new JSONArray();
        JSONObject q2 = new JSONObject();
        List<PolicyContrast> list=policyContrastService.getPolicyContrastListByTime(kindid);
        List<Policy> listpolicy=new ArrayList<Policy>();
        for(int i=0;i<list.size();i++)
        {
            String time=list.get(i).getPolicytime();
            String main=list.get(i).getPolicymain();
            String key[]=list.get(i).getPolicykey().split(" ");
            if(key.length>1) {
                for (int j = 0; j < key.length; j++) {
                    if (main.indexOf(key[j]) != -1) {
                        main = main.replace(key[j], "<b style='color:red'>" + key[j] + "</b>");
                    }
                }
            }
            array.add(main);
            array2.add(time);
        }
        q2.put("time",array2);
        q2.put("main",array);
        return new FebsResponse().success().data(q2);
    }
    @GetMapping("getChange/{name}")
    public FebsResponse getChange(@PathVariable String name) {
        JSONArray array=new JSONArray();
        JSONArray array2=new JSONArray();
        JSONArray array3=new JSONArray();
        JSONArray array4=new JSONArray();
        JSONObject q2 = new JSONObject();
        System.out.println(name);
        List<PolicyChange> list=policyChangeService.getPolicyChangeList(name);
        for(int i=0;i<list.size();i++)
        {
            String time=list.get(i).getYear();
            String main=list.get(i).getChange();
            String namea=list.get(i).getName();
            int id=list.get(i).getId();
            array3.add(id);
            array.add(main);
            array2.add(time);
            array4.add(namea);
        }
        q2.put("time",array2);
        q2.put("main",array);
        q2.put("id",array3);
        q2.put("policyname",array4);
        return new FebsResponse().success().data(q2);
    }
    @GetMapping("getEvo/{kindid}")
    public FebsResponse getEvo(@PathVariable int kindid) {

        if(kindid==0)
        {
            JSONArray array=new JSONArray();
            JSONArray array4 = new JSONArray();
            JSONObject q2 = new JSONObject();
            List<PolicyContrast> list=policyContrastService.getAllsum();
            JSONObject q1 = new JSONObject();
            q1.put("name","");
            JSONArray array2 = new JSONArray();
            JSONArray array3 = new JSONArray();
            for(int i=0;i<list.size();i++)
            {
                int parentid=list.get(i).getParentid();
                List<TPolicyKind> tpolicykind=itPolicyKindService.getTPolicyKindList(parentid);
                array2.add(list.get(i).getAllnum());
                array3.add(tpolicykind.get(0).getKindName());
            }
            q1.put("data",array2);
            array.add(q1);
            q2.put("infor",array);
            q2.put("city",array3);
            if (q2 != null) {

                return new FebsResponse().success().data(q2);
            }
        }
        else
        {
            JSONArray array=new JSONArray();
            JSONArray array2=new JSONArray();
            JSONArray array3 = new JSONArray();
            JSONObject q1 = new JSONObject();
            JSONObject q2 = new JSONObject();
            List<PolicyContrast> policyContrastList=policyContrastService.getPolicyContrastListByYear(kindid);
            for(int i=0;i<policyContrastList.size();i++)
            {
                if(policyContrastList.get(i).getYear()!=null) {
                    String year = policyContrastList.get(i).getYear();
                    int allnum = policyContrastList.get(i).getAllnum();
                    array.add(year);
                    array2.add(allnum);
                }
            }
            q1.put("data", array2);
            q1.put("name", "政策数量");
            array3.add(q1);
            q2.put("data", array3);
            q2.put("type", array);
            System.out.println(q2);
//            q2= CreateFileUtil.readJsonFile("D:/json_science/json_for_evolution2/"+kindid+".json");
            if (q2 != null){
                return new FebsResponse().success().data(q2);
            }
        }

        return new FebsResponse().message("未找到响应数据，请联系管理员。");
    }

    @GetMapping("getJs/{kindid}")
    public FebsResponse getJs(@PathVariable int kindid) {

        if(kindid==0)
        {
            JSONArray array=new JSONArray();
            JSONArray array4 = new JSONArray();
            JSONObject q2 = new JSONObject();
            List<PolicyContrast> list=policyContrastService.getAllsum();
            JSONObject q1 = new JSONObject();
//            q1.put("name","");
            JSONArray array2 = new JSONArray();
            JSONArray array3 = new JSONArray();
            for(int i=0;i<list.size();i++)
            {
                int parentid=list.get(i).getParentid();
                List<TPolicyKind> tpolicykind=itPolicyKindService.getTPolicyKindList(parentid);
                array2.add(list.get(i).getAllnum());
                array3.add(tpolicykind.get(0).getKindName());
            }
            q1.put("data",array2);
            array.add(q1);
            q2.put("infor",array);
            q2.put("city",array3);
            if (q2 != null) {
                return new FebsResponse().success().data(q2);
            }
        }
        else
        {
            List<TPolicyKind> policyKinds=itPolicyKindService.getPTPolicyKindList(kindid);
            List<PolicyContrast> policyContrastList=policyContrastService.getPolicyContrastListByCity(kindid);
            List<String> city=new ArrayList<String>();
            JSONArray array=new JSONArray();
            JSONArray array2 = new JSONArray();
            JSONArray array3 = new JSONArray();
            JSONObject q2 = new JSONObject();
            JSONObject q1 = new JSONObject();
            String[] province=new String[] {"全国","北京","天津","上海","河北","广东","四川","辽宁","吉林","黑龙江","江苏","浙江","安徽","福建","江西","山东","河南","湖北","湖南","山西","广西","海南","重庆","内蒙古","贵州","云南"
                    ,"西藏","陕西","甘肃","青海","宁夏","新疆","台湾","香港","澳门","石家庄","唐山","秦皇岛","邯郸","邢台","保定","张家口","承德","沧州","廊坊","衡水"};
            for(int i=0;i<province.length;i++)
            {
                int flag=0;
                for(int j=0;j<policyContrastList.size();j++)
                {
                    if(policyContrastList.get(j).getCity()!=null) {
                        if (policyContrastList.get(j).getCity().equals(province[i]) && flag == 0) {
                            city.add(province[i]);
                            array.add(province[i]);
                            flag = 1;
                        }
                    }
                }
            }
            for(int i=0;i<policyKinds.size();i++)
            {
                String name=policyKinds.get(i).getKindName();
                q1 = new JSONObject();
                array2 = new JSONArray();
                for(int j=0;j<city.size();j++)
                {
                    int num=0;
                    for(int m=0;m<policyContrastList.size();m++)
                    {
                        int allnum=policyContrastList.get(m).getAllnum();
                        String citypro=policyContrastList.get(m).getCity();
                        String kindname=policyContrastList.get(m).getKind();
                        if(city.get(j).equals(citypro)&&name.equals(kindname))
                        {
                            num=allnum;
                        }
                    }
                    array2.add(num);
                }
                q1.put("data",array2);
                q1.put("name",name);
                array3.add(q1);

            }
            q2.put("city",array);
            q2.put("infor",array3);
            System.out.println(q2);
           // q2= CreateFileUtil.readJsonFile("D:/json_science/json_for_kind/"+kindid+".json");
            if (q2 != null){
                return new FebsResponse().success().data(q2);
            }
        }

        return new FebsResponse().message("未找到响应数据，请联系管理员。");
    }
    /**
     * 返回index页面的地图所需的数据
     */
    @GetMapping("updateMapData")
    public FebsResponse updateMapData() {
        boolean updateSuccess;
        try{
            updateSuccess = CreateMapData.createMapData2();
        }catch (IOException e){
            return new FebsResponse().message("更新过程中出现错误，请联系管理员。");
        }
        if(updateSuccess){
            return new FebsResponse().success();
        }else {
            return new FebsResponse().message("更新过程中出现错误，请联系管理员。");
        }

    }

}
