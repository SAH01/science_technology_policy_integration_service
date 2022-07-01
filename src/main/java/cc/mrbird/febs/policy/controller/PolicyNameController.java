package cc.mrbird.febs.policy.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.NameTree;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.policy.entity.PolicyEvo;
import cc.mrbird.febs.policy.entity.PolicyName;
import cc.mrbird.febs.policy.entity.PolicyNew;
import cc.mrbird.febs.policy.service.IPolicyEvoService;
import cc.mrbird.febs.policy.service.IPolicyNameService;
import cc.mrbird.febs.policy.service.IPolicyNew2Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.hankcs.hanlp.HanLP;
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
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("Name")
public class PolicyNameController {
    @Autowired
    private IPolicyNameService policyNameService;
    @Autowired
    private IPolicyNew2Service policyNew2Service;
    @Autowired
    private IPolicyEvoService policyEvoService;

    @GetMapping("select/tree")
    @ControllerEndpoint(exceptionMessage = "获取部门树失败")
    public List<NameTree<PolicyName>> getNameTree() throws FebsException {
        return this.policyNameService.findPolicyNames();
    }

    @GetMapping("tree")
    @ControllerEndpoint(exceptionMessage = "获取部门树失败")
    public FebsResponse getNameTree(PolicyName policyName) throws FebsException {
        List<PolicyName> policyNameList=this.policyNameService.getAllList();
        List<PolicyEvo> policyEvos=this.policyEvoService.getPolicyEvoGroupByType();
        for(int i=0;i<policyNameList.size();i++)
        {
            int flag=0;
            for(int j=0;j<policyEvos.size();j++)
            {
                if(policyNameList.get(i).getTypeName().equals(policyEvos.get(j).getPolicytype()))
                {
                    flag=1;
                }
            }
            if(flag==0)
            {
                String name=policyNameList.get(i).getTypeName();
                int id=policyNameList.get(i).getTypeId().intValue();
                SelectPolicy(name);
                createAllJson();
                createTypeJson(name,id);
            }
        }
        List<NameTree<PolicyName>> policyNames = this.policyNameService.findPolicyNames(policyName);
        return new FebsResponse().success().data(policyNames);
    }


    @PostMapping
    @RequiresPermissions("dept:add")
    @ControllerEndpoint(operation = "新增部门", exceptionMessage = "新增部门失败")
    public FebsResponse addType(@Valid PolicyName type) {
        String name=type.getTypeName();


        this.policyNameService.createType(type);
        return new FebsResponse().success();
    }

    @GetMapping("delete/{typeIds}")
    @RequiresPermissions("dept:delete")
    @ControllerEndpoint(operation = "删除部门", exceptionMessage = "删除部门失败")
    public FebsResponse deleteTypes(@NotBlank(message = "{required}") @PathVariable String typeIds) throws FebsException {
        String[] ids = typeIds.split(StringPool.COMMA);
        for(int i=0;i<ids.length;i++) {
            int id = Integer.parseInt(ids[i]);
            DeletePolicy(id);
        }
        createAllJson();

        this.policyNameService.deleteTypes(ids);
        return new FebsResponse().success();
    }

