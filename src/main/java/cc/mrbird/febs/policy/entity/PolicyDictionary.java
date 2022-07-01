package cc.mrbird.febs.policy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("dictionary")
public class PolicyDictionary implements Serializable {
    private int id;
    private String name;
    private String weighted;
    private String certain;
}
