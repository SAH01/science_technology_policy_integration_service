package cc.mrbird.febs.policy.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.entity.DeptTree;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.KindTree;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.policy.entity.Policy;
import cc.mrbird.febs.policy.entity.PolicyContrast;
import cc.mrbird.febs.policy.entity.TPolicyKind;
import cc.mrbird.febs.policy.entity.TPolicyKind2;
import cc.mrbird.febs.policy.service.IPolicy2Service;
import cc.mrbird.febs.policy.service.IPolicyContrastService;
import cc.mrbird.febs.policy.service.ITPolicyKind2Service;
import cc.mrbird.febs.policy.service.ITPolicyKindService;
import cc.mrbird.febs.policy.utils.CreateFileUtil;
import cc.mrbird.febs.system.entity.Dept;
import cc.mrbird.febs.system.service.IDeptService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("kind")
public class KindController {

    @Autowired
    private ITPolicyKindService kindService;
    @Autowired
    private IPolicy2Service policy2Service;
    @Autowired
    private IPolicyContrastService policyContrastService;
    @Autowired
    private ITPolicyKind2Service policyKind2Service;
    @GetMapping("select/tree")
    @ControllerEndpoint(exceptionMessage = "获取分类树失败")
    public List<KindTree<TPolicyKind>> getKindTree() throws FebsException {
        return this.kindService.findTPolicyKinds();
    }
    @GetMapping("tree2")
    @ControllerEndpoint(exceptionMessage = "获取分类树失败")
    public FebsResponse getKindTree2(TPolicyKind kind) throws FebsException {
        List<KindTree<TPolicyKind>> kinds = this.kindService.findTPolicyKinds(kind);
        return new FebsResponse().success().data(kinds);
    }

    @GetMapping("tree")
    @ControllerEndpoint(exceptionMessage = "获取分类树失败")
    public FebsResponse getKindTree(TPolicyKind kind) throws FebsException {
        List<KindTree<TPolicyKind>> kinds = this.kindService.findTPolicyKinds(kind);
        List<TPolicyKind> tpolicyKind=this.kindService.getList();
        List<PolicyContrast> policyContrastlist=this.policyContrastService.getgroupList();
        for(int q=0;q<tpolicyKind.size();q++)
        {
            int fl=0;
            if(tpolicyKind.get(q).getBranchId()!=0)
            {
                for(int w=0;w<policyContrastlist.size();w++)
                {
                    int kindid=tpolicyKind.get(q).getKindId().intValue();
                    if(kindid==policyContrastlist.get(w).getKindid())
                    {
                        fl=1;
                    }
                }
                if(fl==0)
                {
                    int id=tpolicyKind.get(q).getParentId().intValue();
                    int kindid=tpolicyKind.get(q).getKindId().intValue();
                    List<Policy> list=new ArrayList<Policy>();
                    List<TPolicyKind> policyKind=this.kindService.getTPolicyKindList(id);
                    String parentname=policyKind.get(0).getKindName();
                    List<String> keywordList=shisi(parentname);
                    String name=tpolicyKind.get(q).getKindName();
                    List<String> keywordList2=shisi(name);
                    list=policy2Service.getPolicyList();
                    add(list,keywordList,keywordList2,kindid,name,id,parentname);
                    //生成json
                    List<TPolicyKind> policyKindJSON=this.kindService.getPTPolicyKindList(id);
                   // createJson(policyKindJSON,policyKind,id);
                    //createMapJson(kindid);
                   // createParentJson(id);
                    //createEvolutionJson(kindid);
                    addEvolution(tpolicyKind);
                }
            }
        }

        return new FebsResponse().success().data(kinds);
    }

