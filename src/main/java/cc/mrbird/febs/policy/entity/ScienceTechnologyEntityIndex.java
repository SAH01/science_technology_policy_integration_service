package cc.mrbird.febs.policy.entity;

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

/**
 * @author WangRY
 * 科技实体指标
 * 分三层  树形结构
 */
@Data
@TableName("entity_index")
@Excel("科技实体指标")
public class ScienceTechnologyEntityIndex implements Serializable {

    private static final long serialVersionUID = 5702271568363798328L;
    /**
     * 科技实体指标 ID
     */
    @TableId(value = "ENTITYINDEX_ID", type = IdType.AUTO)
    private Long entityIndexId;

    /**
     * 上级科技实体指标 ID
     */
    @TableField("PARENT_ID")
    private Long parentId;

    /**
     * 科技实体指标名称
     */
    @TableField("ENTITYINDEX_NAME")
    @NotBlank(message = "{required}")
    @Size(max = 50, message = "{noMoreThan}")
    @ExcelField(value = "科技实体指标名称")
    private String entityIndexName;

    /**
     * 科技实体指标内容
     */
    @TableField("ENTITYINDEX_CONTENT")
    @Size(max = 150, message = "{noMoreThan}")
    @ExcelField(value = "科技实体指标名称")
    private String entityIndexContent;
    /**
     * 标准值
     */
    @TableField("STANDARD_VALUE")
    @ExcelField(value = "标准值")
    private Float standardValue;

    /**
     * 排序
     */
    @TableField("ORDER_NUM")
    private Long orderNum;
}
