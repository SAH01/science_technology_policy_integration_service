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
@TableName("policy_analysis_formula")
@Excel("政策分析公式表")
public class AnalysisFormula implements Serializable {

    private static final long serialVersionUID = 5702271568363798328L;
    /**
     * 公式 ID
     */
    @TableId(value = "FORMULA_ID", type = IdType.AUTO)
    private Long formulaId;

    /**
     * 上级公式 ID
     */
    @TableField("PARENT_ID")
    private Long parentId;

    /**
     * 公式名称
     */
    @TableField("FORMULA_NAME")
    @NotBlank(message = "{required}")
    @Size(max = 50, message = "{noMoreThan}")
    @ExcelField(value = "公式名称")
    private String formulaName;

    /**
     * 公式内容
     */
    @TableField("FORMULA_CONTENT")
    @ExcelField(value = "公式内容")
    private String formulaContent;

    /**
     * 公式权重
     */
    @TableField("WEIGHT")
    @ExcelField(value = "公式权重")
    private Float weight;

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


    /**
     * 政策公式对应科技评价指标 id
     */
    private transient String entityIndexIds;
}
