package cc.mrbird.febs.policy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("policy_evolution")
public class PolicyEvolution {
    private int id;
    private int policyid;
    private String policytext;
    private String policyname;
    private int time;
    private String type;
    private String data;
    private String policymain;

}
