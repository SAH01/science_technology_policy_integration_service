package cc.mrbird.febs.policy.helper.policyImpact.util;

import lombok.Data;

import java.util.Map;

/**
 * 文本信息类，包含文本的文件路径，类别，词向量等
 * @author Angela
 */
@Data
public class Text {

    /**文本路径**/
    private String path;
    /**文本类别ID**/
    private int originLabelID;
    /**文本分类或聚类类别ID**/
    private int judegeLabelID;
    /**文本词-权重**/
    private Map<String,Double> words;
    /**文本长度**/
    private double length;
}