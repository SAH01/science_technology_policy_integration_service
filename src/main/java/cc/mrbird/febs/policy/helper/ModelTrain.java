package cc.mrbird.febs.policy.helper;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.mining.word2vec.Word2VecTrainer;
import com.hankcs.hanlp.mining.word2vec.WordVectorModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ModelTrain {

    public static void main(String args[]){
        ModelTrain modelTrain =new ModelTrain();
        //modelTrain.train();
        modelTrain.prepareCorpus();
        WordVectorModel wordVectorModel = null;
        try {
            wordVectorModel = new WordVectorModel("data/msr_vectors.txt");
//            String[] list={"工业设计" ,
//                    "研发服务" ,
//                    "研发设计" ,
//                    "研究开发" ,
//                    "成果转化" ,
//                    "技术服务" ,
//                    "技术交易" ,
//                    "技术市场" ,
//                    "技术转移" ,
//                    "检验检测认证" ,
//                    "测试" ,
//                    "计量" ,
//                    "创业孵化" ,
//                    "孵化基地" ,
//                    "知识产权保护" ,
//                    "知识产权服务" ,
//                    "知识产权管理" ,
//                    "知识产权交易" ,
//                    "知识产权运营" ,
//                    "科技咨询" ,
//                    "管理咨询" ,
//                    "技术咨询" ,
//                    "科技评估" ,
//                    "科技金融" ,
//                    "质押融资" ,
//                    "产权融资" ,
//                    "股权融资" ,
//                    "科普" ,
//                    "科学技术普及" ,
//                    "科技中介" ,
//                    "增值服务" ,
//                    "中介服务" ,
//                    "产业集群" ,
//                    "高技术服务业" ,
//                    "战略性新兴产业" ,
//                    "高新技术企业" ,
//                    "科技企业" ,
//                    "微企业" ,
//                    "生产力促进中心" ,
//                    "服务机构" ,
//                    "众创空间" ,
//                    "行业协会" ,
//                    "高校" ,
//                    "科研院所" ,
//                    "并购重组" ,
//                    "上市" ,
//                    "创业投资" ,
//                    "创业风险投资" ,
//                    "天使投资" ,
//                    "民间资本" ,
//                    "品牌" ,
//                    "专利" ,
//                    "合作" ,
//                    "国际化" ,
//                    "集聚发展" ,
//                    "人才培养" ,
//                    "人才引进" ,
//                    "市场化" ,
//                    "服务平台" ,
//                    "平台建设" ,
//                    "基地建设" ,
//                    "公共服务" ,
//                    "信息服务" ,
//                    "技术创新" ,
//                    "科技创新" ,
//                    "自主创新" ,
//                    "服务标准" ,
//                    "服务能力" ,
//                    "服务体系" ,
//                    "专业化" ,
//                    "政策扶持" ,
//                    "政策环境" ,
//                    "政策体系" ,
//                    "制度" ,
//                    "监管" ,
//                    "市场准入" ,
//                    "财政资金" ,
//                    "引导基金" ,
//                    "专项资金" ,
//                    "税收政策" ,"税收优惠" ,"政府购买" ,
//                    "政府采购" ,
//                    "服务外包"};
//            for (String word:list) {
//                System.out.println(word+":"+wordVectorModel.nearest(word));
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //准备语料(未完成)，使用python程序已经准备好
    //测试hanlp
    private void prepareCorpus(){
        String filepath = "data/test";
        File directory = new File(filepath);
        File [] filelist = directory.listFiles();
        System.out.println(filelist.length);

        List<File> filelists = new ArrayList<File>();
        for(int i=0;i<filelist.length;i++){
            if(filelist[i].isFile())
                filelists.add(filelist[i]);
        }
//        List<StringBuffer> documentlist = new ArrayList<StringBuffer>();
        for(File file:filelists){
            StringBuffer document = new StringBuffer();
            document.append(file.getName()+"\n");
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(file));
                String tempString = null;
                int line = 1;
                while ((tempString = reader.readLine()) != null) {
                    document.append(tempString);
                    line++;
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e1) {
                    }
                }
            }
            List<String> keywordList = HanLP.extractKeyword(document.toString(), 10);
            System.out.println(file.getName()+":"+keywordList);
            List<String> phraseList = HanLP.extractPhrase(document.toString(), 10);
            System.out.println(file.getName()+":"+phraseList);
            List<String> tieleList = HanLP.extractPhrase(file.getName(), 3);
            System.out.println(file.getName()+":"+tieleList);
        }
    }


    //训练模型
    private boolean train(){
        try{
            Word2VecTrainer trainerBuilder = new Word2VecTrainer();
            WordVectorModel wordVectorModel = trainerBuilder.train("D:\\Desktop\\自然语言处理\\wikiextractor-master\\zhwiki\\AA\\policy_corpus", "data/wiki_policy_vectors.txt");
            wordVectorModel.nearest("中国");
        }catch (Exception e){
            return false;
        }
        return true;
    }
}