    @GetMapping("delete/{deptIds}")
    @RequiresPermissions("dept:delete")
    @ControllerEndpoint(operation = "删除分类", exceptionMessage = "删除分类失败")
    public FebsResponse deleteDepts(@NotBlank(message = "{required}") @PathVariable String deptIds) throws FebsException {
        String[] ids = deptIds.split(StringPool.COMMA);

        for(int i=0;i<ids.length;i++) {
            int id=Integer.parseInt(ids[i]);
            System.out.println(id);
            List<TPolicyKind> policyKind = this.kindService.getTPolicyKindList(id);
            int parentid=policyKind.get(0).getParentId().intValue();
            System.out.println(parentid);
            if(parentid!=0) {
                int kindid=policyKind.get(0).getKindId().intValue();
                List<PolicyContrast> list = policyContrastService.getPolicyContrastList(kindid);
                List<TPolicyKind2> tPolicyKind2s=this.policyKind2Service.getTPolicyKindListByKindoneId(kindid);
                for(int j=0;j<list.size();j++)
                {
                    int a=list.get(j).getId();
                    this.policyContrastService.deletePolicyContrasts(a);
                }
                for(int j=0;j<tPolicyKind2s.size();j++)
                {
                    int a=tPolicyKind2s.get(j).getKindId().intValue();
                    this.policyKind2Service.deleteTPolicyKind2s(a);
                }
            }
            else
            {
                List<TPolicyKind> policyChilden=this.kindService.getPTPolicyKindList(id);
                for(int j=0;j<policyChilden.size();j++)
                {
                    int kindid=policyChilden.get(j).getKindId().intValue();
                    List<PolicyContrast> list = policyContrastService.getPolicyContrastList(kindid);
                    List<TPolicyKind2> tPolicyKind2s=this.policyKind2Service.getTPolicyKindListByKindoneId(kindid);
                    for(int m=0;m<list.size();m++)
                    {
                        int a=list.get(m).getId();
                        this.policyContrastService.deletePolicyContrasts(a);
                    }
                    for(int m=0;m<tPolicyKind2s.size();m++)
                    {
                        int a=tPolicyKind2s.get(m).getKindId().intValue();
                        this.policyKind2Service.deleteTPolicyKind2s(a);
                    }
                }
                int kindoneid=policyKind.get(0).getKindId().intValue();
                List<TPolicyKind2> tPolicyKind2s=this.policyKind2Service.getTPolicyKindListByKindoneId(kindoneid);
                for(int j=0;j<tPolicyKind2s.size();j++)
                {
                    int a=tPolicyKind2s.get(j).getKindId().intValue();
                    this.policyKind2Service.deleteTPolicyKind2s(a);
                }
            }
        }
        this.kindService.deleteKinds(ids);
        return new FebsResponse().success();
    }

    @PostMapping
    @RequiresPermissions("dept:add")
    @ControllerEndpoint(operation = "新增分类", exceptionMessage = "新增分类失败")
    public FebsResponse addDept(@Valid TPolicyKind kind) {
        this.kindService.createKind(kind);
        return new FebsResponse().success();
    }

