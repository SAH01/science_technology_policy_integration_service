package cc.mrbird.febs.policy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("policy_contrast")
public class PolicyContrast {
    private int id;
    private int kindid;
    private String kind;
    private String policyname;
    private String policydept;
    private String policymain;
    private int policyid;
    private String serial;
    private String allsum;
    private String policykey;
    private int sortid;
    private String parentname;
    private String policymain2;
    private int allnum;
    private int parentid;
    private String city;
    private String policytime;
    private String year;

}
