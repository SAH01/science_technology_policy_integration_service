package cc.mrbird.febs.policy.utils;

import com.hankcs.hanlp.mining.word2vec.DocVectorModel;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;

import java.io.IOException;

/**
 * 简单粗暴的关键词相似度计算算法
 */
public class Levenshtein
{
    //hanlp的分词方法
    public float getSimilarity(String str, String target){
        try{
            DocVectorModel docVectorModel = new DocVectorModel(new WordVectorModel("data/msr_vectors.txt"));
            return docVectorModel.similarity(str, target);
        }catch (IOException e){
            System.out.println("未找到模型");
            e.getMessage();
            return 0;
        }
    }

    private int compare(String str, String target)
    {
        int d[][];              // 矩阵
        int n = str.length();
        int m = target.length();
        int i;                  // 遍历str的
        int j;                  // 遍历target的
        char ch1;               // str的
        char ch2;               // target的
        int temp;               // 记录相同字符,在某个矩阵位置值的增量,不是0就是1
        if (n == 0) { return m; }
        if (m == 0) { return n; }
        d = new int[n + 1][m + 1];
        for (i = 0; i <= n; i++)
        {                       // 初始化第一列
            d[i][0] = i;
        }

        for (j = 0; j <= m; j++)
        {                       // 初始化第一行
            d[0][j] = j;
        }

        for (i = 1; i <= n; i++)
        {                       // 遍历str
            ch1 = str.charAt(i - 1);
            // 去匹配target
            for (j = 1; j <= m; j++)
            {
                ch2 = target.charAt(j - 1);
                if (ch1 == ch2 || ch1 == ch2+32 || ch1+32 == ch2)
                {
                    temp = 0;
                } else
                {
                    temp = 1;
                }
                // 左边+1,上边+1, 左上角+temp取最小
                d[i][j] = min(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + temp);
            }
        }
        return d[n][m];
    }

    private int min(int one, int two, int three)
    {
        return (one = one < two ? one : two) < three ? one : three;
    }

    /**
     * 获取两字符串的相似度
     */

    public float getSimilarityRatio(String str, String target)
    {
        return 1 - (float) compare(str, target) / Math.max(str.length(), target.length());
    }

    public static void main(String[] args)
    {
        Levenshtein lt = new Levenshtein();
        String str = "中国人说中文";
        String target = "中文";
//        System.out.println("similarityRatio=" + lt.getSimilarityRatio(str, target));
//        System.out.println(lt.getSimilarity(" 北京市人民政府关于2018年度北京市科学技术奖励的决定","关于进一步促进中关村知识产权质押融资发展的若干措施 "));
//        System.out.println(lt.getSimilarity(" 中共北京市委 北京市人民政府关于印发《北京市大力营造企业家创新创业环境充分激发和弘扬优秀企业家精神若干措施》的通知","中共北京市委 北京市人民政府印发《关于落实农业农村优先发展扎实推进乡村振兴战略实施的工作方案》的通知"));
        System.out.println(lt.getSimilarity("中关村科技园区管理委员会印发《关于精准支持中关村国家自主创新示范区重大前沿项目与创新平台建设的若干措施》的通知 ","关于印发《〈关于精准支持中关村国家自主创新示范区重大前沿项目与创新平台建设的若干措施〉实施办法（试行）》的通知 "));
    }
}