package cc.mrbird.febs.policy.service;

import cc.mrbird.febs.policy.entity.KeySentence;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IKeySentenceService extends IService<KeySentence> {
    //获得由公式查询到的句子
    List<KeySentence> getSentencesList(KeySentence keySentence);
    //获得公式内容，通过公式id
    String getFormulaContentById(Long formulaId);
    //获得公式关键词，通过公式内容
    List<String> getFormulaKeywordsByFormulaContent(String formulaContent);
}
