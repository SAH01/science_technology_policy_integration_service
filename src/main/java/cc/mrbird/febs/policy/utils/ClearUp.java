package cc.mrbird.febs.policy.utils;

import cc.mrbird.febs.policy.entity.*;
import cc.mrbird.febs.policy.service.IPolicy2Service;
import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ClearUp {
    @Autowired
    private IPolicy2Service policy2Service;
    public PolicyEvo ClearUpEvolutionList(List<PolicyNewAnalysis> policyEvolist,String name)
    {
        PolicyEvo policyEvo=new PolicyEvo();
        String policymain="";
        String policytext="";
        String keyword=policyEvolist.get(0).getPolicykey();
        String policyname=name;
        String policykey="";
        for(int i=0;i<policyEvolist.size();i++)
        {
            String a[]=keyword.split(" ");
            String text=policyEvolist.get(i).getPolicytext();
            String key="";
            if(a.length>0)
            {
                for (int j = 0; j < a.length; j++) {
                    if (text.indexOf(a[j]) != -1) {
                        key=key+" "+a[j];
                        text = text.replace(a[j], "<b style='color:red'>" + a[j] + "</b>");
                    }
                }
            }
            if(i==0)
            {
                policytext=policytext+text;
                policykey=policykey+key;
                policymain=policymain+policyEvolist.get(i).getPolicymatters();
            }
            else
            {
                policytext=policytext+"|"+text;
                policykey=policykey+"|"+key;
                policymain=policymain+"|"+policyEvolist.get(i).getPolicymatters();
            }
        }
        policyEvo.setPolicyname(policyname);
        policyEvo.setPolicymain(policymain);
        policyEvo.setPolicytext(policytext);
        policyEvo.setKeyword(policykey);


        return policyEvo;
    }
    public PolicyEvo ClearUpEvolution(PolicyEvo policyEvo)
    {
        PolicyEvo policyEvo1=new PolicyEvo();
        String text=policyEvo.getPolicytext();
        String a[]=text.split("\n");
        String policytext="";
        String key="";
        String keyword=policyEvo.getKeyword();
        String keylist[]=keyword.split(" ");
        for(int i=0;i<a.length;i++)
        {

            String duan=a[i];
            String res="";
            if(keylist.length>0) {
                for (int j = 0; j < keylist.length; j++) {
                    if (duan.indexOf(keylist[j]) != -1) {
                        res=res+" "+keylist[j];
                        duan = duan.replace(keylist[j], "<b style='color:red'>" + keylist[j] + "</b>");
                    }
                }
            }

            String result=res.trim();
            if(i==0)
            {
                policytext=policytext+duan;
                key=key+result;
            }
            else
            {
                policytext=policytext+"|"+duan;
                key=key+"|"+result;
            }

        }
        policyEvo1=policyEvo;
        policyEvo1.setPolicytext(policytext);
        policyEvo1.setKeyword(key);
        return policyEvo1;
    }
    public PolicyContrast ClearUpContrast2(List<PolicyContrast> list)
    {
        PolicyContrast policyContrast=new PolicyContrast();
        String policymain="";
        String kind="";
        String policykey="";
        for(int i=0;i<list.size();i++)
        {
            String key[]=list.get(i).getPolicykey().split(" ");
            String policytext=list.get(i).getPolicymain();
            if(key.length>1) {
                for (int j = 1; j < key.length; j++) {
                    if (policytext.indexOf(key[j]) != -1) {
                        policytext = policytext.replace(key[j], "<b style='color:red'>" + key[j] + "</b>");
                    }
                }
            }
            if(i==0)
            {
                policymain=policymain+policytext;
                kind=kind+list.get(i).getKind();
                policykey=policykey+list.get(i).getPolicykey();
            }
            else
            {
                policymain=policymain+"|"+policytext;
                kind=kind+"|"+list.get(i).getKind();
                policykey=policykey+"|"+list.get(i).getPolicykey();
            }
        }
        policyContrast.setKind(kind);
        policyContrast.setPolicymain(policymain);
        policyContrast.setPolicykey(policykey);
        return policyContrast;
    }
    public PolicyContrast ClearUpContrast(List<PolicyContrast> list,List<Policy> listpolicy)
    {
        String[] province=new String[] {"北京","天津","河北","山西","内蒙古","辽宁","吉林","黑龙江","上海","江苏","浙江","安徽","福建","江西","山东","河南","湖北","湖南","广东","广西","海南","重庆","四川","贵州","云南"
                ,"西藏","陕西","甘肃","青海","宁夏","新疆","台湾","香港","澳门"};
        PolicyContrast policyContrast=new PolicyContrast();
        String policyname="";
        String policydept="";
        String policymain="";
        String policykey="";
        String policytime="";
        int[] num=new int[province.length];
        for(int i=0;i<num.length;i++)
        {
            num[i]=0;
        }
        for(int i=0;i<listpolicy.size();i++)
        {
            String text = listpolicy.get(i).getText();
            String dept = listpolicy.get(i).getOrgan();
            int flag = 0;
            for (int j = 1; j < province.length; j++) {
                if (dept.indexOf(province[j]) != -1 && flag == 0) {
                    num[j]=num[j]+1;
                    flag = 1;
                }
            }
            if (flag == 0) {
                for (int j = 1; j < province.length; j++) {
                    if (text.indexOf(province[j]) != -1 && flag == 0) {
                        num[j]=num[j]+1;
                        flag = 1;
                    }
                }
            }
        }
        String all="";
        for(int j = 1; j < province.length; j++)
        {
            if(num[j]!=0)
            {
                all=all+province[j]+":"+num[j]+"篇;";
            }
        }
        for(int i=0;i<list.size();i++)
        {
            String key[]=list.get(i).getPolicykey().split(" ");
            String policytext=list.get(i).getPolicymain();
            if(key.length>1) {
                for (int j = 0; j < key.length; j++) {
                    if (policytext.indexOf(key[j]) != -1) {
                        policytext = policytext.replace(key[j], "<b style='color:red'>" + key[j] + "</b>");
                    }
                }
            }
            if(i==0)
            {
                policyname=policyname+list.get(i).getPolicyname();
                policydept=policydept+list.get(i).getPolicydept();
                policymain=policymain+policytext;
                policykey=policykey+list.get(i).getPolicykey();
                policytime=policytime+list.get(i).getPolicytime();
            }
            else
            {
                policyname=policyname+"|"+list.get(i).getPolicyname();
                policydept=policydept+"|"+list.get(i).getPolicydept();
                policymain=policymain+"|"+policytext;
                policykey=policykey+"|"+list.get(i).getPolicykey();
                policytime=policytime+"|"+list.get(i).getPolicytime();
            }
        }
        policyContrast.setPolicytime(policytime);
        policyContrast.setPolicydept(policydept);
        policyContrast.setPolicymain(policymain);
        policyContrast.setPolicyname(policyname);
        policyContrast.setAllsum(all);
        policyContrast.setKind(list.get(0).getParentname()+"--"+list.get(0).getKind());
        policyContrast.setPolicykey(policykey);
        return policyContrast;
    }
    public PolicyEvo ClearUpPolicyEvolution(List<PolicyEvo> list,String type) throws ParseException
    {
        PolicyEvo policyevo=new PolicyEvo();
        String policytime="";;
        String policymain="";
        String policyname="";
        String policydept="";
        String highpolicy="";
        String lowpolicy="";
        String policytext="";
        List<String> keyword=shisi(type);
        for(int i=0;i<list.size();i++)
        {
            String text=list.get(i).getPolicytext2();
            for(int j=0;j<keyword.size();j++)
            {
                if(text.indexOf(keyword.get(j))!=-1)
                {
                    text = text.replace(keyword.get(j), "<b style='color:red'>" + keyword.get(j) + "</b>");
                }
            }
            if(i==0)
            {
                //policytext=policytext+text;
                highpolicy=highpolicy+list.get(i).getHighpolicy();
                lowpolicy=lowpolicy+list.get(i).getLowpolicy();
                policytime=policytime+list.get(i).getPolicytime();
                policymain=policymain+list.get(i).getPolicymain();
                policyname=policyname+list.get(i).getPolicyname();
                policydept=policydept+list.get(i).getPolicydept();
                policytext=policytext+text;
            }
            else
            {
                highpolicy=highpolicy+"|"+list.get(i).getHighpolicy();
                lowpolicy=lowpolicy+"|"+list.get(i).getLowpolicy();
                policytime=policytime+"|"+list.get(i).getPolicytime();
                policymain=policymain+"|"+list.get(i).getPolicymain();
                policyname=policyname+"|"+list.get(i).getPolicyname();
                policydept=policydept+"|"+list.get(i).getPolicydept();
                policytext=policytext+"|"+text;
            }
        }
        String a[]=policymain.split("/");
        System.out.println(a.length);
        policyevo.setPolicyname(policyname);
        policyevo.setPolicymain(policymain);
        policyevo.setHighpolicy(highpolicy);
        policyevo.setPolicydept(policydept);
        policyevo.setLowpolicy(lowpolicy);
        policyevo.setPolicytime(policytime);
        policyevo.setPolicytype(type);
        policyevo.setPolicytext2(policytext);
        return policyevo;
    }

    public Policyan ClearUpPolicy(List<Policyan> list,Policy policy)
    {
        Policyan policyan=new Policyan();
        String policytext="";
        String policydept="";
        String policyorgan="";
        String policymain="";
        String policymatters="";
        String policyname="";
        String policykey="";
        for(int i=0;i<list.size();i++)
        {
            String text=list.get(i).getPolicytext();
            String key[]=list.get(i).getPolicykey().split(" ");
            if(key.length>1) {
                for (int j = 1; j < key.length; j++) {
                    if (text.indexOf(key[j]) != -1) {
                        text = text.replace(key[j], "<b style='color:red'>" + key[j] + "</b>");
                    }
                }
            }


            if(i==0)
            {
                policymatters=policymatters+list.get(i).getPolicymatters();
                policytext=policytext+text;;
                policydept=policydept+list.get(i).getPolicydept();
                policyorgan=policyorgan+list.get(i).getPolicyorgan();
                policymain=policymain+list.get(i).getPolicymain();
                policykey=policykey+list.get(i).getPolicykey();
            }
            else
            {
                policymatters=policymatters+"|"+list.get(i).getPolicymatters();
                policytext=policytext+"|"+text;;
                policydept=policydept+"|"+list.get(i).getPolicydept();
                policyorgan=policyorgan+"|"+list.get(i).getPolicyorgan();
                policymain=policymain+"|"+list.get(i).getPolicymain();
                policykey=policykey+"|"+list.get(i).getPolicykey();
            }
        }
        policyan.setPolicydept(policydept);
        policyan.setPolicykey(policykey);
        policyan.setPolicymain(policymain);
        policyan.setPolicymatters(policymatters);
        policyan.setPolicyname(list.get(0).getPolicyname());
        policyan.setPolicyorgan(policyorgan);
        policyan.setPolicytext(policytext);
        policyan.setKeyword(policy.getKeyword());
        policyan.setOrgan(policy.getOrgan());
        return policyan;
    }

    public PolicyAnalysis ClearUpPolicyAnalysis(List<PolicyAnalysis> list)
    {
        PolicyAnalysis policyanalysis=new PolicyAnalysis();
        String policytext="";
        String policydept="";
        String policyorgan="";
        String policymain="";
        String policymatters="";
        String policykey="";
        String similarkey="";
        String policyname="";
        String similartext="";
        String similardept="";
        String similarorgan="";
        String similarmain="";
        String similarname="";
        String one="";
        String two="";
        for(int i=0;i<list.size();i++)
        {
            String texta=list.get(i).getPolicytext();
            String textb=list.get(i).getSimilartext();
            String keyb[]=list.get(i).getSimilarkey().split(" ");
            String keya[]=list.get(i).getPolicykey().split(" ");
            if(keya.length>1) {
                for (int j = 1; j < keya.length; j++) {
                    if (texta.indexOf(keya[j]) != -1) {
                        texta = texta.replace(keya[j], "<b style='color:red'>" + keya[j] + "</b>");
                    }
                }
            }
            if(keyb.length>1) {
                for (int j = 1; j < keyb.length; j++) {
                    if (textb.indexOf(keyb[j]) != -1) {
                        textb = textb.replace(keyb[j], "<b style='color:red'>" + keyb[j] + "</b>");
                    }
                }
            }
            if(i==0)
            {
                policymatters=policymatters+list.get(i).getPolicymatters();
                policytext=policytext+texta;
                policydept=policydept+list.get(i).getPolicydept();
                policyorgan=policyorgan+list.get(i).getPolicyorgan();
                policykey=policykey+list.get(i).getPolicykey();
                policymain=policymain+list.get(i).getPolicymain();
                policyname=policyname+list.get(i).getPolicyname();
                similardept=similardept+list.get(i).getSimilardept();
                similarmain=similarmain+list.get(i).getSimilarmain();
                similarname=similarname+list.get(i).getSimilarname();
                similartext=similartext+textb;
                similarorgan=similarorgan+list.get(i).getSimilarorgan();
                similarkey=similarkey+list.get(i).getSimilarkey();
                one=one+list.get(i).getOne();
                two=two+list.get(i).getTwo();
            }
            else
            {
                policymatters=policymatters+"|"+list.get(i).getPolicymatters();
                policytext=policytext+"|"+texta;
                policydept=policydept+"|"+list.get(i).getPolicydept();
                policyorgan=policyorgan+"|"+list.get(i).getPolicyorgan();
                policymain=policymain+"|"+list.get(i).getPolicymain();
                policykey=policykey+"|"+list.get(i).getPolicykey();
                policyname=policyname+"|"+list.get(i).getPolicyname();
                similardept=similardept+"|"+list.get(i).getSimilardept();
                similarmain=similarmain+"|"+list.get(i).getSimilarmain();
                similarname=similarname+"|"+list.get(i).getSimilarname();
                similartext=similartext+"|"+textb;
                similarorgan=similarorgan+"|"+list.get(i).getSimilarorgan();
                similarkey=similarkey+"|"+list.get(i).getSimilarkey();
                one=one+"|"+list.get(i).getOne();
                two=two+"|"+list.get(i).getTwo();
            }
        }
        policyanalysis.setPolicydept(policydept);
        policyanalysis.setPolicymain(policymain);
        policyanalysis.setPolicymatters(policymatters);
        policyanalysis.setPolicyname(policyname);
        policyanalysis.setPolicyorgan(policyorgan);
        policyanalysis.setPolicytext(policytext);
        policyanalysis.setSimilardept(similardept);
        policyanalysis.setSimilarmain(similarmain);
        policyanalysis.setSimilarname(similarname);
        policyanalysis.setSimilarorgan(similarorgan);
        policyanalysis.setSimilartext(similartext);
        policyanalysis.setPolicykey(policykey);
        policyanalysis.setSimilarkey(similarkey);
        policyanalysis.setOne(one);
        policyanalysis.setTwo(two);
        return policyanalysis;
    }
    public static void NLP(String text)
    {
        System.out.println(NLPTokenizer.segment(text));
    }
    public static List<String> NLPkey(String text)
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
//        for(int i=0;i<list.size();i++)
//        {
//            keyword=keyword+" "+list.get(i);
//        }
//        String result=keyword.trim();
        return list;
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



}
