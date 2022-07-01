package cc.mrbird.febs.policy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("annual")
public class Annual {
    private int id;
    private String name;
    private String province;
    private String year;
    private String unit_c;
    private String data;
}
