package cc.mrbird.febs.policy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("policy_analysis")
public class Policyan implements Serializable {
    private int id;
    private int policyid;
    private String policymatters;
    private String policytext;
    private String policydept;
    private String policyorgan;
    private String policymain;
    private String policyname;
    private String policykey;
    private String organ;
    private String keyword;
}
