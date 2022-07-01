package cc.mrbird.febs.policy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("policy_crawling")
public class PolicyCrawling {
    private int id;
    private String name;
    private String rank1;
    private String form;
    private String organ;
    private String year;
    private String document;
    private String text;
}
