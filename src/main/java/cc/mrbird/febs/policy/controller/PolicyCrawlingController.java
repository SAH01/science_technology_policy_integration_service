package cc.mrbird.febs.policy.controller;

import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.policy.entity.Policy;
import cc.mrbird.febs.policy.entity.PolicyCrawling;
import cc.mrbird.febs.policy.service.impl.Policy2ServiceImpl;
import cc.mrbird.febs.policy.service.impl.PolicyCrawlingServiceImpI;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("Crawling")
public class PolicyCrawlingController extends BaseController {
    @Autowired
    private Policy2ServiceImpl policy2Service;
    @Autowired
    private PolicyCrawlingServiceImpI policyCrawlingService;
    @GetMapping("list")
    @RequiresPermissions("user:view")
    public FebsResponse policyList(PolicyCrawling policy, QueryRequest request) {
        Map<String, Object> dataTable = getDataTable(this.policyCrawlingService.findPolicyCrawlingDetail(policy, request));
        return new FebsResponse().success().data(dataTable);
    }
    @GetMapping("intoPolicy")
    public FebsResponse intoPolicy() throws ParseException {
        JSONObject q2 = new JSONObject();
        List<PolicyCrawling> policyCrawlingList=policyCrawlingService.getPolicyCrawlingList();
        if(policyCrawlingList.size()==0)
        {
            q2.put("information","??????????????????");
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
            q2.put("information","???????????????");
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
        q2.put("information","????????????"+num+"???????????????");
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
        String[] formlist=new String[]{"??????","??????????????????","?????????????????????","??????????????????","?????????????????????","?????????????????????","???????????????????????????","????????????","????????????","??????????????????","??????????????????","???????????????????????????","??????????????????","???????????????????????????","??????????????????????????????????????????"};
        String[] formlist2=new String[]{"??????","?????? ?????? ??????","?????? ??????","?????? ??????","?????? ??????","?????? ??????","???????????? ????????????","??????","??????","?????? ??????","??????","???????????? ??????","?????? ??????","?????? ??????","?????? ?????? ??????"};
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
            form="??????";
        }
        return form;
    }
    public static String setRank(String name)
    {
        String rank="";
        String[] list2=new String[] {"????????????","????????????","????????????"};
        String[] province=new String[] {"??????","??????","??????","??????","??????","??????","??????","?????????","??????","??????","??????","??????","??????","??????","??????","??????","??????","??????","??????","??????","??????","?????????","??????","??????"
                ,"??????","??????","??????","??????","??????","??????","??????","??????","??????"};
        String[] shi=new String[] {"??????","?????????","??????","?????????","??????","??????","??????","?????????","??????","??????","??????","??????","???"};
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
            rank="????????????";
        }
        if(flag==1)
        {
            rank="????????????";
        }
        if(flag==2)
        {
            rank="????????????";
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
                    if(results.get(i).text().indexOf("????????????")!=-1)
                    {
                        name=results.get(i+1).text();
                    }
                    else if(results.get(i).text().indexOf("????????????")!=-1)
                    {
                        dept=results.get(i+1).text();
                    }
                    else if(results.get(i).text().indexOf("????????????")!=-1)
                    {
                        pubdata=results.get(i+1).text();
                        pubdata=pubdata.replace("???", "-");
                        pubdata=pubdata.replace("???", "-");
                        pubdata=pubdata.replace("???", "");
                    }
                    else if(results.get(i).text().indexOf("????????????")!=-1)
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
