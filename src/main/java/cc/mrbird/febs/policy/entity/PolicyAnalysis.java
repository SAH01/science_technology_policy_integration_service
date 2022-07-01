package cc.mrbird.febs.policy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("policysimilar")
public class PolicyAnalysis implements Serializable {
    private int id;
    private int policyid;
    private String policymatters;
    private String policytext;
    private String policydept;
    private String policyorgan;
    private String policymain;
    private String policyname;
    private String similarmatters;
    private String similartext;
    private String similardept;
    private String similarorgan;
    private String similarmain;
    private String similarname;
    private String policykey;
    private String similarkey;
    private String one;
    private String two;
}
