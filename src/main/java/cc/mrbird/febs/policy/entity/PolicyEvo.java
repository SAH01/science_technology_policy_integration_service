package cc.mrbird.febs.policy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("policyevo")
public class PolicyEvo {
    private int id;
    private String policyname;
    private String policydept;
    private String policytime;
    private String policymain;
    private String policytype;
    private String policytext;
    private String highpolicy;
    private String lowpolicy;
    private String keyword;
    private String policytext2;
    private int allsum;
}
