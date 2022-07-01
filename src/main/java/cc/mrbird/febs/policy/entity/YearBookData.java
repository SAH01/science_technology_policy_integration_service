package cc.mrbird.febs.policy.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * 经济统计年鉴的数据
 */
@Data
@TableName("annual_cell")
public class YearBookData implements Serializable {

    private static final long serialVersionUID = 5702271568363798328L;

    @TableField("REGION")
    private String region;
    @TableField("VALUE")
    private String value;
    @TableField("UNIT_C")
    private String unitC;
    @TableField("YEARS")
    private String years;
    @TableField("NAME")
    private String name;

    //    @TableField("PARENT_ID")
//    private String ID;
//    @TableField("PARENT_ID")
//    private String bookName;
//    @TableField("PARENT_ID")
//    private String REGION_ID;
//    @TableField("SEARCH_VALUE")
//    private String SEARCH_VALUE;
    @Override
    public String toString() {
        return "region=" + region + "value=" + value + "unit_c=" + unitC + "years=" + years + "name=" + name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getRegion(), getYears());
    }

    @Override
    public boolean equals(Object o) {
        //自反性
        if (this == o) return true;
        //任何对象不等于null，比较是否为同一类型
        if (!(o instanceof YearBookData)) return false;
        //强制类型转换
        YearBookData bookData = (YearBookData) o;
        //比较属性值
        return Objects.equals(getName(), bookData.getName()) &&
                Objects.equals(getRegion(), bookData.getRegion()) &&
                Objects.equals(getYears(), bookData.getYears());
    }
}
