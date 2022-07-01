package cc.mrbird.febs.policy.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
@Data
@TableName("namelist")
public class PolicyNeo4j {
    private int id;
    private String policyname;
    private String text;
    private String theme;
    private String dept;
    private String year;
    private String keyword;
    private String province;
    private String highpolicy;
    private String lowpolicy;
    private String similarpolicy;
    private String main;
}
