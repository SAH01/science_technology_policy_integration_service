package cc.mrbird.febs.policy.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 为echarts的关系图提供数据支持
 * 是个模型类，表示了：
 * 关键词，所属类（{name:'服务领域'},{name:'政策对象'},{name:'企业发展'},{name:'政策措施'}），权值（出现次数），与之关联词（同一个政策中的关键词相互关联）
 */
@Data
public class GexfNodeHelper {
    private int id;
    private String keyword;
    private int type;
    private float weight;
    private Map<String,Integer> relationWords;
    public GexfNodeHelper(int id,String keyword,Integer type,float weight){
        this.id = id;
        this.keyword =keyword;
        this.type = type;
        this.weight = weight;
        relationWords = new HashMap<>();
    }
}
