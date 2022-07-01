package cc.mrbird.febs.policy.service.impl;

import cc.mrbird.febs.policy.entity.KeySentence;
import cc.mrbird.febs.policy.mapper.KeySentenceMapper;
import cc.mrbird.febs.policy.service.IKeySentenceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class KeySentenceServiceImpl extends ServiceImpl<KeySentenceMapper, KeySentence> implements IKeySentenceService {
    @Override
    public List<KeySentence> getSentencesList(KeySentence keySentence) {
        String[] SQLAndFormulaContent = constructSQLAndFormulaContent(keySentence);
        List<KeySentence> sentencesList = new ArrayList<>();
        if(SQLAndFormulaContent==null){
            return sentencesList;
        }
        StringBuilder finalSQL = new StringBuilder(SQLAndFormulaContent[0]);
        String formulaContent = SQLAndFormulaContent[1];
        List<KeySentence> policyFileList = this.baseMapper.getKeyPolicyFileListBySQL(finalSQL);
        /*for (KeySentence policyFile : policyFileList) {
            System.out.println(policyFile.getName()+"\t"+policyFile.getPubDate()+"\t"+policyFile.getPubData()+"\t"+policyFile.getOrgan()+"\t"+policyFile.getFormulaId());
        }*/
        //时间格式化
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //获取公式中的关键词
        List<String> formulaKeywords = getFormulaKeywordsByFormulaContent(formulaContent);
        for (KeySentence policyFile : policyFileList) {
            String name = policyFile.getName();
            Date pubDate = policyFile.getPubData();
            String organ = policyFile.getOrgan();
            String text = policyFile.getText();
            for (String sequence : getSequenceList(text, formulaKeywords)) {
                KeySentence satisfyKeySentence = new KeySentence();
                satisfyKeySentence.setName(name);
                if(pubDate!=null){
                    satisfyKeySentence.setPubDate(simpleDateFormat.format(pubDate));
                }
                satisfyKeySentence.setOrgan(organ);
                satisfyKeySentence.setSentence(sequence);
                sentencesList.add(satisfyKeySentence);
            }
        }//显示数据

        return sentencesList;
    }

    @Override
    public String getFormulaContentById(Long formulaId) {
        if(formulaId==null)
            return null;
        return this.baseMapper.getFormulaContentById(formulaId);

    }
    @Override
    //获得公式关键词，通过公式内容
    public List<String> getFormulaKeywordsByFormulaContent(String formulaContent) {
        //获取公式中的关键词，作为返回值的一部分
        List<String> formulaKeywords = new ArrayList<>();
        for (String temp1 : formulaContent.split("-")) {
            for (String temp2 : temp1.split("、")) {
                if (!"".equals(temp2.trim())) {
                    formulaKeywords.add(temp2.trim());
                }
            }
        }
        return formulaKeywords;
    }

    //构造查询的SQL
    private String[] constructSQLAndFormulaContent(KeySentence keySentence) {
        String[] result = new String[2];
        String formulaContent = getFormulaContentById(keySentence.getFormulaId());
        if(formulaContent==null)
            return null;
        result[1]=formulaContent;
        String[] segmentation = formulaContent.split("-");
        String textMiningSQL = "SELECT `name`,pubdata,organ,text FROM policy_old WHERE 1 = 2 ";
        //由于 StringBuilder 相较于 StringBuffer 有速度优势，所以多数情况下建议使用 StringBuilder 类。然而在应用程序要求线程安全的情况下，则必须使用 StringBuffer 类。此处没有多线程。
        StringBuilder appendSQL = new StringBuilder("OR ");
        if (keySentence.getCreateTimeFrom() != null && !"".equals(keySentence.getCreateTimeFrom().trim()) && keySentence.getCreateTimeTo() != null && !"".equals(keySentence.getCreateTimeTo().trim())) {
            appendSQL.append("(pubdata > '").append(keySentence.getCreateTimeFrom()).append("' AND pubdata < '").append(keySentence.getCreateTimeTo()).append("' ) AND ");
        }
        //只有关键词  不包含主语
        if (segmentation.length == 1) {
            String[] keywords = formulaContent.split("、");
            appendSQL.append("(`text` LIKE '%").append(keywords[0]).append("%' ");
            if (keywords.length > 1) {
                for (int i = 1; i < keywords.length; i++) {
                    appendSQL.append("OR text LIKE '%").append(keywords[i]).append("%' ");
                }
            }
            appendSQL.append(") ");
        } else if (segmentation.length == 2) {
            String[] frontPartOfContent = segmentation[0].split("、");
            String[] rearPartOfContent = segmentation[1].split("、");
            appendSQL.append("(`text` LIKE '%").append(frontPartOfContent[0]).append("%").append(rearPartOfContent[0]).append("%' ");
            if (rearPartOfContent.length > 1) {
                for (int i = 1; i < rearPartOfContent.length; i++) {
                    appendSQL.append("OR text LIKE '%").append(frontPartOfContent[0]).append("%").append(rearPartOfContent[i]).append("%' ");
                }
            }
            if (frontPartOfContent.length > 1) {
                for (int i = 1; i < frontPartOfContent.length; i++) {
                    for (int j = 0; j < rearPartOfContent.length; j++) {
                        appendSQL.append("OR text LIKE '%").append(frontPartOfContent[i]).append("%").append(rearPartOfContent[j]).append("%' ");
                    }
                }
            }
            appendSQL.append(") ");
        } else {
            return null;
        }
        appendSQL.append("ORDER BY pubdata DESC");
        //最终生成的SQL
        String finalSQL = new StringBuilder(textMiningSQL).append(appendSQL).toString();
        result[0]=finalSQL;
//        System.out.println(finalSQL);
        return result;
    }

    private List<String> getSequenceList(String text, List<String> formulaKeywords) {
        List<String> containKeywordSequenceList = new ArrayList<>();

        String[] sequenceList = text.split("。|；|\n");
        for (String sequence : sequenceList) {
            boolean canAdd = false;
            for (String keyword : formulaKeywords) {
                if (sequence.contains(keyword) && sequence.length() > 2 * keyword.length()) {
                    canAdd = true;
                    break;
                }
            }
            if (canAdd) {
                containKeywordSequenceList.add(sequence);
            }
        }
        return containKeywordSequenceList;
    }
}
