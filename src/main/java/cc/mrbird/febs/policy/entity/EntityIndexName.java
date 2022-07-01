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
 * 用于自动补全，实体指标名称（废弃，为了提高效率，将指标名称数据保存到了一个json文件中（policy/helper/CreateEntityIndexNameJson.java
 * static/data/entityIndexName.json））
 * @author WangRY
 */
@Data
@TableName("entity_index_name")
public class EntityIndexName implements Serializable {

    private static final long serialVersionUID = 5702271568363798328L;
    /**
     * ID
     */
    @TableId(value = "name_id", type = IdType.AUTO)
    private Long nameId;

    /**
     * 名称
     */
    @TableField("entity_name")
    private String entityName;

    /**
     * 数量
     */
    @TableField("quantity")
    private Long quantity;
}
