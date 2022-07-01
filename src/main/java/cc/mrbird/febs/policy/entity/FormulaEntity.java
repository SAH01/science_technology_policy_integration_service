package cc.mrbird.febs.policy.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @author WangRY
 */
@Data
@TableName("formula_entity")
public class FormulaEntity implements Serializable {

    private static final long serialVersionUID = -5200596408874170216L;
    /**
     * 政策公式ID
     */
    @TableField("FORMULA_ID")
    private Long formulaId;

    /**
     * 科技指标ID
     */
    @TableField("ENTITYINDEX_ID")
    private Long entityIndexId;


}
