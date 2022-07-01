package cc.mrbird.febs.policy.controller;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.policy.entity.*;
import cc.mrbird.febs.policy.service.*;
import cc.mrbird.febs.policy.service.impl.Policy2ServiceImpl;
import cc.mrbird.febs.policy.utils.ClassQuestionKeywords;
import cc.mrbird.febs.policy.utils.CreateFileUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping("jsondata")
public class AnalyzeJsonDataController extends BaseController {
    @Autowired
    private IPolicyContrastService policyContrastService;
    @Autowired
    private ITPolicyKindService itPolicyKindService;
    @Autowired
    private Policy2ServiceImpl policy2Service;
    @Autowired
    private IPolicyanService policyanService;
    @Autowired
    private IPolicyRankService policyRankService;
    @Autowired
    private IPolicySimilarityService policySimilarityService;
    @Autowired
    private ThemeService themeService;
    @GetMapping("getTreeDataMore/{data}")
    public FebsResponse getTreeDataMore(@PathVariable String data) {

        String a[]=data.split(",");
        Policy policy=new Policy();
        policy.setName(a[0].trim());
        if(a[1]!="") {
            String b[] = a[1].trim().split(" - ");
            if (b.length > 1) {
                String createTimeFrom = b[0];
                String createTimeTo = b[1];
                policy.setCreateTimeFrom(createTimeFrom);
                policy.setCreateTimeTo(createTimeTo);
            }
        }
        policy.setKeyword(a[2].trim());
        policy.setOrgan(a[3].trim());
        policy.setText(a[4].trim());
        List<Policy> policyList=policy2Service.getPolicyMultiple(policy);
        List<Policy> policyList1=policy2Service.getPolicyMultipleAll(policy);
        String[] list=new String[] {"综合","科研机构改革","科技计划管理","科技经费与财务","基础研究与科研基地","企业技术进步与高新技术产业化","农村科技与社会发展","科技人才","科技中介服务","科技条件与标准","科技金融与税收","科技成果与知识产权","科学技术普及","科技奖励","国际科技合作"};
        String[] list1=new String[] {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o"};
        String[] list2=new String[] {"国家级","省级/直辖市","市级"};
        JSONObject q1 = new JSONObject();
        JSONObject q2 = new JSONObject();
        JSONObject q3 = new JSONObject();
        JSONArray array = new JSONArray();
        JSONArray array2 = new JSONArray();
        JSONArray array3 = new JSONArray();
        for(int i=policyList1.size()-1;i>=0;i--)
        {
            q1 = new JSONObject();
            //array = new JSONArray();
            array3 = new JSONArray();
            int flag=0;
            for(int j=0;j<list.length;j++)
            {
                if(list[j].equals(policyList1.get(i).getForm()))
                {
                    String name=policyList1.get(i).getForm()+" ("+policyList1.get(i).getAllsum()+")";
                    q1.put("title", name);
                    q1.put("id", list1[j]);
                    flag=1;
                }
            }
            if(flag==1) {
                for(int n=0;n<list2.length;n++)
                {
                    int num=0;
                    q3 = new JSONObject();
                    array = new JSONArray();
                    for (int j = 0; j < policyList.size(); j++) {
                        if (policyList1.get(i).getForm().equals(policyList.get(j).getForm())&&policyList.get(j).getRank().equals(list2[n])) {
                            num+=1;
                        }
                    }
                    String listname=list2[n]+" ("+num+")";
                    q3.put("title", listname);
                    q3.put("id", 0);
                    array3.add(q3);

                }
            }
            q1.put("children", array3);
            array2.add(q1);
        }
        //JSONArray array = CreateFileUtil.readJSONArrayFile("D:/json_science/json_for_type/policytyperank.json");
        if (array2 != null){
            return new FebsResponse().success().data(array2);
        }
        return new FebsResponse().message("未找到响应数据，请联系管理员。");
    }

    @GetMapping("getTreeDataMore2/{data}")
    public FebsResponse getTreeDataMore2(@PathVariable String data) {

        String a[]=data.split(",");
        Policy policy=new Policy();
        policy.setName(a[0].trim());
        if(a[1]!="") {
            String b[] = a[1].trim().split(" - ");
            if (b.length > 1) {
                String createTimeFrom = b[0];
                String createTimeTo = b[1];
                policy.setCreateTimeFrom(createTimeFrom);
                policy.setCreateTimeTo(createTimeTo);
            }
        }
        policy.setKeyword(a[2].trim());
        policy.setOrgan(a[3].trim());
        policy.setText(a[4].trim());
        List<Policy> policyList=policy2Service.getPolicyMultiple(policy);
        List<Policy> policyList1=policy2Service.getPolicyMultipleAll(policy);
        String[] list=new String[] {"综合","科研机构改革","科技计划管理","科技经费与财务","基础研究与科研基地","企业技术进步与高新技术产业化","农村科技与社会发展","科技人才","科技中介服务","科技条件与标准","科技金融与税收","科技成果与知识产权","科学技术普及","科技奖励","国际科技合作"};
        String[] list1=new String[] {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o"};
        String[] list2=new String[] {"国家级","省级/直辖市","市级"};
        JSONObject q1 = new JSONObject();
        JSONObject q2 = new JSONObject();
        JSONObject q3 = new JSONObject();
        JSONArray array = new JSONArray();
        JSONArray array2 = new JSONArray();
        JSONArray array3 = new JSONArray();
        for(int i=policyList1.size()-1;i>=0;i--)
        {
            q1 = new JSONObject();
            //array = new JSONArray();
            array3 = new JSONArray();
            int flag=0;
            for(int j=0;j<list.length;j++)
            {
                if(list[j].equals(policyList1.get(i).getForm()))
                {
                    String name=policyList1.get(i).getForm()+" ("+policyList1.get(i).getAllsum()+")";
                    q1.put("title", name);
                    q1.put("id", list1[j]);
                    flag=1;
                }
            }
            if(flag==1) {
                for(int n=0;n<list2.length;n++)
                {
                    int num=0;
                    q3 = new JSONObject();
                    array = new JSONArray();
                    for (int j = 0; j < policyList.size(); j++) {
                        if (policyList1.get(i).getForm().equals(policyList.get(j).getForm())&&policyList.get(j).getRank().equals(list2[n])) {
                            num+=1;
                        }
                    }
                    String listname=list2[n]+" ("+num+")";
                    q3.put("title", listname);
                    q3.put("id", 0);
                    array3.add(q3);

                }
            }
            q1.put("children", array3);
            array2.add(q1);
        }
        //JSONArray array = CreateFileUtil.readJSONArrayFile("D:/json_science/json_for_type/policytyperank.json");
        if (array2 != null){
            return new FebsResponse().success().data(array2);
        }
        return new FebsResponse().message("未找到响应数据，请联系管理员。");
    }
    @GetMapping("getTreeData")
    public FebsResponse getMapData() {
        Policy policy=new Policy();
        List<Policy> policyList=policy2Service.getPolicyListGroupFormRank();
        List<Policy> policyList1=policy2Service.getPolicyListGroupForm();
        String[] list=new String[] {"综合","科研机构改革","科技计划管理","科技经费与财务","基础研究与科研基地","基础研究","平台基地","企业技术进步与高新技术产业化","企业","产业","创新载体","农村科技与社会发展","科技人才","科技中介服务","科技条件与标准","科技金融与税收","科技成果与知识产权","科学技术普及","科技奖励","国际科技合作"};
        String[] list1=new String[] {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v"};
        String[] list2=new String[] {"国家级","省级/直辖市","市级"};
        String[] list3=new String[] {"基础研究","平台基地","企业","产业","创新载体"};
        JSONObject q1 = new JSONObject();
        JSONObject q2 = new JSONObject();
        JSONObject q3 = new JSONObject();
        JSONArray array = new JSONArray();
        JSONArray array2 = new JSONArray();
        JSONArray array3 = new JSONArray();
        for(int i=policyList1.size()-1;i>=0;i--)
        {
            q1 = new JSONObject();
            //array = new JSONArray();
            array3 = new JSONArray();
            int flag=0;
            for(int j=0;j<list.length;j++)
            {
                if(list[j].equals(policyList1.get(i).getForm()))
                {
                    String name=policyList1.get(i).getForm()+" ("+policyList1.get(i).getAllsum()+")";
                    q1.put("title", name);
                    q1.put("id", list1[j]);
                    flag=1;
                    if(list[j].equals("基础研究与科研基地")||list[j].equals("企业技术进步与高新技术产业化")){
                        flag=2;
                    }
                }
            }
            if(flag==1) {
                for(int n=0;n<list2.length;n++)
                {
                    int num=0;
                    q3 = new JSONObject();
                    array = new JSONArray();
                    for (int j = 0; j < policyList.size(); j++) {
                        if (policyList1.get(i).getForm().equals(policyList.get(j).getForm())&&policyList.get(j).getRank().equals(list2[n])) {
                            num=policyList.get(j).getAllsum();
                        }
                    }
                    String listname=list2[n]+" ("+num+")";
                    q3.put("title", listname);
                    q3.put("id", 0+"");
                    array3.add(q3);

                }
            }

            q1.put("children", array3);

            array2.add(q1);
        }
        List<Tree> myTrees  = new ArrayList<>();
        Gson gson = new Gson();
        JSONObject jcyj = new JSONObject();
        JSONObject kyjd = new JSONObject();
        JSONObject qy = new JSONObject();
        JSONObject cy = new JSONObject();
        JSONObject cxzt = new JSONObject();
        JSONObject t1 = new JSONObject();
        JSONObject t2 = new JSONObject();
        for (int i = 0; i < array2.size(); i++) {
            JSONObject o = (JSONObject) array2.get(i);
            String title = (String) o.get("title");
            if (title==null)continue;
            //array2.remove(o);

            if (title.contains("基础研究")&&(!title.contains("基础研究与科研基地"))){
                jcyj=o;continue;
            }
            if (title.contains("平台基地")&&(!title.contains("基础研究与科研基地"))){
                kyjd=o;continue;
            }
            if (title.contains("企业")&&(!title.contains("企业技术进步与高新技术产业化"))){
                qy=o;continue;
            }
            if (title.contains("产业")&&(!title.contains("企业技术进步与高新技术产业化"))){
                cy=o;continue;
            }
            if (title.contains("创新载体")&&(!title.contains("企业技术进步与高新技术产业化"))){
                cxzt=o;continue;
            }
            if (title.contains("基础研究与科研基地")){
                t1=o;continue;
            }
            if (title.contains("企业技术进步与高新技术产业化")){
                t2=o;
            }
        }
        JSONArray ar = new JSONArray();
        ar.add(jcyj);
        ar.add(kyjd);
        array2.remove(t1);
        t1.put("id","y");
        t1.put("children", ar);
        t1.put("title", "基础研究与科研基地(2753)");
        array2.add(t1);
        JSONArray ar2 = new JSONArray();
        ar2.add(qy);
        ar2.add(cxzt);
        ar2.add(cy);
        array2.remove(t2);
        t2.put("children", ar2);
        t2.put("title", "企业技术进步与高新技术产业化(1046)");
        array2.add(t2);
        array2.remove(kyjd);
        array2.remove(jcyj);
        array2.remove(cxzt);
        array2.remove(cy);
        array2.remove(qy);
//        for (int i = 0; i < array2.size(); i++) {
//            JSONObject o = (JSONObject) array2.get(i);
//            if(o.size()<3){
//                array2.remove(o);break;
//            }
//        }

        System.out.println(array2.toJSONString());

        if (array2 != null){
            return new FebsResponse().success().data(array2);
        }
        return new FebsResponse().message("未找到响应数据，请联系管理员。");
    }

    @GetMapping("getTreeData2")
    public String getMapData2() {
//        Policy policy=new Policy();
//        List<Policy> policyList=policy2Service.getPolicyListGroupFormRank();
//        List<Policy> policyList1=policy2Service.getPolicyListGroupForm();
//        String[] list=new String[] {"综合","科研机构改革","科技计划管理","科技经费与财务","基础研究与科研基地","基础研究","平台基地","企业技术进步与高新技术产业化","企业","产业","创新载体","农村科技与社会发展","科技人才","科技中介服务","科技条件与标准","科技金融与税收","科技成果与知识产权","科学技术普及","科技奖励","国际科技合作"};
//        Integer[] list1=new Integer[] {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26};
//        JSONObject q1 = new JSONObject();
//        JSONObject q2 = new JSONObject();
//        JSONObject q3 = new JSONObject();
//        JSONArray array = new JSONArray();
//        JSONArray array2 = new JSONArray();
//        JSONArray array3 = new JSONArray();
//        for(int i=policyList1.size()-1;i>=0;i--)
//        {
//            q1 = new JSONObject();
//            array3 = new JSONArray();
//            int flag=0;
//            for(int j=0;j<list.length;j++)
//            {
//                if(list[j].equals(policyList1.get(i).getForm()))
//                {
//                    String name=policyList1.get(i).getForm();
//                    q1.put("name", name);
//                    q1.put("id", list1[j]);
//                    flag=1;
//                    if(list[j].equals("基础研究与科研基地")||list[j].equals("企业技术进步与高新技术产业化")){
//                        flag=2;
//                    }
//                }
//            }
//            array2.add(q1);
//        }
//        List<Tree> myTrees  = new ArrayList<>();
//        Gson gson = new Gson();
//        JSONObject jcyj = new JSONObject();
//        JSONObject kyjd = new JSONObject();
//        JSONObject qy = new JSONObject();
//        JSONObject cy = new JSONObject();
//        JSONObject cxzt = new JSONObject();
//        JSONObject t1 = new JSONObject();
//        JSONObject t2 = new JSONObject();
//        for (int i = 0; i < array2.size(); i++) {
//            JSONObject o = (JSONObject) array2.get(i);
//            String title = (String) o.get("name");
//            if (title==null)continue;
//            //array2.remove(o);
//
//            if (title.contains("基础研究")&&(!title.contains("基础研究与科研基地"))){
//                jcyj=o;continue;
//            }
//            if (title.contains("平台基地")&&(!title.contains("基础研究与科研基地"))){
//                kyjd=o;continue;
//            }
//            if (title.contains("企业")&&(!title.contains("企业技术进步与高新技术产业化"))){
//
//                qy=o;
//                System.out.println(qy);
//                continue;
//
//            }
//            if (title.contains("产业")&&(!title.contains("企业技术进步与高新技术产业化"))){
//                cy=o;continue;
//            }
//            if (title.contains("创新载体")&&(!title.contains("企业技术进步与高新技术产业化"))){
//                cxzt=o;continue;
//            }
//            if (title.contains("基础研究与科研基地")){
//                t1=o;continue;
//            }
//            if (title.contains("企业技术进步与高新技术产业化")){
//                t2=o;
//            }
//        }
//        JSONArray ar = new JSONArray();
//        ar.add(jcyj);
//
//        ar.add(kyjd);
//        array2.remove(t1);
//        t1.put("id",99);
//        t1.put("children", ar);
//        t1.put("name", "基础研究与科研基地");
//
//        JSONArray ar2 = new JSONArray();
//        ar2.add(qy);
//        ar2.add(cxzt);
//        ar2.add(cy);
//        System.out.println(ar2.toJSONString());
//        array2.remove(t2);
//        t2.put("children", ar2);
//        t2.put("id", 999);
//        t2.put("name", "企业技术进步与高新技术产业化");
//        array2.add(t2);
//        array2.add(t1);
//        System.out.println(array2.toJSONString());
//        array2.remove(kyjd);
//        array2.remove(jcyj);
//        array2.remove(cxzt);
//        array2.remove(cy);
//        array2.remove(qy);
//        System.out.println("--------------------");
//        System.out.println(array2.toJSONString());
        JSONArray array2 = CreateFileUtil.readJSONArrayFile("F:/test.json");
        System.out.println("-----");
        System.out.println(array2.toJSONString());
        if (array2 != null){
            return array2.toJSONString();
        }
        return "[]";
    }

    @GetMapping("getTreeDataRank")
    public FebsResponse getTreeDataRank() {
        JSONArray array = CreateFileUtil.readJSONArrayFile("G:\\A科技政策系统\\science_technology_policy_integration_service (3)\\json_for_type/RankJsonnew.json");
        //JSONArray array = CreateFileUtil.readJSONArrayFile("newjson/policyRankJSON.json");
        //JSONObject json = CreateFileUtil.readJsonFile("json_for_CreateMapData/xinjian.json");
        if (array != null){
            return new FebsResponse().success().data(array);
        }
        return new FebsResponse().message("未找到响应数据，请联系管理员。");
    }
    @GetMapping("getTreeDataSimilarity")
    public FebsResponse getTreeDataSimilarity() {
        JSONArray array = CreateFileUtil.readJSONArrayFile("G:\\A科技政策系统\\science_technology_policy_integration_service (3)/json_for_helpers/SimilarityJsonnew.json");
        //JSONArray array = CreateFileUtil.readJSONArrayFile("newjson/policySimilarJSON.json");
        //JSONObject json = CreateFileUtil.readJsonFile("json_for_CreateMapData/xinjian.json");
        if (array != null){
            return new FebsResponse().success().data(array);
        }
        return new FebsResponse().message("未找到响应数据，请联系管理员。");
    }

    @GetMapping("typePolicyNum/{id}")
    public FebsResponse typePolicyNum(@PathVariable String id) {
        JSONObject json=new JSONObject();
        if(id.equals("0"))
        {
            JSONObject q1 = new JSONObject();
            JSONObject q2 = new JSONObject();
            JSONArray array = new JSONArray();
            JSONArray array2 = new JSONArray();
            List<Policy> policyList=this.policy2Service.getPolicyListGroupForm();
            for(int i=0;i<policyList.size();i++)
            {
                q1 = new JSONObject();
                q1.put("name",policyList.get(i).getForm());
                q1.put("value",policyList.get(i).getAllsum());
                array.add(q1);
                array2.add(policyList.get(i).getForm());
            }
            json.put("types", array2);
            json.put("dataList", array);
        }
        else
        {
            JSONObject q1 = new JSONObject();
            JSONObject q2 = new JSONObject();
            JSONArray array = new JSONArray();
            JSONArray array2 = new JSONArray();
            String a[]=id.split(" ");
            List<Policy> policyList=this.policy2Service.getPolicyListGroupRank(a[0]);
            for(int i=0;i<policyList.size();i++)
            {
                q1 = new JSONObject();
                q1.put("name",policyList.get(i).getRank());
                q1.put("value",policyList.get(i).getAllsum());
                array.add(q1);
                array2.add(policyList.get(i).getRank());
            }
            json.put("types", array2);
            json.put("dataList", array);
        }
        if (json != null){
            return new FebsResponse().success().data(json);
        }
        return new FebsResponse().message("未找到响应数据，请联系管理员。");
    }

    @GetMapping("getTableData/{id}")
    public FebsResponse getTableData(@PathVariable String id) {
        JSONObject json=new JSONObject();
        if(id.equals("0"))
        {
          // json = CreateFileUtil.readJsonFile("D:/two2\\idea\\code\\science_technology_policy_integration_service\\json_for_type\\allpolicytype.json");
            json = CreateFileUtil.readJsonFile("G:\\A科技政策系统\\science_technology_policy_integration_service (3)/json_for_type/allpolicytype.json");
        }

        if (json != null){
            return new FebsResponse().success().data(json);
        }
        return new FebsResponse().message("未找到响应数据，请联系管理员。");
    }
    @GetMapping("getRankData/{id}")
    public FebsResponse getRankData(@PathVariable int id) {
        JSONObject json=new JSONObject();
        List<PolicyRank> policyRankList=this.policyRankService.getPolicyRankListBystartid(id);
        JSONObject q1 = new JSONObject();
        JSONObject q2 = new JSONObject();
        JSONObject q3 = new JSONObject();
        JSONObject q4 = new JSONObject();
        JSONArray array = new JSONArray();
        JSONArray array2 = new JSONArray();
        JSONArray array3 = new JSONArray();
        JSONArray array4 = new JSONArray();
        JSONArray array5 = new JSONArray();
        JSONArray array6 = new JSONArray();
        array2.add("政策名");
        array2.add("政策级别");
        array2.add("政策序号");

        q2.put("name", "政策名");
        q2.put("type", "bar");
        q2.put("unit_c", "份");
        q3.put("name", "政策序号");
        q3.put("type", "bar");
        q3.put("unit_c", "份");
        q4.put("name", "政策级别");
        q4.put("type", "bar");
        q4.put("unit_c", "份");

        for(int i=policyRankList.size()-1;i>=0;i--)
        {
            if(policyRankList.get(i).getCategory()==0)
            {
                array.add("上位政策");
            }
            else
            {
                array.add("下位政策");
            }
            array3.add(policyRankList.get(i).getEndname());
            array5.add(policyRankList.get(i).getEndid());
            array6.add(policyRankList.get(i).getEndrank());
        }
        array.add("本政策");
        array3.add(policyRankList.get(0).getStartname());
        array5.add(policyRankList.get(0).getStartid());
        array6.add(policyRankList.get(0).getStartrank());

        q2.put("values",array3);
        q3.put("values",array5);
        q4.put("values",array6);
        array4.add(q2);
        array4.add(q4);
        array4.add(q3);
        q1.put("years", array);
        q1.put("names", array2);
        q1.put("data", array4);
        if (q1 != null){
            return new FebsResponse().success().data(q1);
        }
        return new FebsResponse().message("未找到响应数据，请联系管理员。");
    }

    @GetMapping("getSimilarityData/{id}")
    public FebsResponse getSimilarityData(@PathVariable int id) {
        JSONObject json=new JSONObject();
       // List<PolicySimilarity> policyRankList=this.policyRankService.getPolicyRankListBystartid(id);
        List<PolicySimilarity> policyRankList=this.policySimilarityService.getPolicySimilarityListBystartid(id);
        JSONObject q1 = new JSONObject();
        JSONObject q2 = new JSONObject();
        JSONObject q3 = new JSONObject();
        JSONObject q4 = new JSONObject();
        JSONArray array = new JSONArray();
        JSONArray array2 = new JSONArray();
        JSONArray array3 = new JSONArray();
        JSONArray array4 = new JSONArray();
        JSONArray array5 = new JSONArray();
        JSONArray array6 = new JSONArray();
        array2.add("政策名");
        array2.add("政策级别");
        array2.add("政策序号");
        q2.put("name", "政策名");
        q2.put("type", "bar");
        q2.put("unit_c", "份");
        q3.put("name", "政策序号");
        q3.put("type", "bar");
        q3.put("unit_c", "份");
        q4.put("name", "政策级别");
        q4.put("type", "bar");
        q4.put("unit_c", "份");

        for(int i=0;i<policyRankList.size();i++)
        {
            if(policyRankList.get(i).getCategory()==0)
            {
                array.add("相似政策");
            }
            else
            {
                array.add("下位政策");
            }
            array3.add(policyRankList.get(i).getEndname());
            array5.add(policyRankList.get(i).getEndid());
            array6.add(policyRankList.get(i).getEndrank());
        }
        array.add("本政策");
        array3.add(policyRankList.get(0).getStartname());
        array5.add(policyRankList.get(0).getStartid());
        array6.add(policyRankList.get(0).getStartrank());
        q2.put("values",array3);
        q3.put("values",array5);
        q4.put("values",array6);
        array4.add(q2);
        array4.add(q4);
        array4.add(q3);
        q1.put("years", array);
        q1.put("names", array2);
        q1.put("data", array4);
        if (q1 != null){
            return new FebsResponse().success().data(q1);
        }
        return new FebsResponse().message("未找到响应数据，请联系管理员。");
    }

    @GetMapping("getSRelationData/{id}")
    public FebsResponse getSRelationData(@PathVariable int id) {
        List<PolicySimilarity> policyRankList=this.policySimilarityService.getPolicySimilarityListBystartid(id);
        JSONObject json=new JSONObject();
        JSONArray array = new JSONArray();
        JSONArray array2 = new JSONArray();
        JSONArray array3 = new JSONArray();
        JSONObject q1=new JSONObject();
        JSONObject q2=new JSONObject();
        JSONObject q3=new JSONObject();
        String name=policyRankList.get(0).getStartname();
        q1.put("name",name);
        q1.put("category",0);
        array.add(q1);
        for(int i=0;i<policyRankList.size();i++)
        {
            q1=new JSONObject();
            q2=new JSONObject();
            q1.put("name",policyRankList.get(i).getEndname());

            q2.put("source",name);
            q2.put("target",policyRankList.get(i).getEndname());
            if(policyRankList.get(i).getCategory()==0)
            {
                q2.put("name","相似政策");
                q1.put("category",1);
            }
            array.add(q1);
            array2.add(q2);

        }
        q3.put("nodes",array);
        q3.put("links",array2);


        return new FebsResponse().success().data(q3);
    }

    @GetMapping("getRelationData/{id}")
    public FebsResponse getRelationData(@PathVariable int id) {
        List<PolicyRank> policyRankList=this.policyRankService.getPolicyRankListBystartid(id);
        JSONObject json=new JSONObject();
        JSONArray array = new JSONArray();
        JSONArray array2 = new JSONArray();
        JSONArray array3 = new JSONArray();
        JSONObject q1=new JSONObject();
        JSONObject q2=new JSONObject();
        JSONObject q3=new JSONObject();
        String name=policyRankList.get(0).getStartname();
        q1.put("name",name);
        q1.put("category",0);
        array.add(q1);
        for(int i=0;i<policyRankList.size();i++)
        {
            q1=new JSONObject();
            q2=new JSONObject();
            q1.put("name",policyRankList.get(i).getEndname());

            q2.put("source",name);
            q2.put("target",policyRankList.get(i).getEndname());
            if(policyRankList.get(i).getCategory()==0)
            {
                q2.put("name","上位政策");
                q1.put("category",1);
            }
            else
            {
                q2.put("name","下位政策");
                q1.put("category",2);
            }
            array.add(q1);
            array2.add(q2);

        }
        q3.put("nodes",array);
        q3.put("links",array2);
        System.out.println(q3);


        return new FebsResponse().success().data(q3);
    }
    @GetMapping("getTreeName/{policyName}")
    public FebsResponse getTreeName(@PathVariable String policyName) {
        List<PolicyRank> policyList=this.policyRankService.getPolicyRankByName(policyName);
        List<PolicyRank> policyList1=this.policyRankService.getPolicyRankByNameGroup(policyName);
        String[] list=new String[] {"综合","科研机构改革","科技计划管理","科技经费与财务","基础研究与科研基地","企业技术进步与高新技术产业化","农村科技与社会发展","科技人才","科技中介服务","科技条件与标准","科技金融与税收","科技成果与知识产权","科学技术普及","科技奖励","国际科技合作"};
        String[] list1=new String[] {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o"};
        String[] list2=new String[] {"国家级","省级/直辖市","市级"};
        JSONObject q1 = new JSONObject();
        JSONObject q2 = new JSONObject();
        JSONObject q3 = new JSONObject();
        JSONArray array = new JSONArray();
        JSONArray array2 = new JSONArray();
        JSONArray array3 = new JSONArray();
        for(int i=policyList1.size()-1;i>=0;i--)
        {
            q1 = new JSONObject();

            array3 = new JSONArray();
            int flag=0;
            for(int j=0;j<list.length;j++)
            {
                if(list[j].equals(policyList1.get(i).getForm()))
                {
                    String name=policyList1.get(i).getForm()+" "+policyList1.get(i).getAllsum();
                    q1.put("title", name);
                    q1.put("id", list1[j]);
                    flag=1;
                }
            }
            if(flag==1) {
                for(int n=0;n<list2.length;n++)
                {
                    q3 = new JSONObject();
                    array = new JSONArray();
                    q3.put("title", list2[n]);
                    q3.put("id", 0);
                    for (int j = 0; j < policyList.size(); j++) {
                        if (policyList1.get(i).getForm().equals(policyList.get(j).getForm())&&policyList.get(j).getStartrank().equals(list2[n])) {
                            q2 = new JSONObject();
                            q2.put("title", policyList.get(j).getStartname());
                            q2.put("id", policyList.get(j).getStartid());
                            array.add(q2);
                        }
                    }
                    q3.put("children",array);
                    array3.add(q3);

                }
            }
            q1.put("children", array3);
            array2.add(q1);

        }
        return new FebsResponse().success().data(array2);
    }

    @GetMapping("getTreeNameSim/{policyName}")
    public FebsResponse getTreeNameSim(@PathVariable String policyName) {
        List<PolicySimilarity> policyList=this.policySimilarityService.getPolicySimilarityByName(policyName);
        List<PolicySimilarity> policyList1=this.policySimilarityService.getPolicySimilarityByNameGroup(policyName);
        String[] list=new String[] {"综合","科研机构改革","科技计划管理","科技经费与财务","基础研究与科研基地","企业技术进步与高新技术产业化","农村科技与社会发展","科技人才","科技中介服务","科技条件与标准","科技金融与税收","科技成果与知识产权","科学技术普及","科技奖励","国际科技合作"};
        String[] list1=new String[] {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o"};
        String[] list2=new String[] {"国家级","省级/直辖市","市级"};
        JSONObject q1 = new JSONObject();
        JSONObject q2 = new JSONObject();
        JSONObject q3 = new JSONObject();
        JSONArray array = new JSONArray();
        JSONArray array2 = new JSONArray();
        JSONArray array3 = new JSONArray();
        for(int i=policyList1.size()-1;i>=0;i--)
        {
            q1 = new JSONObject();

            array3 = new JSONArray();
            int flag=0;
            for(int j=0;j<list.length;j++)
            {
                if(list[j].equals(policyList1.get(i).getForm()))
                {
                    String name=policyList1.get(i).getForm()+" "+policyList1.get(i).getAllsum();
                    q1.put("title", name);
                    q1.put("id", list1[j]);
                    flag=1;
                }
            }
            if(flag==1) {
                for(int n=0;n<list2.length;n++)
                {
                    q3 = new JSONObject();
                    array = new JSONArray();
                    q3.put("title", list2[n]);
                    q3.put("id", 0);
                    for (int j = 0; j < policyList.size(); j++) {
                        if (policyList1.get(i).getForm().equals(policyList.get(j).getForm())&&policyList.get(j).getStartrank().equals(list2[n])) {
                            q2 = new JSONObject();
                            q2.put("title", policyList.get(j).getStartname());
                            q2.put("id", policyList.get(j).getStartid());
                            array.add(q2);
                        }
                    }
                    q3.put("children",array);
                    array3.add(q3);
                }
            }
            q1.put("children", array3);
            array2.add(q1);

        }
        return new FebsResponse().success().data(array2);
    }


    @GetMapping("getName/{policyName}")
    public FebsResponse getMap(@PathVariable String policyName) {
        List<Policy> policyList=this.policy2Service.getPolicyListByName(policyName);
        List<Policy> policyList1=this.policy2Service.getPolicyListByNameAll(policyName);
        String[] list=new String[] {"综合","科研机构改革","科技计划管理","科技经费与财务","基础研究与科研基地","企业技术进步与高新技术产业化","农村科技与社会发展","科技人才","科技中介服务","科技条件与标准","科技金融与税收","科技成果与知识产权","科学技术普及","科技奖励","国际科技合作"};
        String[] list1=new String[] {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o"};
        String[] list2=new String[] {"国家级","省级/直辖市","市级"};
        JSONObject q1 = new JSONObject();
        JSONObject q2 = new JSONObject();
        JSONObject q3 = new JSONObject();
        JSONArray array = new JSONArray();
        JSONArray array2 = new JSONArray();
        JSONArray array3 = new JSONArray();
        for(int i=policyList1.size()-1;i>=0;i--)
        {
            q1 = new JSONObject();
            //array = new JSONArray();
            array3 = new JSONArray();
            int flag=0;
            for(int j=0;j<list.length;j++)
            {
                if(list[j].equals(policyList1.get(i).getForm()))
                {
                    String name=policyList1.get(i).getForm()+" ("+policyList1.get(i).getAllsum()+")";
                    q1.put("title", name);
                    q1.put("id", list1[j]);
                    flag=1;
                }
            }
            if(flag==1) {
                for(int n=0;n<list2.length;n++)
                {
                    int num=0;
                    q3 = new JSONObject();
                    array = new JSONArray();
                    for (int j = 0; j < policyList.size(); j++) {
                        if (policyList1.get(i).getForm().equals(policyList.get(j).getForm())&&policyList.get(j).getRank().equals(list2[n])) {
//                            q2 = new JSONObject();
//                            q2.put("title", policyList.get(j).getName());
//                            q2.put("id", policyList.get(j).getId());
//                            array.add(q2);
                            num+=1;
                        }
                    }
                    String listname=list2[n]+" ("+num+")";
                    q3.put("title", listname);
                    q3.put("id", 0);

                    //q3.put("children",array);
                    array3.add(q3);

                }
//                for (int j = 0; j < policyList.size(); j++) {
//                    if (policyList1.get(i).getForm().equals(policyList.get(j).getForm())) {
//                        q2 = new JSONObject();
//                        q2.put("title", policyList.get(j).getName());
//                        q2.put("id", policyList.get(j).getId());
//                        array.add(q2);
//                    }
//                }
            }
            q1.put("children", array3);
            array2.add(q1);

        }
        return new FebsResponse().success().data(array2);
    }
    @GetMapping("getJsonQuestion/{policyName}")
    public FebsResponse getJsonQuestion(@PathVariable String policyName, QueryRequest request) {
        List<Term> termList=biaozhun(policyName);
        System.out.println(termList);
        List<Theme> themes=this.themeService.getThemeList();

        List<String> organ=new ArrayList<String>();
        List<String> range=new ArrayList<String>();
        List<String> noun=new ArrayList<String>();
        List<String> theme=new ArrayList<String>();
        List<String> year=new ArrayList<String>();
        List<String> name=new ArrayList<String>();
        List<String> fanwei=new ArrayList<String>();
        for(int i=0;i<termList.size();i++)
        {
            String term=termList.get(i).toString();
            String termsplit[]=term.split("/");
            if(termsplit.length==2)
            {
                if(termsplit[1].equals("nt")||termsplit[0].equals("科技部"))
                {
                    organ.add(termsplit[0]);
                }
                else if(termsplit[1].equals("ns"))
                {
                    range.add(termsplit[0]);
                }
                else if(termsplit[1].equals("f")||termsplit[1].equals("p")||termsplit[1].equals("v"))
                {
                    fanwei.add(termsplit[0]);
                }
                else if(termsplit[1].equals("m"))
                {

                    year.add(termsplit[0]);
                }
                else if(termsplit[1].equals("nz"))
                {

                    name.add(termsplit[0]);
                }
                else if(termsplit[1].equals("n")||termsplit[1].equals("r")||termsplit[1].equals("ry")||termsplit[1].equals("vn"))
                {
                    noun.add(termsplit[0]);
                }
                for(int j=0;j<42;j++)
                {
                    String a[]=themes.get(j).getThirdname().split(" ");
                    for(int m=0;m<a.length;m++)
                    {
                        if(a[m].equals(termsplit[0])&&!termsplit[0].equals("政策"))
                        {
                            theme.add(termsplit[0]);
                        }
                    }
//                    if(themes.get(j).getThirdname().indexOf(termsplit[0])!=-1&&!termsplit[0].equals("政策"))
//                    {
//                        theme.add(termsplit[0]);
//                    }
                }
            }

        }
        ClassQuestionKeywords classQuestionKeywords=new ClassQuestionKeywords();
        int flag=classQuestionKeywords.ClassJudge(noun);
        System.out.println(flag);
        JSONObject q1 = new JSONObject();
        q1= createQuestion(flag,organ,range,theme,year,name,fanwei,request);
        q1.put("flag",flag);
        return new FebsResponse().success().data(q1);
    }
    @GetMapping("getJsonQuestiontable/{policyName}")
    public FebsResponse getJsonQuestiontable(@PathVariable String policyName, QueryRequest request) {
        List<Term> termList=biaozhun(policyName);
        List<Theme> themes=this.themeService.getThemeList();

        List<String> organ=new ArrayList<String>();
        List<String> range=new ArrayList<String>();
        List<String> noun=new ArrayList<String>();
        List<String> theme=new ArrayList<String>();
        List<String> year=new ArrayList<String>();
        List<String> name=new ArrayList<String>();
        List<String> fanwei=new ArrayList<String>();
        for(int i=0;i<termList.size();i++)
        {
            String term=termList.get(i).toString();
            String termsplit[]=term.split("/");
            if(termsplit.length==2)
            {
                if(termsplit[1].equals("nt")||termsplit[0].equals("科技部"))
                {
                    organ.add(termsplit[0]);
                }
                else if(termsplit[1].equals("ns"))
                {
                    range.add(termsplit[0]);
                }
                else if(termsplit[1].equals("f")||termsplit[1].equals("p")||termsplit[1].equals("v"))
                {
                    fanwei.add(termsplit[0]);
                }
                else if(termsplit[1].equals("m"))
                {

                    year.add(termsplit[0]);
                }
                else if(termsplit[1].equals("nz"))
                {

                    name.add(termsplit[0]);
                }
                else if(termsplit[1].equals("n")||termsplit[1].equals("r")||termsplit[1].equals("ry")||termsplit[1].equals("vn"))
                {
                    noun.add(termsplit[0]);
                }
                for(int j=0;j<42;j++)
                {
                    String a[]=themes.get(j).getThirdname().split(" ");
                    for(int m=0;m<a.length;m++)
                    {
                        if(a[m].equals(termsplit[0])&&!termsplit[0].equals("政策"))
                        {
                            theme.add(termsplit[0]);
                        }
                    }
//                    if(themes.get(j).getThirdname().indexOf(termsplit[0])!=-1&&!termsplit[0].equals("政策"))
//                    {
//                        theme.add(termsplit[0]);
//                    }
                }
            }

        }
        ClassQuestionKeywords classQuestionKeywords=new ClassQuestionKeywords();
        int flag=classQuestionKeywords.ClassJudge(noun);
        JSONObject q1 = new JSONObject();
        Map<String, Object> dataTable= createQuestion2(flag,organ,range,theme,year,name,fanwei,request);
        return new FebsResponse().success().data(dataTable);
    }
    public  JSONObject createQuestion(int flag,List<String> organ,List<String> range,List<String> theme,List<String> year,List<String> name,List<String> fanwei, QueryRequest request)
    {
        JSONObject q1 = new JSONObject();
        String result="";
        if(organ.size()==0)
        {
            organ=null;
        }
        if(range.size()==0)
        {
            range=null;
        }
        if(theme.size()==0)
        {
            theme=null;
        }
        if(year.size()==0)
        {
            year=null;
        }
        if(name.size()==0)
        {
            name=null;
        }
        if(fanwei.size()==0)
        {
            fanwei=null;
        }
        if(flag==1)
        {
            if(fanwei.size()>0)
            {
                for(int i=0;i<fanwei.size();i++)
                {
                    if(fanwei.get(i).equals("之前")||fanwei.get(i).equals("以前"))
                    {
                        List<Policy> policyList = policy2Service.getQuestionNumByPolicy2(organ, range, theme, year, name,fanwei);
                        if (policyList.size() > 0) {
                            result = "一共有" + policyList.size() + "条政策";
                        } else {
                            result = "无";
                        }
                    }
                    if(fanwei.get(i).equals("之后")||fanwei.get(i).equals("以后"))
                    {
                        List<Policy> policyList = policy2Service.getQuestionNumByPolicy3(organ, range, theme, year, name,fanwei);
                        if (policyList.size() > 0) {
                            result = "一共有" + policyList.size() + "条政策";
                        } else {
                            result = "无";
                        }
                    }
                    if(fanwei.get(i).equals("至")||fanwei.get(i).equals("到"))
                    {
                        String startyear=year.get(0);
                        String endyear=year.get(1);
                        List<Policy> policyList = policy2Service.getQuestionNumByPolicy4(organ, range, theme,name,startyear,endyear);
                        if (policyList.size() > 0) {
                            result = "一共有" + policyList.size() + "条政策";
                        } else {
                            result = "无";
                        }
                    }
                }
            }
            else {
                List<Policy> policyList = policy2Service.getQuestionNumByPolicy(organ, range, theme, year, name);

                if (policyList.size() > 0) {
                    result = "一共有" + policyList.size() + "条政策";
                } else {
                    result = "无";
                }
            }

        }
        if(flag==2)
        {

            List<Policy> policyList=policy2Service.getQuestionNumByPolicy(organ,range,theme,year,name);
            if(policyList.size()>0) {
                result = "发布时间为：" + policyList.get(0).getYear();
                q1.put("name", policyList.get(0).getName());
                q1.put("dept", policyList.get(0).getOrgan());
                q1.put("year", policyList.get(0).getYear());
                q1.put("theme", policyList.get(0).getTheme());
                q1.put("keyword", policyList.get(0).getKeyword());
                q1.put("range", policyList.get(0).getRange());
                q1.put("id",policyList.get(0).getId());
                String text=policyList.get(0).getText();
                String subText=text.substring(0,200);
                q1.put("text",subText);

            }
            else
            {
                result = "无";
            }

        }
        if(flag==3)
        {
            List<Policy> policyList=policy2Service.getQuestionNumByPolicy(organ,range,theme,year,name);
            if(policyList.size()>0) {
                result = "发布部门为：" + policyList.get(0).getOrgan();
                q1.put("name", policyList.get(0).getName());
                q1.put("dept", policyList.get(0).getOrgan());
                q1.put("year", policyList.get(0).getYear());
                q1.put("theme", policyList.get(0).getTheme());
                q1.put("keyword", policyList.get(0).getKeyword());
                q1.put("range", policyList.get(0).getRange());
                q1.put("id",policyList.get(0).getId());
                String text=policyList.get(0).getText();
                String subText=text.substring(0,200);
                q1.put("text",subText);

            }
            else
            {
                result = "无";
            }

        }
        if(flag==4)
        {
            List<PolicyContrast> policycontrastList=policyContrastService.getQuestionNumPolicyContrast(organ,range,theme,year,name);
            if(policycontrastList.size()>0) {
                result = "已给出答案";
                String policymain=policycontrastList.get(0).getPolicymain();
                q1.put("text",policymain);
                if(policycontrastList.size()>1) {
                    String policymain1=policycontrastList.get(1).getPolicymain();
                    q1.put("text1",policymain1);
                }
                if(policycontrastList.size()>2) {
                    String policymain2=policycontrastList.get(2).getPolicymain();
                    q1.put("text2",policymain2);
                }
                if(policycontrastList.size()>3) {
                    String policymain3=policycontrastList.get(3).getPolicymain();
                    q1.put("text3",policymain3);
                }


            }
            else
            {
                result = "未查到相应数据";
            }
        }
        if(flag==-1)
        {
            result = "无";
        }
        q1.put("answer",result);
        return q1;
    }
    public  Map<String, Object> createQuestion2(int flag,List<String> organ,List<String> range,List<String> theme, List<String> year,List<String> name,List<String> fanwei,QueryRequest request)
    {
        Map<String, Object> dataTable=null;
        if(flag==1)
        {

            if(organ.size()==0)
            {
                organ=null;
            }
            if(range.size()==0)
            {
                range=null;
            }
            if(theme.size()==0)
            {
                theme=null;
            }
            if(year.size()==0)
            {
                year=null;
            }
            if(name.size()==0)
            {
                name=null;
            }
            if(fanwei.size()>0)
            {
                for(int i=0;i<fanwei.size();i++)
                {
                    if(fanwei.get(i).equals("之前")||fanwei.get(i).equals("以前"))
                    {
                        List<Policy> policyList = policy2Service.getQuestionNumByPolicy2(organ, range, theme, year, name,fanwei);
                        if(policyList.size()>0) {
                            dataTable = getDataTable(this.policy2Service.findPolicyDetail3(organ,range,theme,year,name, request));
                        }
                    }
                    if(fanwei.get(i).equals("之后")||fanwei.get(i).equals("以后"))
                    {
                        List<Policy> policyList = policy2Service.getQuestionNumByPolicy3(organ, range, theme, year, name,fanwei);
                        if(policyList.size()>0) {
                            dataTable = getDataTable(this.policy2Service.findPolicyDetail4(organ,range,theme,year,name, request));
                        }
                    }
                    if(fanwei.get(i).equals("至")||fanwei.get(i).equals("到"))
                    {
                        String startyear=year.get(0);
                        String endyear=year.get(1);
                        List<Policy> policyList = policy2Service.getQuestionNumByPolicy4(organ, range, theme,name,startyear,endyear);
                        if(policyList.size()>0) {
                            dataTable = getDataTable(this.policy2Service.findPolicyDetail5(organ,range,theme,name,startyear,endyear, request));
                        }
                    }
                }
            }
            else {
                List<Policy> policyList = policy2Service.getQuestionNumByPolicy(organ, range, theme, year, name);
                if(policyList.size()>0) {
                    dataTable = getDataTable(this.policy2Service.findPolicyDetail2(organ,range,theme,year,name, request));
                }

            }
        }
        if(flag==-1)
        {
            dataTable=null;
        }
        return dataTable;
    }
    public static List<Term> biaozhun(String text)
    {
        List<Term> termList = StandardTokenizer.segment(text);
        return termList;
    }
}
