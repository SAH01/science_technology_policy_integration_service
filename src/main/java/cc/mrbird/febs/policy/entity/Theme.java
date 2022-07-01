package cc.mrbird.febs.policy.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("theme")
public class Theme {
    private int id;
    private String firstname;
    private String secondname;
    private String thirdname;
}
