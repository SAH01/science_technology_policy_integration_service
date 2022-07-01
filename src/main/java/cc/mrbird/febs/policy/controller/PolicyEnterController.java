package cc.mrbird.febs.policy.controller;

import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.policy.entity.Policy;
import cc.mrbird.febs.policy.entity.PolicyCrawling;
import cc.mrbird.febs.policy.entity.PolicyEnter;
import cc.mrbird.febs.policy.service.impl.Policy2ServiceImpl;
import cc.mrbird.febs.policy.service.impl.PolicyCrawlingServiceImpI;
import cc.mrbird.febs.policy.service.impl.PolicyEnterServiceImpl;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("Enter")
public class PolicyEnterController extends BaseController {
    @Autowired
    private Policy2ServiceImpl policy2Service;
    @Autowired
    private PolicyCrawlingServiceImpI policyCrawlingService;
    @Autowired
    private PolicyEnterServiceImpl policyEnterService;

    private String temp = "0";
    @GetMapping("list")
    @RequiresPermissions("user:view")
    public FebsResponse policyList(PolicyEnter policy, QueryRequest request) {
        Map<String, Object> dataTable = getDataTable(this.policyEnterService.findPolicyCrawlingDetail(policy, request));
        temp="0";
        return new FebsResponse().success().data(dataTable);
    }

    @RequestMapping("add")
    public ModelAndView addPolicy(PolicyEnter policy, QueryRequest request){
        policyEnterService.createPolicyCrawling(policy);
        System.out.println("\n\n后台即将插入数据库的正文内容： \n "+policy.getText()+"\n\n");
        temp = "录入成功";
        return new ModelAndView("redirect:/index#/policy/policyCrawling");
    }

    @RequestMapping("update")
    public ModelAndView updatePolicy(PolicyEnter policy, QueryRequest request){
        policyEnterService.updatePolicyCrawling(policy);
        temp = "修改成功";
        return new ModelAndView("redirect:/index#/policy/policyCrawling");
    }

    @RequestMapping("deleteP")
    public FebsResponse deleteP(int id){
        JSONObject q2 = new JSONObject();
        policyEnterService.deletePolicyCrawling(id);
        q2.put("information","删除成功！");
        return new FebsResponse().success().data(q2);
        //return new ModelAndView("redirect:/index#/policy/policyCrawling");
    }

    @RequestMapping("getFlag")
    public FebsResponse getFlag(){
        JSONObject q2 = new JSONObject();
        q2.put("information",temp);
        return new FebsResponse().success().data(q2);
    }

    @RequestMapping("findById")
    public FebsResponse findById(int id){
        System.out.println("--------- findById -----------");
        PolicyEnter byId = policyEnterService.findById(id);
        System.out.println(byId.toString());
        return new FebsResponse().success().data(byId);
    }

    @RequestMapping("selectMeunTree")
    public String selectMeunTree(){
        JSONObject q = new JSONObject();
        JSONObject q1 = new JSONObject();
        JSONArray ca = new JSONArray();
        JSONArray arr = new JSONArray();

        q1.put("id",11);
        q1.put("name", "权限管理1");
        q1.put("open", false);
        q1.put("children", new JSONArray());
        ca.add(q1);
        q.put("id",10);
        q.put("name", "权限管理");
        q.put("open", false);
        q.put("children", ca);
        arr.add(q);

        return arr.toJSONString();
    }

