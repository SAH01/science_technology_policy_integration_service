package cc.mrbird.febs.policy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("policy_similarity")
public class PolicySimilarity {
    private int startid;
    private int endid;
    private int category;
    private String startname;
    private String endname;
    private String form;
    private String startrank;
    private String endrank;
    private int allsum;
}
