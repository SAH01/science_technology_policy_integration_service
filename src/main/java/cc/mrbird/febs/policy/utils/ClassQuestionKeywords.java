package cc.mrbird.febs.policy.utils;

import java.util.List;

public class ClassQuestionKeywords {
    private static String[] ClassFirst = {"多少","数量","哪些","有关"};
    private static String[] ClassSecond = {"时候","时间","哪一年"};
    private static String[] ClassThird = {"单位","部门"};
    private static String[] ClassForth={"措施","方面","方法"};

    public int ClassJudge(List<String> noun)
    {
        int flag=-1;
        for(int i=0;i<noun.size();i++)
        {
            String key=noun.get(i);
            for(int j=0;j<ClassFirst.length;j++)
            {
                if(key.equals(ClassFirst[j]))
                {
                    flag=1;
                }
            }
            for(int j=0;j<ClassSecond.length;j++)
            {
                if(key.equals(ClassSecond[j]))
                {
                    flag=2;
                }
            }
            for(int j=0;j<ClassThird.length;j++)
            {
                if(key.equals(ClassThird[j]))
                {
                    flag=3;
                }
            }
            for(int j=0;j<ClassForth.length;j++)
            {
                if(key.equals(ClassForth[j]))
                {
                    flag=4;
                }
            }
        }
        return flag;
    }
}
