package cc.mrbird.febs.policy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("policy_kind")
public class PolicyKind {
    private int id;
    private String policykind;
}
