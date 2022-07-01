package cc.mrbird.febs.policy.utils;

import cc.mrbird.febs.policy.entity.Policy;
import cc.mrbird.febs.policy.entity.Theme;
import com.alibaba.fastjson.JSONObject;

import java.util.*;

public class CreateTableJson {
    public static JSONObject getPolicyInstrumentTableJsonData(List<Policy> policylist,List<Theme> themeList)
    {
        List<String> the=new ArrayList<String>();
        for(int i=0;i<policylist.size();i++)
        {
            String theme=policylist.get(i).getTheme();
            if(theme!=null&&!theme.equals("")) {
                String a[] = theme.split(" ");
                for (int j = 0; j < a.length; j++) {
                    if (a[j] != null && !a[j].equals("")) {
                        the.add(a[j]);
                    }
                }
            }
        }
        Map<String, Integer> wordFrequentMap = new HashMap<>();
        for(int i=0;i<the.size();i++)
        {
            if (wordFrequentMap.containsKey(the.get(i))) {
                wordFrequentMap.put(the.get(i), wordFrequentMap.get(the.get(i)) + 1);
            } else {
                wordFrequentMap.put(the.get(i), 1);
            }
        }
        List<Map.Entry<String, Integer>> list = new ArrayList<>(wordFrequentMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            //升序排序
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        List<String>[] keywords = new ArrayList[42];
        int[] frequency = new int[42];
        int[] group_frequency = new int[13];
        double[] proportion = new double[42];
        double[] group_proportion = new double[13];
        for (int i = 0; i < keywords.length; i++) {
            keywords[i] = new ArrayList<>();
            String a[]=themeList.get(i).getThirdname().split(" ");
            for(int j=0;j<a.length;j++)
            {
                keywords[i].add(a[j]);
            }
            frequency[i]=0;
        }
        for (Map.Entry<String, Integer> e : list) {
            String word = e.getKey();
            int num=e.getValue();
            for(int i=0;i<42;i++)
            {
                String third=themeList.get(i).getThirdname();
                if(third.indexOf(word)!=-1)
                {
                   // keywords[i].add(word);
                    frequency[i] += e.getValue();
                }
            }
        }
        for (int i = 0; i < 42; i++) {
            group_frequency[getInstrumentFirsrIndexBySecondIndex(i)] += frequency[i];
        }
        for (int i = 0; i < 42; i++) {
            proportion[i] = (double) frequency[i] / group_frequency[getInstrumentFirsrIndexBySecondIndex(i)];
        }
        int words_num = 0;
        for (int i = 0; i < 13; i++) {
            words_num += group_frequency[i];
        }
        for (int i = 0; i < 13; i++) {
            group_proportion[i] = (double) group_frequency[i] / words_num;
        }

        JSONObject json = new JSONObject();
        json.put("keywords", keywords);
        json.put("frequency", frequency);
        json.put("group_frequency", group_frequency);
        json.put("proportion", proportion);
        json.put("group_proportion", group_proportion);
        json.put("words_num", words_num);
        return json;
    }
    public static int getInstrumentFirsrIndexBySecondIndex(int secondIindex) {
        if (secondIindex == -1) {
            return -1;
        }
        if (secondIindex < 2) {
            return 0;
        } else if (secondIindex < 6) {
            return 1;
        } else if (secondIindex < 9) {
            return 2;
        }
        else if (secondIindex < 13) {
            return 3;
        }else if (secondIindex < 15) {
            return 4;
        }else if (secondIindex < 17) {
            return 5;
        }else if (secondIindex < 19) {
            return 6;
        }else if (secondIindex < 22) {
            return 7;
        }else if (secondIindex < 27) {
            return 8;
        }else if (secondIindex < 32) {
            return 9;
        }else if (secondIindex < 33) {
            return 10;
        }else if (secondIindex < 35) {
            return 11;
        }else{
            return 12;
        }
    }
}
