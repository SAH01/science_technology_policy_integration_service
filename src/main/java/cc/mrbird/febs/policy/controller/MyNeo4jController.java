package cc.mrbird.febs.policy.controller;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.policy.entity.NodeEntity;
import cc.mrbird.febs.policy.entity.PolicyNeo4j;
import cc.mrbird.febs.policy.entity.Theme;
import cc.mrbird.febs.policy.service.IPolicyNeo4jService;
import cc.mrbird.febs.policy.service.ThemeService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.v1.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
@Slf4j
@RestController
@RequestMapping("neo4j")
public class MyNeo4jController {
    @Autowired
    private IPolicyNeo4jService policyNeo4jService;
    @Autowired
    private ThemeService themeService;
    List<String> list2=new ArrayList<>();
    @GetMapping("namelist")
    public FebsResponse namelist() {
        List<PolicyNeo4j> policyNeo4jList=policyNeo4jService.getPolicyNeo4jList();
        JSONArray array = new JSONArray();
        for(int i=0;i<policyNeo4jList.size();i++)
        {
            String name=policyNeo4jList.get(i).getPolicyname();
            array.add(name);
        }
        return new FebsResponse().success().data(array);
    }
    @GetMapping("getAtladept/{dept}")
    public FebsResponse getAtladept(@PathVariable String dept) {
        JSONObject q1=new JSONObject();
        JSONObject q2=new JSONObject();
        JSONObject q3=new JSONObject();
        JSONArray array = new JSONArray();
        JSONArray array2 = new JSONArray();
        List<PolicyNeo4j> policyNeo4jList3=policyNeo4jService.getPolicyNeo4jList();
        List<PolicyNeo4j> policyNeo4jList=new ArrayList<>();
        for(int i=0;i<policyNeo4jList3.size();i++)
        {
            String policydept=policyNeo4jList3.get(i).getDept();
            if(!policydept.equals("")&&policydept!=null) {
                if (policydept.equals(dept)) {
                    policyNeo4jList.add(policyNeo4jList3.get(i));
                }
            }
        }
        List<PolicyNeo4j> policyNeo4jList2=new ArrayList<>();
        List<String> list1=new ArrayList<>();
        List<String> list2=new ArrayList<>();
        List<String> list3=new ArrayList<>();
        List<Theme> themeList=themeService.getThemeList();
        for(int i=0;i<42;i++)
        {
            String thelist[]=themeList.get(i).getThirdname().split(" ");
            for(int j=0;j<thelist.length;j++)
            {
                list1.add(thelist[j]);
            }
        }
        System.out.println(list1.size());
        list1=removeDuplicateb(list1);
        policyNeo4jList=removeDuplicatea(policyNeo4jList);
        System.out.println(list1.size());
        for(int i=0;i<80;i++) {
            int num=0;
            for (int j = 0; j < policyNeo4jList.size(); j++) {
                String the = policyNeo4jList.get(j).getTheme();
                String name = policyNeo4jList.get(j).getPolicyname();
                if (!the.equals("") || the != null) {
                    if (the.indexOf(list1.get(i)) != -1&&num<3) {
                        policyNeo4jList2.add(policyNeo4jList.get(j));
                        list2.add(list1.get(i));
                        num+=1;
                    }
                }
            }
        }
        policyNeo4jList2=removeDuplicatea(policyNeo4jList2);
        list2=removeDuplicateb(list2);
        for(int i=0;i<list2.size();i++)
        {
            q1 = new JSONObject();
            q1.put("name", list2.get(i));
            q1.put("category", 0);
            array.add(q1);
        }
        for(int i=0;i<policyNeo4jList2.size();i++)
        {
            String the = policyNeo4jList2.get(i).getTheme();
            String name = policyNeo4jList2.get(i).getPolicyname();
            q1 = new JSONObject();
            q1.put("name", name);
            q1.put("category", 1);
            array.add(q1);
            for(int j=0;j<50;j++)
            {
                if (the.indexOf(list1.get(j)) != -1) {
                    q2 = new JSONObject();
                    q2.put("source", list1.get(j));
                    q2.put("target", name);
                    q2.put("name", "关联政策");
                    array2.add(q2);
                }
            }
        }
        q3.put("nodes",array);
        q3.put("links",array2);
        System.out.println(q3);
        return new FebsResponse().success().data(q3);
    }
    @GetMapping("getAtla")
    public FebsResponse getAtla() {
        JSONObject q1=new JSONObject();
        JSONObject q2=new JSONObject();
        JSONObject q3=new JSONObject();
        JSONArray array = new JSONArray();
        JSONArray array2 = new JSONArray();
        List<PolicyNeo4j> policyNeo4jList=policyNeo4jService.getPolicyNeo4jList();
        List<PolicyNeo4j> policyNeo4jList2=new ArrayList<>();
        List<String> list1=new ArrayList<>();
        List<String> list2=new ArrayList<>();
        List numsong=new ArrayList();
        List<String> list3=new ArrayList<>();
        List<Theme> themeList=themeService.getThemeList();
        for(int i=0;i<42;i++)
        {
            String thelist[]=themeList.get(i).getThirdname().split(" ");
            for(int j=0;j<thelist.length;j++)
            {
                list1.add(thelist[j]);
            }
        }
        list1=removeDuplicateb(list1);
        policyNeo4jList=removeDuplicatea(policyNeo4jList);
        for(int i=0;i<80;i++) {
            int size = policyNeo4jList.size();
            int num=0;
            for (int j = 0; j < policyNeo4jList.size(); j++) {
                String the = policyNeo4jList.get(j).getTheme();
                String name = policyNeo4jList.get(j).getPolicyname();
                if (!the.equals("") || the != null) {
                    if (the.indexOf(list1.get(i)) != -1&&num<3) {
                        policyNeo4jList2.add(policyNeo4jList.get(j));
                        list2.add(list1.get(i));
                        num+=1;
                    }
                }
            }
        }

        policyNeo4jList2=removeDuplicatea(policyNeo4jList2);
        list2=removeDuplicateb(list2);
        int[] sow=new int[list2.size()];
        for(int i=0;i<list2.size();i++)
        {
//            q1 = new JSONObject();
//            q1.put("name", list2.get(i));
//            q1.put("category", 0);
//            array.add(q1);
            sow[i]=20;
        }

        for(int i=0;i<policyNeo4jList2.size();i++)
        {
            String the = policyNeo4jList2.get(i).getTheme();
            String name = policyNeo4jList2.get(i).getPolicyname();
            q1 = new JSONObject();
            q1.put("name", name);
            q1.put("category", 1);
            array.add(q1);
            int num=0;
            String abc[]={"第一主题词","第二主题词","第三主题词","第四主题词"};
            for(int j=0;j<50;j++)
            {
                if (the.indexOf(list1.get(j)) != -1) {
                    for(int n=0;n<list2.size();n++)
                    {
                        if(list2.get(n).equals(list1.get(j)))
                        {
                            sow[n]=sow[n]+5;
                        }
                    }

                    q2 = new JSONObject();
//                    q2.put("source", list1.get(j));
//                    q2.put("target", name);
                    q2.put("source", name);
                    q2.put("target", list1.get(j));
                    q2.put("name", abc[num]);
                    num+=1;
                    array2.add(q2);
                }
            }
        }
        for(int i=0;i<list2.size();i++)
        {
            q1 = new JSONObject();
            q1.put("name", list2.get(i));
            q1.put("category", 0);
            q1.put("symbolSize",sow[i]);
            array.add(q1);
            sow[i]=20;
        }
        q3.put("nodes",array);
        q3.put("links",array2);
        System.out.println(q3);
        return new FebsResponse().success().data(q3);
    }
    @GetMapping("getpolicytheme/{policyName}")
    public FebsResponse getpolicytheme(@PathVariable String policyName) {
        System.out.println(policyName);
        PolicyNeo4j policyNeo4j=policyNeo4jService.getPolicyNeo4j(policyName);
        List<PolicyNeo4j> policyNeo4jList=policyNeo4jService.getPolicyNeo4jList();
        List<PolicyNeo4j> policyNeo4jList1=new ArrayList<>();
        List<PolicyNeo4j> policyNeo4jList2=new ArrayList<>();
        JSONObject q1=new JSONObject();
        JSONObject q3=new JSONObject();
        JSONArray array = new JSONArray();
        JSONArray array2 = new JSONArray();
        if(policyNeo4j.getTheme()!=null&&!policyNeo4j.getTheme().equals("")) {
            String[] theme = policyNeo4j.getTheme().split(" ");
            for(int i=0;i<theme.length;i++)
            {
                q1=new JSONObject();
                for(int j=0;j<policyNeo4jList.size();j++)
                {
                    String the=policyNeo4jList.get(j).getTheme();
                    if(the.indexOf(theme[i])!=-1)
                    {
                        policyNeo4jList1.add(policyNeo4jList.get(j));
                    }
                }
                q1.put("name",theme[i]);
                q1.put("category",0);
                array.add(q1);
            }
            System.out.println(policyNeo4jList1.size());
            policyNeo4jList2=removeDuplicatea(policyNeo4jList1);
            System.out.println(policyNeo4jList2.size());
            int num=0;
            for(int i=0;i<20;i++)
            {
                q1=new JSONObject();
                String name=policyNeo4jList2.get(i).getPolicyname();
                q1.put("name",name);
                q1.put("category",1);
                array.add(q1);
                String the=policyNeo4jList2.get(i).getTheme();
                for(int j=0;j<theme.length;j++)
                {
                    if(the.indexOf(theme[j])!=-1)
                    {
                        JSONObject q2=new JSONObject();
                        q2.put("source",theme[j]);
                        q2.put("target",name);
                        q2.put("name","关联政策");
                        array2.add(q2);
                    }
                }
            }
            List<String> stringList=new ArrayList<>();
            for(int i=0;i<20;i++)
            {
                String the=policyNeo4jList2.get(i).getTheme();
                String[] thelist=the.split(" ");

                System.out.println(the);
                for(int j=0;j<thelist.length;j++)
                {
                    int flag=0;
                    for(int m=0;m<theme.length;m++)
                    {
                        if(theme[m].equals(thelist[j]))
                        {
                            flag=1;
                        }
                    }
                    if(flag==0)
                    {
                        stringList.add(thelist[j]);
                    }
                }
            }
            stringList=removeDuplicateb(stringList);
            System.out.println("num:"+stringList.size());
            for(int i=0;i<stringList.size();i++)
            {
                q1=new JSONObject();
                q1.put("name",stringList.get(i));
                q1.put("category",2);
                array.add(q1);
                for(int j=0;j<20;j++)
                {
                    String the=policyNeo4jList2.get(j).getTheme();
                    if(the.indexOf(stringList.get(i))!=-1)
                    {
                        JSONObject q2=new JSONObject();
                        q2.put("source",policyNeo4jList2.get(j).getPolicyname());
                        q2.put("target",stringList.get(i));
                        q2.put("name","其他主题词");
                        array2.add(q2);
                    }
                }
            }
            q3.put("nodes",array);
            q3.put("links",array2);
        }
        return new FebsResponse().success().data(q3);
    }
    @GetMapping("getTreeNameTwo/{policyName}")
    public FebsResponse getTreeNameTwo(@PathVariable String policyName) {
        PolicyNeo4j policyNeo4j=policyNeo4jService.getPolicyNeo4j(policyName);
        List<NodeEntity> nodeEntityList=new ArrayList<NodeEntity>();
        JSONObject q1=new JSONObject();
        JSONObject q3=new JSONObject();
        list2=new ArrayList<>();
        q3=createNeo4j(policyNeo4j);
        System.out.println(q3);
        String highpolicy=policyNeo4j.getHighpolicy();
        String dept=policyNeo4j.getDept();
        String keyword=policyNeo4j.getKeyword();
        String year=policyNeo4j.getYear();
        String organ=policyNeo4j.getProvince();
        String theme=policyNeo4j.getTheme();
        String lowpolicy=policyNeo4j.getLowpolicy().trim();
        String similarpolicy=policyNeo4j.getSimilarpolicy().trim();
        q1.put("theme",theme);
        q1.put("highpolicy",highpolicy.trim());
        q1.put("lowpolicy",lowpolicy);
        q1.put("dept",dept);
        q1.put("keyword",keyword);
        q1.put("year",year);
        q1.put("organ",organ);
        q1.put("name",policyNeo4j.getPolicyname());
        q1.put("atlas",q3);
        q1.put("similarpolicy",similarpolicy);
        String text=policyNeo4j.getText();
        String subText=text.substring(0,200);
        q1.put("text",subText);
        return new FebsResponse().success().data(q1);
    }
    @GetMapping("getTreeName/{policyName}")
    public FebsResponse getTreeName(@PathVariable String policyName) {
        String a[]=policyName.split(",");
        String kind="";
        if(a[1].equals("0")||a[1].equals("1"))
        {
            kind="政策";
        }
        else if(a[1].equals("2"))
        {
            kind="部门";
        }
        else if(a[1].equals("3"))
        {
            kind="词汇";
        }
        else if(a[1].equals("4"))
        {
            kind="年份";
        }
        else if(a[1].equals("5"))
        {
            kind="地区";
        }
        List<NodeEntity> nodeEntityList=new ArrayList<NodeEntity>();
        Driver driver = GraphDatabase.driver( "bolt://localhost:7687", AuthTokens.basic( "neo4j", "751208liu" ) );
        Session session = driver.session();
        NodeEntity nodeEntity1=new NodeEntity();
        try {
            StatementResult result1 = session.run( "match (x:"+kind+") where x.name ='"+a[0]+"' return x.name as name,x.type as type" );
            StatementResult result = session.run( "match (x:"+kind+")-[*1]-(y) where x.name ='"+a[0]+"' return y.name as name,y.type as type" );
            List<Record> records = result.list();
            List<Record> records1 = result1.list();
            Record recordItem1=records1.get(0);
            String name1=recordItem1.get("name").toString().split("\"")[1];
            String type1=recordItem1.get("type").toString().split("\"")[1];
            nodeEntity1.setName(name1);
            nodeEntity1.setType(type1);
            for (Record recordItem : records) {
                NodeEntity nodeEntity=new NodeEntity();
                String name=recordItem.get("name").toString().split("\"")[1];
                String type=recordItem.get("type").toString().split("\"")[1];
                nodeEntity.setName(name);
                nodeEntity.setType(type);
                nodeEntityList.add(nodeEntity);

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
            driver.close();
        }
        JSONArray array = new JSONArray();
        JSONArray array2 = new JSONArray();
        JSONArray array3 = new JSONArray();
        JSONObject q1=new JSONObject();
        JSONObject q2=new JSONObject();
        JSONObject q3=new JSONObject();
        q1.put("name",nodeEntity1.getName());
        int cate=Integer.parseInt(a[1]);
        if(cate==1)
        {
            cate=0;
        }
        q1.put("category",cate);
        array.add(q1);
        for(int i=0;i<nodeEntityList.size();i++)
        {
            q1=new JSONObject();
            q2=new JSONObject();
            q1.put("name",nodeEntityList.get(i).getName());

            q2.put("source",nodeEntity1.getName());
            q2.put("target",nodeEntityList.get(i).getName());
            if(nodeEntityList.get(i).getType().equals("policy"))
            {
                q2.put("name","上位政策");
                q1.put("category",1);
            }
            else if(nodeEntityList.get(i).getType().equals("dept"))
            {
                q2.put("name","发布部门");
                q1.put("category",2);
            }
            else if(nodeEntityList.get(i).getType().equals("keyword"))
            {
                q2.put("name","关键词汇");
                q1.put("category",3);
            }
            else if(nodeEntityList.get(i).getType().equals("year"))
            {
                q2.put("name","发布时间");
                q1.put("category",4);
            }
            else
            {
                q2.put("name","实施范围");
                q1.put("category",5);
            }
            array.add(q1);
            array2.add(q2);
        }
        q3.put("nodes",array);
        q3.put("links",array2);
        System.out.println(q3);
        return new FebsResponse().success().data(q3);
    }
    public  JSONObject createNeo4j(PolicyNeo4j policyNeo4j)
    {
        JSONArray array = new JSONArray();
        JSONArray array2 = new JSONArray();
        JSONArray array3 = new JSONArray();
        JSONObject q1=new JSONObject();
        JSONObject q2=new JSONObject();
        JSONObject q3=new JSONObject();
        q1.put("name",policyNeo4j.getPolicyname());
        int cate=0;
        q1.put("category",cate);
        array.add(q1);
        if(policyNeo4j.getDept()!=null&&!policyNeo4j.getDept().equals("")) {
            String dept[] = policyNeo4j.getDept().split(" ");
            for (int i = 0; i < dept.length; i++) {
                q1 = new JSONObject();
                q2 = new JSONObject();
                q1.put("name", dept[i]);
                q2.put("source", policyNeo4j.getPolicyname());
                q2.put("target", dept[i]);
                q2.put("name", "发布部门");
                q1.put("category", 2);
                array.add(q1);
                array2.add(q2);
            }
        }
        if(policyNeo4j.getYear()!=null&&!policyNeo4j.getYear().equals("")) {
            String year[] = policyNeo4j.getYear().split(" ");
            for (int i = 0; i < year.length; i++) {
                q1 = new JSONObject();
                q2 = new JSONObject();
                q1.put("name", year[i]);
                q2.put("source", policyNeo4j.getPolicyname());
                q2.put("target", year[i]);
                q2.put("name", "发布时间");
                q1.put("category", 4);
                array.add(q1);
                array2.add(q2);
            }
        }
        if(policyNeo4j.getKeyword()!=null&&!policyNeo4j.getKeyword().equals("")) {
            String keyword[] = policyNeo4j.getKeyword().split(" ");
            for (int i = 0; i < keyword.length; i++) {
                q1 = new JSONObject();
                q2 = new JSONObject();
                q1.put("name", keyword[i]);
                q2.put("source", policyNeo4j.getPolicyname());
                q2.put("target", keyword[i]);
                q2.put("name", "关键词汇");
                q1.put("category", 3);
                array.add(q1);
                array2.add(q2);
            }
        }
        if(policyNeo4j.getProvince()!=null&&!policyNeo4j.getProvince().equals("")) {
            String province[] = policyNeo4j.getProvince().split(" ");
            for (int i = 0; i < province.length; i++) {
                q1 = new JSONObject();
                q2 = new JSONObject();
                q1.put("name", province[i]);
                q2.put("source", policyNeo4j.getPolicyname());
                q2.put("target", province[i]);
                q2.put("name", "实施范围");
                q1.put("category", 5);
                array.add(q1);
                array2.add(q2);
            }
        }
        if(policyNeo4j.getHighpolicy()!=null&&!policyNeo4j.getHighpolicy().equals("")) {
            int numhigh = 0;
            String highpolicy[] = policyNeo4j.getHighpolicy().split(" ");
            List<String> list=quchong(highpolicy);
            for (int i = 0; i < list.size(); i++) {
                if (numhigh < 5) {
                    q1 = new JSONObject();
                    q2 = new JSONObject();
                    q1.put("name", list.get(i));
                    q2.put("source", policyNeo4j.getPolicyname());
                    q2.put("target", list.get(i));
                    q2.put("name", "上位政策");
                    q1.put("category", 1);
                    array.add(q1);
                    array2.add(q2);
                    numhigh += 1;
                }
            }
        }
        if(policyNeo4j.getLowpolicy()!=null&&!policyNeo4j.getLowpolicy().equals("")) {
            int numlow = 0;
            String lowpolicy[] = policyNeo4j.getLowpolicy().split(" ");
            List<String> list=quchong(lowpolicy);
            for (int i = 0; i < list.size(); i++) {
                if (numlow < 5) {
                    q1 = new JSONObject();
                    q2 = new JSONObject();
                    q1.put("name", list.get(i));
                    q2.put("source", policyNeo4j.getPolicyname());
                    q2.put("target", list.get(i));
                    q2.put("name", "下位政策");
                    q1.put("category", 6);
                    array.add(q1);
                    array2.add(q2);
                    numlow += 1;
                }
            }
        }
        if(policyNeo4j.getSimilarpolicy()!=null&&!policyNeo4j.getSimilarpolicy().equals("")) {
            int numsim = 0;
            String simpolicy[] = policyNeo4j.getSimilarpolicy().split(" ");
            List<String> list=quchong(simpolicy);
            for (int i = 0; i < list.size(); i++) {
                if (numsim < 5) {
                    q1 = new JSONObject();
                    q2 = new JSONObject();
                    q1.put("name", list.get(i));
                    q2.put("source", policyNeo4j.getPolicyname());
                    q2.put("target", list.get(i));
                    q2.put("name", "相似政策");
                    q1.put("category", 7);
                    array.add(q1);
                    array2.add(q2);
                    numsim += 1;
                }
            }
        }
        q3.put("nodes",array);
        q3.put("links",array2);
        return q3;
    }
    public static JSONObject createNeo4jJson(List<NodeEntity> nodeEntityList,PolicyNeo4j policyNeo4j)
    {
        JSONArray array = new JSONArray();
        JSONArray array2 = new JSONArray();
        JSONArray array3 = new JSONArray();
        JSONObject q1=new JSONObject();
        JSONObject q2=new JSONObject();
        JSONObject q3=new JSONObject();
        q1.put("name",policyNeo4j.getPolicyname());
        int cate=0;
        q1.put("category",cate);
        array.add(q1);
        for(int i=0;i<nodeEntityList.size();i++)
        {
            q1=new JSONObject();
            q2=new JSONObject();
            q1.put("name",nodeEntityList.get(i).getName());

            q2.put("source",policyNeo4j.getPolicyname());
            q2.put("target",nodeEntityList.get(i).getName());
            if(nodeEntityList.get(i).getType().equals("policy"))
            {
                q2.put("name","上位政策");
                q1.put("category",1);
            }
            else if(nodeEntityList.get(i).getType().equals("dept"))
            {
                q2.put("name","发布部门");
                q1.put("category",2);
            }
            else if(nodeEntityList.get(i).getType().equals("keyword"))
            {
                q2.put("name","关键词汇");
                q1.put("category",3);
            }
            else if(nodeEntityList.get(i).getType().equals("year"))
            {
                q2.put("name","发布时间");
                q1.put("category",4);
            }
            else
            {
                q2.put("name","实施范围");
                q1.put("category",5);
            }
            array.add(q1);
            array2.add(q2);

        }
        q3.put("nodes",array);
        q3.put("links",array2);
        return q3;
    }
    public  List<String> quchong(String a[])
    {
        List<String> list=new ArrayList<>();
        System.out.println(list2.size());
        for(int i=0;i<a.length;i++)
        {
            if(list2.size()==0)
            {
                list2.add(a[i]);
            }
            else
            {
                int flag=0;
                for(int j=0;j<list2.size();j++)
                {
                    if(a[i].equals(list2.get(j)))
                    {
                        flag=1;
                    }
                }
                if(flag==0)
                {
                    list2.add(a[i]);
                    list.add(a[i]);
                }
            }
        }
        System.out.println(list);
        return list;
    }
    public   static   List<PolicyNeo4j>  removeDuplicatea(List<PolicyNeo4j> list)  {
        for  ( int  i  =   0 ; i  <  list.size()  -   1 ; i ++ )  {
            for  ( int  j  =  list.size()  -   1 ; j  >  i; j -- )  {
                if  (list.get(j).getPolicyname().equals(list.get(i).getPolicyname()))  {
                    list.remove(j);
                }
            }
        }
        return list;
    }
    public   static   List  removeDuplicateb(List list)  {
        for  ( int  i  =   0 ; i  <  list.size()  -   1 ; i ++ )  {
            for  ( int  j  =  list.size()  -   1 ; j  >  i; j -- )  {
                if  (list.get(j).equals(list.get(i)))  {
                    list.remove(j);
                }
            }
        }
        return list;
    }
}
