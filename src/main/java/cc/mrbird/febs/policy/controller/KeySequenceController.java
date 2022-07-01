package cc.mrbird.febs.policy.controller;

import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.policy.entity.KeySentence;
import cc.mrbird.febs.policy.service.IKeySentenceService;
import com.alibaba.fastjson.JSONObject;
import com.hankcs.hanlp.mining.cluster.ClusterAnalyzer;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author MrBird
 */
@Slf4j
@RestController
@RequestMapping("textMining")
public class KeySequenceController extends BaseController {

    @Autowired
    IKeySentenceService keySentenceService;

    @GetMapping("sequenceList")
    @RequiresPermissions("user:view")

    public FebsResponse userList(KeySentence keySentence, QueryRequest request) {
        final int TYPES = 3;
        List<KeySentence> sentencesList = this.keySentenceService.getSentencesList(keySentence);

        JSONObject json = new JSONObject();

        if(sentencesList.size()==0){
            json.put("rows", sentencesList);
            json.put("total", sentencesList.size());
            json.put("formulaKeywords", "");
            json.put("formulaContent", "");
            json.put("types", TYPES);
            return new FebsResponse().success().data(json);
        }
        //获取公式的内容，作为返回值的一部分
        String formulaContent = this.keySentenceService.getFormulaContentById(keySentence.getFormulaId());
        ClusterAnalyzer<String> analyzer = new ClusterAnalyzer<String>();

        for (KeySentence keySentence1 : sentencesList) {
            analyzer.addDocument(keySentence1.getSentence(), keySentence1.getSentence());
        }

        //按分类结果分组的句子信息结果集
        List<KeySentence>[] keySentenceList = null;

        //获得句子聚类的结果
        List<Set<String>> clusteringSentences;

        try {
            clusteringSentences = analyzer.repeatedBisection(TYPES);
            keySentenceList = new ArrayList[clusteringSentences.size()];
            for (int i = 0; i < clusteringSentences.size(); i++) {
                keySentenceList[i] = new ArrayList<>();
            }
            for (KeySentence keySentence2 : sentencesList) {
                for (int i = 0; i < clusteringSentences.size(); i++) {
                    if (clusteringSentences.get(i).contains(keySentence2.getSentence())) {
                        keySentence2.setType(i);
                        keySentenceList[i].add(keySentence2);
//                        System.out.println(i + "\t" + keySentence2.getSentence());
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("没有找到满足该公式的政策。");
            e.getMessage();
        }

        //把所有的句子信息放到一个集合里，返回最展示，按照类别和时间排序
        sentencesList.clear();
        if (keySentenceList != null) {
            for (int i = 0; i < keySentenceList.length; i++) {
                for (KeySentence keySentence3 : keySentenceList[i]) {
                    sentencesList.add(keySentence3);
                }
            }
        }

        json.put("rows", sentencesList);
        json.put("total", sentencesList.size());
        json.put("formulaKeywords", this.keySentenceService.getFormulaKeywordsByFormulaContent(formulaContent));
        json.put("formulaContent", formulaContent);
        json.put("types", TYPES);
        return new FebsResponse().success().data(json);
    }
}
