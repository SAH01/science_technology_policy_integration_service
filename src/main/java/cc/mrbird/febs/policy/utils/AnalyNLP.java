package cc.mrbird.febs.policy.utils;

import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AnalyNLP {
    public static Date NLP(String text)throws ParseException
    {
        Date date=null;
        int n=5;
        List<Term> termList = NLPTokenizer.segment(text);
        for(Term term : termList)
        {
           n=isValidDate(term.word);
           if(n==1)
           {
               Date d1 = new SimpleDateFormat("yyyy/MM/dd").parse(term.word);//定义起始日期
               SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy");
               SimpleDateFormat sdf1 = new SimpleDateFormat("MM");
               SimpleDateFormat sdf2= new SimpleDateFormat("dd");
               String str1 = sdf0.format(d1);
               String str2 = sdf1.format(d1);
               String str3 = sdf2.format(d1);
               String str=str1+"-"+str2+"-"+str3;
               SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
               date = sdf.parse(str);
           }
           if(n==2)
           {
               SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
               date = sdf.parse(term.word);
           }
           if(n==3)
           {
               Date d1 = new SimpleDateFormat("yyyy年MM月dd日").parse(term.word);//定义起始日期
               SimpleDateFormat sdf0 = new SimpleDateFormat("yyyy");
               SimpleDateFormat sdf1 = new SimpleDateFormat("MM");
               SimpleDateFormat sdf2= new SimpleDateFormat("dd");
               String str1 = sdf0.format(d1);
               String str2 = sdf1.format(d1);
               String str3 = sdf2.format(d1);
               String str=str1+"-"+str2+"-"+str3;
               SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
               date = sdf.parse(str);
           }
        }
        return date;
    }
    public static int isValidDate(String str) {
        int n1=1;
        int n2=2;
        int n3=3;
        SimpleDateFormat format= new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat formattwo=new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatthree=new SimpleDateFormat("yyyy年MM月dd日");
        try {
            //format.setLenient(false);
            format.parse(str);
        } catch (ParseException e) {
            n1=0;
        }
        try {
            //format.setLenient(false);
            formattwo.parse(str);
        } catch (ParseException e) {
            n2=0;
        }
        try {
            //format.setLenient(false);
            formatthree.parse(str);
        } catch (ParseException e) {
            n3=0;
        }
        if(n1==1)
        {
            return 1;
        }
        else if(n2==2)
        {
            return 2;
        }
        else if(n3==3)
        {
            return 3;
        }
       else
        {
            return 0;
        }
    }


}
