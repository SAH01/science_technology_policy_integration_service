package cc.mrbird.febs.policy.entity;

import cc.mrbird.febs.common.converter.TimeConverter;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * @author WangRY
 */
@Data
@TableName("industrial_division")
@Excel("政策产业划分表")
public class PolicyIndustrial implements Serializable {

    private static final long serialVersionUID = 5702271568363798328L;
    /**
     * 产业 ID
     */
    @TableId(value = "INDUSTRIAL_ID", type = IdType.AUTO)
    private Long industrialId;

    /**
     * 上级产业 ID
     */
    @TableField("PARENT_ID")
    private Long parentId;

    /**
     * 产业名称
     */
    @TableField("INDUSTRIAL_NAME")
    @NotBlank(message = "{required}")
    @Size(max = 10, message = "{noMoreThan}")
    @ExcelField(value = "产业名称")
    private String industrialName;

    /**
     * 排序
     */
    @TableField("ORDER_NUM")
    private Long orderNum;

    /**
     * 创建时间
     */
    @TableField("CREATE_TIME")
    @ExcelField(value = "创建时间", writeConverter = TimeConverter.class)
    private Date createTime;

    @TableField("MODIFY_TIME")
    @ExcelField(value = "修改时间", writeConverter = TimeConverter.class)
    private Date modifyTime;

}