    @PostMapping("update")
    @RequiresPermissions("dept:update")
    @ControllerEndpoint(operation = "修改分类", exceptionMessage = "修改分类失败")
    public FebsResponse updateDept(@Valid TPolicyKind kind) throws FebsException {
        System.out.println();
        List<TPolicyKind> tpolictk=new ArrayList<TPolicyKind>();
        List<TPolicyKind> tpolicyKind=this.kindService.getList();
        int parentid=kind.getParentId().intValue();
        if(parentid!=0)
        {
            int ida=kind.getKindId().intValue();
            tpolictk=kindService.getTPolicyKindList(ida);
        }
        else
        {
            tpolictk=kindService.getPTPolicyKindList(parentid);
        }
        for(int k=0;k<tpolictk.size();k++)
        {
            int ida = tpolictk.get(k).getKindId().intValue();
            List<PolicyContrast> listb=policyContrastService.getPolicyContrastList(ida);
            for(int i=0;i<listb.size();i++)
            {
                int a=listb.get(i).getId();
                this.policyContrastService.deletePolicyContrasts(a);
            }
            List<TPolicyKind2> tPolicyKind2s=this.policyKind2Service.getTPolicyKindListByKindoneId(ida);
            for(int j=0;j<tPolicyKind2s.size();j++)
            {
                int a=tPolicyKind2s.get(j).getKindId().intValue();
                this.policyKind2Service.deleteTPolicyKind2s(a);
            }
            int id=tpolictk.get(k).getParentId().intValue();
            List<Policy> list=new ArrayList<Policy>();
            List<TPolicyKind> policyKind=this.kindService.getTPolicyKindList(id);

            String parentname=policyKind.get(0).getKindName();
            List<String> keywordList=shisi(parentname);
            String name=tpolictk.get(k).getKindName();
            List<PolicyContrast> listcon=new ArrayList<PolicyContrast>();
            List<PolicyContrast> listcon1=new ArrayList<PolicyContrast>();
            List<String> keywordList2=shisi(name);
            List<Policy> one=new ArrayList<Policy>();
            list=policy2Service.getPolicyList();
            add(list,keywordList,keywordList2,ida,name,id,parentname);
            List<TPolicyKind> policyKindJSON=this.kindService.getPTPolicyKindList(id);
           // createJson(policyKindJSON,policyKind,id);
            //createMapJson(ida);
           // createParentJson(id);
           // createEvolutionJson(ida);
            addEvolution(tpolicyKind);
        }
        this.kindService.updateKind(kind);
        return new FebsResponse().success();
    }
    public void addEvolution(List<TPolicyKind> tpolicyKind)
    {
        List<TPolicyKind2> tPolicyKind2s=this.policyKind2Service.getTPolicyKindByKindoneId();
        for(int i=0;i<tpolicyKind.size();i++)
        {
            int flag=0;
            for(int j=0;j<tPolicyKind2s.size();j++)
            {
                if(tpolicyKind.get(i).getKindId()==tPolicyKind2s.get(j).getKindoneId())
                {
                    flag=1;
                }
            }
            if(flag==0)
            {
                if(tpolicyKind.get(i).getBranchId()==0)
                {
                    TPolicyKind2 tPolicyKind2=new TPolicyKind2();
                    Long parentid=new Long(0);
                    tPolicyKind2.setParentId(parentid);
                    tPolicyKind2.setKindName(tpolicyKind.get(i).getKindName());
                    tPolicyKind2.setOrderNum(tpolicyKind.get(i).getOrderNum());
                    tPolicyKind2.setBranchId(0);
                    tPolicyKind2.setYearList(tpolicyKind.get(i).getYearList());
                    tPolicyKind2.setCityList(tpolicyKind.get(i).getCityList());
                    tPolicyKind2.setKindoneId(tpolicyKind.get(i).getKindId().intValue());
                    this.policyKind2Service.createKind(tPolicyKind2);
                }
                else
                {
                    int kindid=tpolicyKind.get(i).getKindId().intValue();
                    List<PolicyContrast> policyContrastList=this.policyContrastService.getPolicyContrastList(kindid);
                    int num=1;
                    for(int j=0;j<policyContrastList.size();j++)
                    {
                        TPolicyKind2 tPolicyKind2=new TPolicyKind2();
                        Long parentid=new Long(policyContrastList.get(j).getParentid());
                        tPolicyKind2.setParentId(parentid);
                        tPolicyKind2.setBranchId(2);
                        tPolicyKind2.setOrderNum(num);
                        tPolicyKind2.setKindName(policyContrastList.get(j).getPolicyname());
                        tPolicyKind2.setPolicyId(policyContrastList.get(j).getId());
                        tPolicyKind2.setKindoneId(policyContrastList.get(j).getParentid());
                        this.policyKind2Service.createKind(tPolicyKind2);
                        num+=1;
                    }
                    int parentid=tpolicyKind.get(i).getParentId().intValue();
                    List<TPolicyKind> tPolicyKindList=this.kindService.getPTPolicyKindList(parentid);
                    List<TPolicyKind2> tPolicyKind2s1=this.policyKind2Service.getTPolicyKindListByParentId(parentid);
                    for(int j=0;j<tPolicyKindList.size();j++)
                    {
                       for(int m=0;m<tPolicyKind2s1.size();m++)
                       {
                           int judge=0;
                           int number=1;
                           List<TPolicyKind2> tPolicyKind2List=this.policyKind2Service.getTPolicyKindListByParentId(tPolicyKind2s1.get(m).getKindId().intValue());
                           for(int n=0;n<tPolicyKind2List.size();n++)
                           {
                               if(tPolicyKind2List.get(n).getKindName().equals(tPolicyKindList.get(j).getKindName()))
                               {
                                   judge=1;
                                   number+=1;
                               }
                           }
                           if(judge==0)
                           {
                               TPolicyKind2 tPolicyKind2=new TPolicyKind2();
                               Long parent=new Long(tPolicyKind2s1.get(m).getKindId());
                               tPolicyKind2.setParentId(parent);
                               tPolicyKind2.setBranchId(1);
                               tPolicyKind2.setOrderNum(number);
                               tPolicyKind2.setKindName(tPolicyKindList.get(j).getKindName());
                               tPolicyKind2.setKindoneId(tPolicyKindList.get(j).getKindId().intValue());
                               this.policyKind2Service.createKind(tPolicyKind2);

                           }
                       }
                    }
                }
            }
        }
    }
    public void createEvolutionJson(int kindid)
    {
        List<PolicyContrast> policyContrastList=this.policyContrastService.getPolicyContrastListByTime(kindid);
        JSONObject q1 = new JSONObject();
        JSONObject q2 = new JSONObject();
        JSONArray array = new JSONArray();
        JSONArray array2 = new JSONArray();
        JSONArray array3 = new JSONArray();
        for(int i=0;i<policyContrastList.size();i++)
        {
            array.add(policyContrastList.get(i).getAllnum());
            array2.add(policyContrastList.get(i).getPolicytime());
        }
        q1.put("name", "政策数量");
        q1.put("data", array);
        array3.add(q1);
        q2.put("data", array3);
        q2.put("type", array2);
        FileOutputStream fileOutputStream;
        try {
            File file=new File("G:\\A科技政策系统\\science_technology_policy_integration_service (3)\\json_for_evolution2"+kindid+".json");
            if(!file.exists())//判断文件是否存在，若不存在则新建
            {
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter=new OutputStreamWriter(fileOutputStream,"utf-8");//将字符流转换为字节流
            BufferedWriter bufferedWriter= new BufferedWriter(outputStreamWriter);//创建字符缓冲输出流对象
            String jsonString=q2.toString();//将jsonarray数组转化为字符串
            bufferedWriter.write(jsonString);//将格式化的jsonarray字符串写入文件
            bufferedWriter.flush();//清空缓冲区，强制输出数据
            bufferedWriter.close();//关闭输出流
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void createParentJson(int parentid)
    {
        List<TPolicyKind> tPolicyKinds=this.kindService.getPTPolicyKindList(parentid);
        List<String> cityresult=new ArrayList<String>();
        for(int i=0;i<tPolicyKinds.size();i++)
        {
            String citylist[]=tPolicyKinds.get(i).getCityList().split(",");
            for(int j=0;j<citylist.length;j++)
            {
                cityresult.add(citylist[j]);
            }
        }
        cityresult=removeDuplicate(cityresult);
        String city="";
        for(int i=0;i<cityresult.size();i++)
        {
            if(city.equals(""))
            {
                city=city+cityresult.get(i);
            }
            else
            {
                city=city+","+cityresult.get(i);
            }
        }
        TPolicyKind tPolicyKind=this.kindService.getById(parentid);
        tPolicyKind.setCityList(city);
        this.kindService.updateKind(tPolicyKind);
        JSONArray array3 = new JSONArray();
        JSONArray array2 = new JSONArray();
        JSONArray array = new JSONArray();
        JSONObject q1 = new JSONObject();
        JSONObject q2 = new JSONObject();
        for(int i=0;i<cityresult.size();i++)
        {
            array3.add(cityresult.get(i));
        }

        for(int i=0;i<tPolicyKinds.size();i++)
        {
            q1 = new JSONObject();
            array = new JSONArray();
            String name=tPolicyKinds.get(i).getKindName();
            q1.put("name",name);
            int kindid=tPolicyKinds.get(i).getKindId().intValue();
            List<PolicyContrast> policyContrasts=this.policyContrastService.getPolicyContrastList(kindid);
            for(int j=0;j<cityresult.size();j++)
            {
                int num=0;
                if(cityresult.get(j).equals("全国"))
                {
                    for(int m=0;m<policyContrasts.size();m++)
                    {
                        String policyname=policyContrasts.get(m).getPolicyname();
                        String policydept=policyContrasts.get(m).getPolicydept();
                        if(policyname.indexOf("国")!=-1||policyname.indexOf("科技部")!=-1||policyname.indexOf("中共")!=-1||policydept.indexOf("国")!=-1||policydept.indexOf("科技部")!=-1||policydept.indexOf("中共")!=-1)
                        {
                            num+=1;
                        }
                    }
                }
                else
                {
                    for(int m=0;m<policyContrasts.size();m++)
                    {
                        String policyname=policyContrasts.get(m).getPolicyname();
                        String policydept=policyContrasts.get(m).getPolicydept();
                        if(policyname.indexOf(cityresult.get(j))!=-1||policydept.indexOf(cityresult.get(j))!=-1)
                        {
                            num+=1;
                        }
                    }
                }
                array.add(num);
            }
            q1.put("data",array);
            array2.add(q1);
        }
        q2.put("infor", array2);
        q2.put("city", array3);
        FileOutputStream fileOutputStream;
        String path="G:\\A科技政策系统\\science_technology_policy_integration_service (3)/json_for_kind/"+parentid+".json";
        try {
            File file=new File(path);
            if(!file.exists())//判断文件是否存在，若不存在则新建
            {
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter=new OutputStreamWriter(fileOutputStream,"utf-8");//将字符流转换为字节流
            BufferedWriter bufferedWriter= new BufferedWriter(outputStreamWriter);//创建字符缓冲输出流对象
            String jsonString=q2.toString();//将jsonarray数组转化为字符串
            bufferedWriter.write(jsonString);//将格式化的jsonarray字符串写入文件
            bufferedWriter.flush();//清空缓冲区，强制输出数据
            bufferedWriter.close();//关闭输出流
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    public void createMapJson(int kindid)
    {
        String[] province=new String[] {"国","科技部","中央","北京","天津","上海","河北","广东","四川","辽宁","吉林","黑龙江","江苏","浙江","安徽","福建","江西","山东","河南","湖北","湖南","山西","广西","海南","重庆","内蒙古","贵州","云南"
                ,"西藏","陕西","甘肃","青海","宁夏","新疆","台湾","香港","澳门"};
        List<PolicyContrast> policyContrastList=this.policyContrastService.getPolicyContrastList(kindid);
        List<String> citylist=new ArrayList<String>();
        List<String> cityresult=new ArrayList<String>();
        for(int i=0;i<policyContrastList.size();i++)
        {
            String poliydept=policyContrastList.get(i).getPolicydept();
            String policyname=policyContrastList.get(i).getPolicyname();
            PolicyContrast policyContrast=new PolicyContrast();
            int flag=0;
            for(int j=0;j<province.length;j++)
            {
                if((poliydept.indexOf(province[j])!=-1||policyname.indexOf(province[j])!=-1)&&flag==0)
                {
                    citylist.add(province[j]);
                }
            }
        }
        citylist=removeDuplicate(citylist);
        String city="";
        int flag=0;
        for(int i=0;i<province.length;i++)
        {
            for(int j=0;j<citylist.size();j++)
            {
                if(citylist.get(j)==province[i])
                {
                    if(citylist.get(j).equals("国")||citylist.get(j).equals("科技部")||citylist.get(j).equals("中央"))
                    {
                        if(flag==0)
                        {
                            flag=1;
                            cityresult.add("全国");
                            if(city.equals(""))
                            {
                                city=city+"全国";
                            }
                            else
                            {
                                city=city+","+"全国";
                            }
                        }
                    }
                    else
                    {
                        cityresult.add(province[i]);
                        if(city.equals(""))
                        {
                            city=city+province[i];
                        }
                        else
                        {
                            city=city+","+province[i];
                        }
                    }
                }
            }
        }
        TPolicyKind tPolicyKind=this.kindService.getById(kindid);
        tPolicyKind.setCityList(city);
        this.kindService.updateKind(tPolicyKind);
        JSONObject q1 = new JSONObject();
        JSONObject q2 = new JSONObject();
        JSONObject q3 = new JSONObject();
        JSONArray array = new JSONArray();
        JSONArray array2 = new JSONArray();
        JSONArray array3 = new JSONArray();
        int[] numlist=suiji();
        int jishu=0;
        for(int i=0;i<cityresult.size();i++)
        {
            array.add(cityresult.get(i));
        }
        for(int i=0;i<5;i++)
        {
           System.out.println(1);
            q1 = new JSONObject();
            array2 = new JSONArray();
            int tiao=i+1;
            q1.put("name", "第"+tiao+"条");
            for(int j=0;j<cityresult.size();j++)
            {
                int flaga=0;
                for(int m=0;m<policyContrastList.size();m++)
                {
                    String policydept=policyContrastList.get(m).getPolicydept();
                    String policyname=policyContrastList.get(m).getPolicyname();
                    String a[]=policyContrastList.get(m).getPolicymain().split("；|。");
                    System.out.println(a.length);
                    if(cityresult.get(j).equals("全国"))
                    {
                        if((policyname.indexOf("国")!=-1||policyname.indexOf("科技部")!=-1||policyname.indexOf("中共")!=-1||policydept.indexOf("国")!=-1||policydept.indexOf("科技部")!=-1||policydept.indexOf("中共")!=-1)&&flaga==0)
                        {
                            if(a.length<i+1)
                            {
                                array2.add(null);
                                jishu+=1;
                                flaga=1;
                            }
                            else
                            {
                                array2.add(numlist[jishu]);
                                String str=a[i].replace("\n", "");
                                str=str.replace("\r", "");
                                str=str.replace("\t", "");
                                str=str.replace(" ", "");
                                q3.put(numlist[jishu]+"", str);
                                jishu+=1;
                                flaga=1;
                            }
                        }
                    }
                    else
                    {
                        if((policyname.indexOf(cityresult.get(j))!=-1||policydept.indexOf(cityresult.get(j))!=-1)&&flaga==0)
                        {
                            if(a.length<i+1)
                            {
                                array2.add(null);
                                jishu+=1;
                                flaga=1;
                            }
                            else
                            {
                                array2.add(numlist[jishu]);
                                String str=a[i].replace("\n", "");
                                str=str.replace("\r", "");
                                str=str.replace("\t", "");
                                str=str.replace(" ", "");
                                q3.put(numlist[jishu]+"", str);
                                jishu+=1;
                                flaga=1;
                            }
                        }
                    }
                }
            }
            System.out.println(jishu);
            q1.put("data",array2);
            array3.add(q1);
            System.out.println(4);
        }
        q3.put("infor",array3);
        q3.put("city",array);
        FileOutputStream fileOutputStream;
        String path="G:\\A科技政策系统\\science_technology_policy_integration_service (3)/json_for_kind/"+kindid+".json";
        try {
            File file=new File(path);
            if(!file.exists())//判断文件是否存在，若不存在则新建
            {
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter=new OutputStreamWriter(fileOutputStream,"utf-8");//将字符流转换为字节流
            BufferedWriter bufferedWriter= new BufferedWriter(outputStreamWriter);//创建字符缓冲输出流对象
            String jsonString=q3.toString();//将jsonarray数组转化为字符串
            bufferedWriter.write(jsonString);//将格式化的jsonarray字符串写入文件
            bufferedWriter.flush();//清空缓冲区，强制输出数据
            bufferedWriter.close();//关闭输出流
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
    public void add(List<Policy> list,List<String> keywordList,List<String> keywordList2,int kindid,String name,int parentid,String parentname)
    {
        List<PolicyContrast> policyContrastList=new ArrayList<PolicyContrast>();
        List<Policy> one=new ArrayList<Policy>();
        int id=list.get(0).getId();
        Policy policy = this.policy2Service.getPolicyById(id);
        List<String> policykeylist=new ArrayList<>();
        policykeylist.addAll(keywordList);
        policykeylist.addAll(keywordList2);
        policykeylist=removeDuplicate(policyContrastList);
        String policykeyresult="";
        for(int i=0;i<policykeylist.size();i++)
        {
            policykeyresult=policykeyresult+" "+policykeylist.get(i);
        }
        System.out.println(policy.getName());
        for(int i=0;i<list.size();i++)
        {
            String text=list.get(i).getText();
            int judge=0;
            int num=0;
            for(int j=0;j<keywordList.size();j++)
            {
                if(text.indexOf(keywordList.get(j))!=-1)
                {
                }
                else
                {
                    judge=1;
                }
            }
            for(int m=0;m<keywordList2.size();m++)
            {
                if(text.indexOf(keywordList2.get(m))!=-1&&judge==0&&num==0)
                {
                    one.add(list.get(i));
                    num=1;
                }
            }
        }
        one=removeDuplicatep(one);
        String[] city=new String[] {"国","北京","天津","上海","河北","广东","四川","辽宁","吉林","黑龙江","江苏","浙江","安徽","福建","江西","山东","河南","湖北","湖南","山西","广西","海南","重庆","内蒙古","贵州","云南"
                ,"西藏","陕西","甘肃","青海","宁夏","新疆","台湾","香港","澳门","石家庄","唐山","秦皇岛","邯郸","邢台","保定","张家口","承德","沧州","廊坊","衡水"};
        for(int i=0;i<one.size();i++)
        {
            String result="";
            String a[]=list.get(i).getText().split("。");
            for(int j=0;j<a.length;j++)
            {
                int flag=0;
                int chan=1;
                for(int m=0;m<keywordList.size();m++)
                {
                    if (a[j].indexOf(keywordList.get(m)) != -1)
                    {

                    }
                    else
                    {
                        chan=0;
                    }
                }
                for(int m=0;m<keywordList2.size();m++) {
                    if (a[j].indexOf(keywordList2.get(m)) != -1&&flag==0&&chan==1) {
                        flag=1;
                        if (result.equals("")) {
                            result = result + a[j];
                        } else {
                            result = result + "。" + a[j];
                        }

                    }
                }
            }
            if(!result.equals("")&&result.indexOf("附件")==-1&&result.length()>100&&result.length()<700)
            {

                PolicyContrast policyContrast=new PolicyContrast();
                for(int m=0;m<city.length;m++)
                {
                    if(one.get(i).getRange()!=null) {
                        String range = one.get(i).getRange();
                        System.out.println(range);
                        if (range.indexOf(city[m]) != -1) {
                            if (city[m].equals("国")) {
                                policyContrast.setCity("全国");
                            } else {
                                policyContrast.setCity(city[m]);
                            }
                        }
                    }
                }
                try {
                    if(one.get(i).getYear()!=null) {
                        String time = one.get(i).getYear();
                        policyContrast.setPolicytime(time);
                        String timelist[]=time.split("-");
                        System.out.println();
                        policyContrast.setYear(timelist[0]);
                    }
                }catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                policyContrast.setPolicyname(one.get(i).getName());
                policyContrast.setPolicymain(result);
                String policykey=NLPkey(result);
                policyContrast.setPolicykey(policykey);
                policyContrast.setPolicydept(one.get(i).getOrgan());
                policyContrast.setKind(name);
                policyContrast.setKindid(kindid);
                String keyword=NLP(result);
                int idd=one.get(i).getId();
                policyContrast.setPolicyid(idd);
                policyContrast.setPolicykey(policykeyresult.trim());
                policyContrast.setPolicyid(idd);
                policyContrast.setParentname(parentname);
                policyContrast.setParentid(parentid);
                policyContrastList.add(policyContrast);
                //this.policyContrastService.createPolicyContrast(policyContrast);
            }
        }
        String[] province=new String[] {"国","中共","科技部","北京","天津","上海","河北","广东","四川","辽宁","吉林","黑龙江","江苏","浙江","安徽","福建","江西","山东","河南","湖北","湖南","山西","广西","海南","重庆","内蒙古","贵州","云南"
                ,"西藏","陕西","甘肃","青海","宁夏","新疆","台湾","香港","澳门","石家庄","唐山","秦皇岛","邯郸","邢台","保定","张家口","承德","沧州","廊坊","衡水"};
        List<PolicyContrast> policyContrasts=new ArrayList<>();
        for(int i=0;i<province.length;i++)
        {
            for(int j=0;j<policyContrastList.size();j++)
            {
                String policyname=policyContrastList.get(j).getPolicyname();
                String policydept=policyContrastList.get(j).getPolicydept();
                if(policyname.indexOf(province[i])!=-1||policydept.indexOf(province[i])!=-1)
                {
                    if(policyContrasts.size()>=1)
                    {
                        int flag=0;
                        for(int n=0;n<policyContrasts.size();n++)
                        {
                            int policyid=policyContrastList.get(j).getPolicyid();
                            if(policyContrasts.get(n).getPolicyid()==policyid)
                            {
                                flag=1;
                            }
                        }
                        if(flag==0)
                        {
                            policyContrasts.add(policyContrastList.get(j));
                        }

                    }
                    else
                    {
                        policyContrasts.add(policyContrastList.get(j));
                    }
                }
            }
        }
        for(int i=0;i<policyContrastList.size();i++)
        {
            int flag=0;
            int policyid=policyContrastList.get(i).getPolicyid();
            for(int j=0;j<policyContrasts.size();j++)
            {
                int aid=policyContrasts.get(j).getPolicyid();

                if(policyid==aid)
                {
                    flag=1;
                }
            }
            if(flag==0)
            {
                policyContrasts.add(policyContrastList.get(i));
            }
        }
        for(int i=0;i<policyContrasts.size();i++)
        {
            PolicyContrast policyContrast=policyContrasts.get(i);
            policyContrast.setSortid(i);
            this.policyContrastService.createPolicyContrast(policyContrast);
        }
    }
    public void createJson(List<TPolicyKind> policyKindJSON,List<TPolicyKind> policyKind,int id)
    {
        List<PolicyContrast> listcon=new ArrayList<PolicyContrast>();
        for(int i=0;i<policyKindJSON.size();i++)
        {
            String kindName=policyKindJSON.get(i).getKindName();
            int kindid=policyKindJSON.get(i).getKindId().intValue();
            List<PolicyContrast> lista=this.policyContrastService.getPolicyContrastList(kindid);
            listcon.addAll(lista);
        }
        List<Policy> be = new ArrayList<Policy>();
        //json数据准备
        String[] province=new String[] {"北京","天津","河北","山西","内蒙古","辽宁","吉林","黑龙江","上海","江苏","浙江","安徽","福建","江西","山东","河南","湖北","湖南","广东","广西","海南","重庆","四川","贵州","云南"
                ,"西藏","陕西","甘肃","青海","宁夏","新疆","台湾","香港","澳门"};
        try {
            for(int i=0;i<listcon.size();i++)
            {
                try {
                    Policy po = new Policy();
                    int policyid = listcon.get(i).getPolicyid();
                    Policy policy = this.policy2Service.getPolicyById(policyid);
                    String dept = policy.getOrgan();
                    String date ="";

                    if (policy.getPubdata()!= null) {
                        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                        Date data = (Date) sdf.parse(policy.getPubdata().toString());
                        date = new SimpleDateFormat("yyyy").format(data);

                    }

                    String text = policy.getText();
                    int flag = 0;
                    for (int j = 1; j < province.length; j++) {
                        if (dept.indexOf(province[j]) != -1 && flag == 0) {
                            po.setOrgan(province[j]);
                            System.out.println(date);
                            po.setCreateTimeFrom(date);
                            flag = 1;
                        }
                    }
                    if (flag == 0) {
                        for (int j = 1; j < province.length; j++) {
                            if (text.indexOf(province[j]) != -1 && flag == 0) {
                                po.setOrgan(province[j]);
                                po.setCreateTimeFrom(date);
                                flag = 1;
                            }
                        }
                    }
                    be.add(po);
                }catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        List<String> list1=new ArrayList<String>();
        List list2=new ArrayList();
        for(int i=0;i<be.size();i++)
        {
            if(be.get(i).getOrgan()!=null)
            {
                list1.add(be.get(i).getOrgan());
            }
            if(be.get(i).getCreateTimeFrom()!=null)
            {
                int year=Integer.parseInt(be.get(i).getCreateTimeFrom());
                list2.add(year);
            }

        }
        list1=removeDuplicate(list1);
        list2=removeDuplicate(list2);
        Collections.sort(list2);
        String year="";
        for(int i=0;i<list2.size();i++)
        {
            if(year.equals(""))
            {
                year=year+list2.get(i).toString();
            }
            else
            {
                year=year+","+list2.get(i).toString();
            }
        }
        TPolicyKind policyt=new TPolicyKind();
        policyt=policyKind.get(0);
        policyt.setYearList(year);
        this.kindService.updateKind(policyt);
        JSONObject q1 = new JSONObject();
        JSONArray array = new JSONArray();
        JSONArray array2 = new JSONArray();
        JSONObject w1 = new JSONObject();
        Random random=new Random();
        for(int i=0;i<list2.size();i++)
        {
            array = new JSONArray();
            for(int j=0;j<list1.size();j++)
            {
                int num=random.nextInt(70)+30;
                q1 = new JSONObject();
                q1.put("name", list1.get(j));
                q1.put("value", num);
                q1.put("year", list2.get(i));
                array.add(q1);
            }
            array2.add(array);
        }
        w1.put("mapDetails", array2);
        FileOutputStream fileOutputStream;
        String path="G:\\A科技政策系统\\science_technology_policy_integration_service (3)/json_for_CreateMapData/"+id+".json";
        try {
            File file=new File(path);
            if(!file.exists())//判断文件是否存在，若不存在则新建
            {
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file);
            OutputStreamWriter outputStreamWriter=new OutputStreamWriter(fileOutputStream,"utf-8");//将字符流转换为字节流
            BufferedWriter bufferedWriter= new BufferedWriter(outputStreamWriter);//创建字符缓冲输出流对象
            String jsonString=w1.toString();//将jsonarray数组转化为字符串
            bufferedWriter.write(jsonString);//将格式化的jsonarray字符串写入文件
            bufferedWriter.flush();//清空缓冲区，强制输出数据
            bufferedWriter.close();//关闭输出流
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static List<String> shisi(String text)
    {
        List<String> keywordList = HanLP.extractKeyword(text, 5);

        return keywordList;
    }
    public   static   List  removeDuplicate(List list)  {
        for  ( int  i  =   0 ; i  <  list.size()  -   1 ; i ++ )  {
            for  ( int  j  =  list.size()  -   1 ; j  >  i; j -- )  {
                if  (list.get(j).equals(list.get(i)))  {
                    list.remove(j);
                }
            }
        }
        return list;
    }
    public   static   List<Policy>  removeDuplicatep(List<Policy> list)  {
        for  ( int  i  =   0 ; i  <  list.size()  -   1 ; i ++ )  {
            for  ( int  j  =  list.size()  -   1 ; j  >  i; j -- )  {
                if  (list.get(j).getName().equals(list.get(i).getName()))  {
                    list.remove(j);
                }
            }
        }
        return list;
    }
    public static String NLP(String text)
    {
        List<Term> termList= NLPTokenizer.segment(text);
        String keyword="";
        for(int i=0;i<termList.size();i++)
        {
            if(termList.get(i).toString().indexOf("/jl")!=-1)
            {
                String a[]=termList.get(i).toString().split("/");
                if(keyword.equals(" "))
                {
                    keyword=keyword+a[0];
                }
                else
                {
                    keyword=keyword+" "+a[0];
                }

            }
        }
        return keyword;
    }
    public static int[] suiji()
    {
        int[] intRandom = new int[150];
        List mylist = new ArrayList(); //生成数据集，用来保存随即生成数，并用于判断
        Random rd = new Random();
        while(mylist.size() < 150) {
            int num = rd.nextInt(150)+50;
            if(!mylist.contains(num)) {
                mylist.add(num); //往集合里面添加数据。
            }
        }
        for(int j = 0;j <mylist.size();j++) {
            intRandom[j] = (Integer)(mylist.get(j));
        }
        return intRandom;
    }
    public static String NLPkey(String text)
    {
        List<Term> termList=NLPTokenizer.segment(text);
        List<String> list=new ArrayList<String>();
        String keyword="";
        for(int i=0;i<termList.size();i++)
        {
            if(termList.get(i).toString().indexOf("/jl")!=-1)
            {
                String a[]=termList.get(i).toString().split("/");
                list.add(a[0]);
            }
        }
        list=removeDuplicate(list);
        for(int i=0;i<list.size();i++)
        {
            keyword=keyword+" "+list.get(i);
        }
        String result=keyword.trim();
        return result;
    }

}
