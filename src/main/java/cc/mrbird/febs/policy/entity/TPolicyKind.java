package cc.mrbird.febs.policy.entity;

import cc.mrbird.febs.common.converter.TimeConverter;
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
@TableName("t_policy_kind")
public class TPolicyKind implements Serializable {
    private static final long serialVersionUID = 5702271568363798328L;
    /**
     * 公式 ID
     */
    @TableId(value = "KIND_ID", type = IdType.AUTO)
    private Long kindId;

    /**
     * 上级公式 ID
     */
    @TableField("PARENT_ID")
    private Long parentId;

    @TableField("BRANCH_ID")
    private int branchId;

    /**
     * 公式名称
     */
    @TableField("KIND_NAME")
    @NotBlank(message = "{required}")
    @Size(max = 50, message = "{noMoreThan}")
    @ExcelField(value = "公式名称")
    private String kindName;

    @TableField("PARENT_NAME")
    @NotBlank(message = "{required}")
    @Size(max = 50, message = "{noMoreThan}")
    @ExcelField(value = "公式名称")
    private String parentName;

    /**
     * 排序
     */
    @TableField("ORDER_NUM")
    private int orderNum;

    @TableField("YEAR_LIST")
    @Size(max = 2550, message = "{noMoreThan}")
    private String yearList;

    @TableField("CITY_LIST")
    @Size(max = 2550, message = "{noMoreThan}")
    private String cityList;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    private Date createTime;

    @TableField("MODIFY_TIME")
    private Date modifyTime;



}
