package cc.mrbird.febs.policy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("policynew")
public class PolicyNew {
    private int id;
    private String policyname;
    private String policydept;
    private String policymain;
    private String policytext;
    private String policytime;
    private String highpolicy;
    private String lowpolicy;
}
