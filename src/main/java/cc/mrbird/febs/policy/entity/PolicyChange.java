package cc.mrbird.febs.policy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("policychange")
public class PolicyChange {
    private int id;
    private String name;
    private String type;
    private int num;
    private String text;
    private String year;
    private String change;
}