    @GetMapping("intoPolicy")
    public FebsResponse intoPolicy() throws ParseException {
        JSONObject q2 = new JSONObject();
        List<PolicyCrawling> policyCrawlingList=policyCrawlingService.getPolicyCrawlingList();
        if(policyCrawlingList.size()==0)
        {
            q2.put("information","请爬取政策！");
        }
        else
        {
            for(int i=0;i<policyCrawlingList.size();i++)
            {
                PolicyCrawling policyCrawling=policyCrawlingList.get(i);
                Policy policy=new Policy();
                policy.setRank(policyCrawling.getRank1());
                policy.setForm(policyCrawling.getForm());
                policy.setText(policyCrawling.getText());
                policy.setDocument(policyCrawling.getDocument());
                policy.setYear(policyCrawling.getYear());
                policy.setName(policyCrawling.getName());
                try {
                    policy2Service.createPolicy(policy);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            for(int i=0;i<policyCrawlingList.size();i++)
            {
                int id=policyCrawlingList.get(i).getId();
                try {
                    policyCrawlingService.deletePolicyCrawling(id);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            q2.put("information","成功入库！");
        }
        System.out.println(q2);
        return new FebsResponse().success().data(q2);
    }
    @GetMapping("getPolicy")
    public FebsResponse getPolicy() throws ParseException {
        JSONObject q2 = new JSONObject();
        List<Policy> policyList=new ArrayList<Policy>();
        policyList=shaixuan();
        int num=policyList.size();
        q2.put("information","成功获取"+num+"条新政策！");
        for(int i=0;i<policyList.size();i++)
        {
            Policy policy=new Policy();
            policy=policyList.get(i);
            PolicyCrawling policyCrawling=new PolicyCrawling();
            policyCrawling.setDocument(policy.getDocument());
            policyCrawling.setForm(policy.getForm());
            policyCrawling.setName(policy.getName());
            policyCrawling.setOrgan(policy.getOrgan());
            policyCrawling.setYear(policy.getYear());
            policyCrawling.setText(policy.getText());
            policyCrawling.setRank1(policy.getRank());
            try {
                policyCrawlingService.createPolicyCrawling(policyCrawling);
                //policy2Service.createPolicy(policy);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new FebsResponse().success().data(q2);
    }
    public List<Policy> shaixuan()
    {
        List<Policy> policies=policy2Service.getPolicyList();
        List<Policy> policyList=getaddress();

        System.out.println(policyList.size());
        List<Policy> policyList1=new ArrayList<Policy>();
        for(int i=0;i<policyList.size();i++)
        {
            String name=policyList.get(i).getName();
            if(name!=null&&!name.equals("")) {
                int flag = 0;
                for (int j = 0; j < policies.size(); j++) {
                    String oldname = policies.get(j).getName();
                    if(oldname!=null&&!oldname.equals("")) {
                        if (name.equals(oldname)) {
                            flag = 1;
                        }
                    }
                }
                if (flag == 0) {
                    policyList1.add(policyList.get(i));
                }
            }
        }
        return policyList1;
    }
    public static List<Policy> getaddress()
    {
        List<Policy> policyList=new ArrayList<Policy>();
        for(int i=0;i<2;i++)
        {

            int num=i+1;
            String abc="";
            if(i==0)
            {
                String dizhi="http://www.most.gov.cn/xxgk/xinxifenlei/fdzdgknr/fgzc/gfxwj/index.html";
                abc=dizhi;
            }
            else
            {
                String dizhi="http://www.most.gov.cn/xxgk/xinxifenlei/fdzdgknr/fgzc/gfxwj/index_"+num+".html";
                abc=dizhi;
            }
            try {
                Document document=Jsoup.connect(abc).timeout(100000).get();
                Element alist=document.getElementById("data_list");
                Elements results=alist.select("a");
                for(int j=0;j<results.size();j++)
                {
                    String href=results.get(j).attr("href");
                    Policy policy=new Policy();
                    if(href.indexOf("gfxwj")!=-1&&href.indexOf("pdf")==-1)
                    {
                        String result=href.substring(1);
                        String newhref="http://www.most.gov.cn/xxgk/xinxifenlei/fdzdgknr/fgzc/gfxwj"+result;
                        policy=getPolicy(newhref);
                        policyList.add(policy);
                    }
                }


            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return policyList;
    }
    public static String setForm(String name)
    {
        String[] formlist=new String[]{"综合","科研机构改革","科技金融与税收","科技计划管理","科技经费与财务","科技条件与标准","科技成果与知识产权","科技奖励","科技人才","科技中介服务","科学技术普及","基础研究与科研基地","国际科技合作","农村科技与社会发展","企业技术进步与高新技术产业化"};
        String[] formlist2=new String[]{"综合","科研 机构 改革","金融 税收","计划 管理","经费 财务","条件 标准","科技成果 知识产权","奖励","人才","中介 服务","普及","基础研究 基地","国际 合作","农村 社会","企业 进步 高新"};
        String form="";
        for(int i=0;i<formlist.length;i++)
        {
            String a[]=formlist2[i].split(" ");
            for(int j=0;j<a.length;j++)
            {
                if(name.indexOf(a[j])!=-1)
                {
                    form=formlist[i];
                }
            }
        }
        if(form.equals(""))
        {
            form="综合";
        }
        return form;
    }
    public static String setRank(String name)
    {
        String rank="";
        String[] list2=new String[] {"国家政策","河北省市","其他省市"};
        String[] province=new String[] {"北京","天津","上海","广东","四川","辽宁","吉林","黑龙江","江苏","浙江","安徽","福建","江西","山东","河南","湖北","湖南","山西","广西","海南","重庆","内蒙古","贵州","云南"
                ,"西藏","陕西","甘肃","青海","宁夏","新疆","台湾","香港","澳门"};
        String[] shi=new String[] {"河北","石家庄","唐山","秦皇岛","邯郸","邢台","保定","张家口","承德","沧州","廊坊","衡水","县"};
        int flag=0;
        for(int m=0;m<shi.length;m++)
        {
            if(name.indexOf(shi[m])!=-1)
            {
                flag=1;
            }
        }
        for(int m=0;m<province.length;m++)
        {
            if(name.indexOf(province[m])!=-1)
            {
                flag=2;
            }
        }
        if(flag==0)
        {
            rank="国家政策";
        }
        if(flag==1)
        {
            rank="河北省市";
        }
        if(flag==2)
        {
            rank="其他省市";
        }
        return rank;
    }
    public static Policy getPolicy(String address) throws IOException
    {
        Policy policy=new Policy();
        try {
            Document document= Jsoup.connect(address).timeout(10000).get();
            Elements alist=document.getElementsByClass("xxgk_detail_head");
            Element zhengwen=document.getElementById("Zoom");
            String text=zhengwen.toString();
            text=text.replace(".h1 {\n" +
                    "\tPAGE-BREAK-AFTER: avoid; TEXT-JUSTIFY: inter-ideograph; TEXT-ALIGN: justify; LINE-HEIGHT: 240%; MARGIN: 17pt 0cm 16.5pt; FONT-SIZE: 22pt; FONT-WEIGHT: bold\n" +
                    "}\n" +
                    ".h2 {\n" +
                    "\tPAGE-BREAK-AFTER: avoid; TEXT-JUSTIFY: inter-ideograph; TEXT-ALIGN: justify; LINE-HEIGHT: 173%; MARGIN: 13pt 0cm; FONT-SIZE: 16pt; FONT-WEIGHT: bold\n" +
                    "}\n" +
                    ".h3 {\n" +
                    "\tPAGE-BREAK-AFTER: avoid; TEXT-JUSTIFY: inter-ideograph; TEXT-ALIGN: justify; LINE-HEIGHT: 173%; MARGIN: 13pt 0cm; FONT-SIZE: 16pt; FONT-WEIGHT: bold\n" +
                    "}\n" +
                    "DIV.union {\n" +
                    "\tLINE-HEIGHT: 18px; FONT-SIZE: 14px\n" +
                    "}\n" +
                    "DIV.union TD {\n" +
                    "\tLINE-HEIGHT: 18px; FONT-SIZE: 14px\n" +
                    "}\n" +
                    ".h1 {\n" +
                    "\tPAGE-BREAK-AFTER: avoid; TEXT-JUSTIFY: inter-ideograph; TEXT-ALIGN: justify; LINE-HEIGHT: 240%; MARGIN: 17pt 0cm 16.5pt; FONT-SIZE: 22pt; FONT-WEIGHT: bold\n" +
                    "}\n" +
                    ".h2 {\n" +
                    "\tPAGE-BREAK-AFTER: avoid; TEXT-JUSTIFY: inter-ideograph; TEXT-ALIGN: justify; LINE-HEIGHT: 173%; MARGIN: 13pt 0cm; FONT-SIZE: 16pt; FONT-WEIGHT: bold\n" +
                    "}\n" +
                    ".h3 {\n" +
                    "\tPAGE-BREAK-AFTER: avoid; TEXT-JUSTIFY: inter-ideograph; TEXT-ALIGN: justify; LINE-HEIGHT: 173%; MARGIN: 13pt 0cm; FONT-SIZE: 16pt; FONT-WEIGHT: bold\n" +
                    "}\n" +
                    ".union {\n" +
                    "\tLINE-HEIGHT: 18px; FONT-SIZE: 14px\n" +
                    "}\n" +
                    ".union TD {\n" +
                    "\tLINE-HEIGHT: 18px; FONT-SIZE: 14px\n" +
                    "}","");
            text=text.replace(".h1 {\n" +
                    "\tPAGE-BREAK-AFTER: avoid; TEXT-JUSTIFY: inter-ideograph; TEXT-ALIGN: justify; LINE-HEIGHT: 240%; MARGIN: 17pt 0cm 16.5pt; FONT-SIZE: 22pt; FONT-WEIGHT: bold\n" +
                    "}\n" +
                    ".h2 {\n" +
                    "\tPAGE-BREAK-AFTER: avoid; TEXT-JUSTIFY: inter-ideograph; TEXT-ALIGN: justify; LINE-HEIGHT: 173%; MARGIN: 13pt 0cm; FONT-SIZE: 16pt; FONT-WEIGHT: bold\n" +
                    "}\n" +
                    ".h3 {\n" +
                    "\tPAGE-BREAK-AFTER: avoid; TEXT-JUSTIFY: inter-ideograph; TEXT-ALIGN: justify; LINE-HEIGHT: 173%; MARGIN: 13pt 0cm; FONT-SIZE: 16pt; FONT-WEIGHT: bold\n" +
                    "}\n" +
                    "DIV.union {\n" +
                    "\tLINE-HEIGHT: 18px; FONT-SIZE: 14px\n" +
                    "}\n" +
                    "DIV.union TD {\n" +
                    "\tLINE-HEIGHT: 18px; FONT-SIZE: 14px\n" +
                    "}\n" +
                    ".h1 {\n" +
                    "\tPAGE-BREAK-AFTER: avoid; TEXT-JUSTIFY: inter-ideograph; TEXT-ALIGN: justify; LINE-HEIGHT: 240%; MARGIN: 17pt 0cm 16.5pt; FONT-SIZE: 22pt; FONT-WEIGHT: bold\n" +
                    "}\n" +
                    ".h2 {\n" +
                    "\tPAGE-BREAK-AFTER: avoid; TEXT-JUSTIFY: inter-ideograph; TEXT-ALIGN: justify; LINE-HEIGHT: 173%; MARGIN: 13pt 0cm; FONT-SIZE: 16pt; FONT-WEIGHT: bold\n" +
                    "}\n" +
                    ".h3 {\n" +
                    "\tPAGE-BREAK-AFTER: avoid; TEXT-JUSTIFY: inter-ideograph; TEXT-ALIGN: justify; LINE-HEIGHT: 173%; MARGIN: 13pt 0cm; FONT-SIZE: 16pt; FONT-WEIGHT: bold\n" +
                    "}\n" +
                    ".union {\n" +
                    "\tLINE-HEIGHT: 18px; FONT-SIZE: 14px\n" +
                    "}\n" +
                    ".union TD {\n" +
                    "\tLINE-HEIGHT: 18px; FONT-SIZE: 14px\n" +
                    "}","");
            text=text.replace(".h1 {\n" +
                    "\tPAGE-BREAK-AFTER: avoid; TEXT-JUSTIFY: inter-ideograph; TEXT-ALIGN: justify; LINE-HEIGHT: 240%; MARGIN: 17pt 0cm 16.5pt; FONT-SIZE: 22pt; FONT-WEIGHT: bold\n" +
                    "}\n" +
                    ".h2 {\n" +
                    "\tPAGE-BREAK-AFTER: avoid; TEXT-JUSTIFY: inter-ideograph; TEXT-ALIGN: justify; LINE-HEIGHT: 173%; MARGIN: 13pt 0cm; FONT-SIZE: 16pt; FONT-WEIGHT: bold\n" +
                    "}\n" +
                    ".h3 {\n" +
                    "\tPAGE-BREAK-AFTER: avoid; TEXT-JUSTIFY: inter-ideograph; TEXT-ALIGN: justify; LINE-HEIGHT: 173%; MARGIN: 13pt 0cm; FONT-SIZE: 16pt; FONT-WEIGHT: bold\n" +
                    "}\n" +
                    "DIV.union {\n" +
                    "\tLINE-HEIGHT: 18px; FONT-SIZE: 14px\n" +
                    "}\n" +
                    "DIV.union TD {\n" +
                    "\tLINE-HEIGHT: 18px; FONT-SIZE: 14px\n" +
                    "}","");
            Elements results=alist.select("td");
            String name="";
            String dept="";
            String pubdata="";
            String wenhao="";
            for(int i=0;i<results.size();i++)
            {
                if(results.size()>i+1)
                {
                    if(results.get(i).text().indexOf("标　　题")!=-1)
                    {
                        name=results.get(i+1).text();
                    }
                    else if(results.get(i).text().indexOf("发文机构")!=-1)
                    {
                        dept=results.get(i+1).text();
                    }
                    else if(results.get(i).text().indexOf("成文日期")!=-1)
                    {
                        pubdata=results.get(i+1).text();
                        pubdata=pubdata.replace("年", "-");
                        pubdata=pubdata.replace("月", "-");
                        pubdata=pubdata.replace("日", "");
                    }
                    else if(results.get(i).text().indexOf("发文字号")!=-1)
                    {

                        wenhao=results.get(i+1).text();
                    }
                }

            }
            String form=setForm(name);
            String rank=setRank(name);
            policy.setForm(form);
            policy.setRank(rank);
            policy.setName(name);
            policy.setOrgan(dept);
            policy.setYear(pubdata);
            policy.setDocument(wenhao);
            policy.setText(text);
            return policy;

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return policy;
    }
}
