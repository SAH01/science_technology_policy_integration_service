package cc.mrbird.febs.policy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("policy_type")
public class PolicyType {
    private int id;
    private String policytype;
}
