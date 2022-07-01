package cc.mrbird.febs.policy.helper.classifyTest;

import com.hankcs.hanlp.mining.word2vec.DocVectorModel;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class KeywordsClassify {
    public static void main(String[] args) throws IOException {

        DocVectorModel docVectorModel = new DocVectorModel(new WordVectorModel("data/msr_vectors.txt"));
        String[] documents = new String[]{
                "山东苹果丰收",
                "农民在江苏种水稻",
                "奥运会女排夺冠",
                "世界锦标赛胜出",
                "中国足球失败",
        };

        for (int i = 0; i < documents.length; i++)
        {
            docVectorModel.addDocument(i, documents[i]);
        }

        System.out.println("============体育=============");
        List<Map.Entry<Integer, Float>> entryList = docVectorModel.nearest("体育");
        for (Map.Entry<Integer, Float> entry : entryList)
        {
            System.out.printf("%d %s %.2f\n", entry.getKey(), documents[entry.getKey()], entry.getValue());
        }

        System.out.println("============农业=============");
        entryList = docVectorModel.nearest("农业");
        for (Map.Entry<Integer, Float> entry : entryList)
        {
            System.out.printf("%d %s %.2f\n", entry.getKey(), documents[entry.getKey()], entry.getValue());
        }
    }
}
