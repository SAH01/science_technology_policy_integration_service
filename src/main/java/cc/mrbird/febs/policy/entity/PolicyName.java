package cc.mrbird.febs.policy.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_policy_type")
public class PolicyName implements Serializable {
    private static final long serialVersionUID = 5702271568363798328L;

    @TableId(value = "TYPE_ID", type = IdType.AUTO)
    private Long typeId;

    @TableField("PARENT_ID")
    private Long parentId;

    @TableField("ORDER_NUM")
    private int orderNum;

    @TableField("TYPE_NAME")
    @NotBlank(message = "{required}")
    @Size(max = 50, message = "{noMoreThan}")
    @ExcelField(value = "公式名称")
    private String typeName;

    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("MODIFY_TIME")
    private Date modifyTime;
}
