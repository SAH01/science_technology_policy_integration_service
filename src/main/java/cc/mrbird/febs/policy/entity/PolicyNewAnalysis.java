package cc.mrbird.febs.policy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("policynew_analysis")
public class PolicyNewAnalysis {
    private int id;
    private int policyid;
    private String policymatters;
    private String policydept;
    private String policytext;
    private String policykey;
    private String policyname;
}