    @PostMapping("update")
    @RequiresPermissions("dept:update")
    @ControllerEndpoint(operation = "修改部门", exceptionMessage = "修改部门失败")
    public FebsResponse updateType(@Valid PolicyName type) throws FebsException {
        int id=type.getTypeId().intValue();
        String name=type.getTypeName();
        DeletePolicy(id);
        this.policyNameService.updateType(type);
        SelectPolicy(name);
        createAllJson();
        createTypeJson(name,id);
        return new FebsResponse().success();
    }
    public void DeletePolicy(int id)
    {
        PolicyName policyName=this.policyNameService.getPolicyNameById(id);
        String type=policyName.getTypeName();
        List<PolicyEvo> policyEvoList=this.policyEvoService.getPolicyEvoList(type);
        for(int j=0;j<policyEvoList.size();j++)
        {
            int policyid=policyEvoList.get(j).getId();
            this.policyEvoService.deletePolicyEvo(policyid);
        }
    }
    public void SelectPolicy(String name)
    {
        List<PolicyNew> list=policyNew2Service.getPolicyNewList();
        List<PolicyNew> policyNewList=new ArrayList<PolicyNew>();
        for(int i=0;i<list.size();i++)
        {
            String policyname=list.get(i).getPolicyname();
            if(policyname.indexOf(name)!=-1)
            {
                System.out.println(policyname);
                policyNewList.add(list.get(i));
            }
        }
        policyNewList=removeDuplicate(policyNewList);
        List<String> keywordList=keyWord(name);
        for(int i=0;i<policyNewList.size();i++)
        {
            System.out.println(i);
            List<String> textlist=new ArrayList<String>();
            String text=policyNewList.get(i).getPolicytext();
            String a[]=text.split("。|\\n");
            for(int j=0;j<a.length;j++)
            {
                int flag=0;
                for(int m=0;m<keywordList.size();m++)
                {
                    String key=keywordList.get(m);
                    if(flag==0&&a[j].indexOf(key)!=-1&&(a[j].indexOf("内容")!=-1||a[j].indexOf("措施")!=-1||a[j].indexOf("方法")!=-1||a[j].indexOf("办法")!=-1||a[j].indexOf("条件")!=-1))
                    {
                        System.out.println(789);
                        textlist.add(a[j]);
                        flag=1;
                    }
                }
            }
            String text2="";
            if(textlist.size()==0)
            {

                for(int j=0;j<a.length;j++)
                {
                    if(a[j].length()>=10)
                    {
                        if(text2.equals(""))
                        {
                            text2=text2+a[j];
                        }
                        else
                        {
                            text2=text2+","+a[j];
                        }
                    }
                }
            }
            else
            {
                for(int j=0;j<textlist.size();j++)
                {
                    if(j==0)
                    {
                        text2=text2+textlist.get(j);
                    }
                    else
                    {
                        text2=text2+","+textlist.get(j);
                    }
                }
                System.out.println(text2);
            }
            PolicyEvo policyEvo=new PolicyEvo();
            policyEvo.setPolicytext2(text2);
            policyEvo.setPolicydept(policyNewList.get(i).getPolicydept());
            policyEvo.setPolicyname("《"+policyNewList.get(i).getPolicyname()+"》");
            policyEvo.setHighpolicy(policyNewList.get(i).getHighpolicy());
            policyEvo.setLowpolicy(policyNewList.get(i).getLowpolicy());
            policyEvo.setPolicytype(name);
            String time=policyNewList.get(i).getPolicytime();
            String timelist[]=time.split("\n");
            String policytime="";
            if(isNumeric(timelist[0])==true)
            {
                policytime=timelist[0];
            }
            policyEvo.setPolicytime(policytime);
            this.policyEvoService.createPolicyEvo(policyEvo);
        }

    }
    public void createAllJson()
    {
        JSONObject q1 = new JSONObject();
        JSONObject q2 = new JSONObject();
        JSONArray array = new JSONArray();
        JSONArray array2 = new JSONArray();
        JSONArray array3 = new JSONArray();
        List<PolicyEvo> policyEvos=this.policyEvoService.getPolicyEvoGroupByType();
        for(int i=0;i<policyEvos.size();i++)
        {
            String typename=policyEvos.get(i).getPolicytype();
            int allsum=policyEvos.get(i).getAllsum();
            array.add(typename);
            array2.add(allsum);

        }
        q1.put("name","政策数量");
        q1.put("data",array2);
        array3.add(q1);
        q2.put("data",array3);
        q2.put("type",array);
        FileOutputStream fileOutputStream;
        try {
            File file=new File("json_for_evolution/evolution.json");
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
    public void createTypeJson(String name,int id)
    {
        List<PolicyEvo> policyEvos=this.policyEvoService.getPolicyEvoListByTime(name);
        JSONObject q1 = new JSONObject();
        JSONObject q2 = new JSONObject();
        JSONArray array = new JSONArray();
        JSONArray array2 = new JSONArray();
        JSONArray array3 = new JSONArray();
        for(int i=0;i<policyEvos.size();i++)
        {
            String time=policyEvos.get(i).getPolicytime();
            if(!time.equals(""))
            {
                int sum=policyEvos.get(i).getAllsum();
                String a[]=time.split("\n");
                array.add(sum);
                array2.add(a[0]);
            }
        }
        q1.put("name", "政策数量");
        q1.put("data", array);
        array3.add(q1);

        q2.put("data", array3);
        q2.put("type", array2);
        FileOutputStream fileOutputStream;
        try {
            File file=new File("json_for_evolution/"+id+".json");
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
    public   static   List<PolicyNew>  removeDuplicate(List<PolicyNew> list)  {
        for  ( int  i  =   0 ; i  <  list.size()  -   1 ; i ++ )  {
            for  ( int  j  =  list.size()  -   1 ; j  >  i; j -- )  {
                if  (list.get(j).getPolicyname().equals(list.get(i).getPolicyname()))  {
                    list.remove(j);
                }
            }
        }
        return list;
    }
    public static List<String> keyWord(String type)
    {
        List<String> keywordList = HanLP.extractKeyword(type, 5);
        return keywordList;
    }
    public static boolean isNumeric(String str){
        for (int i = str.length();--i>=0;){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }
}
