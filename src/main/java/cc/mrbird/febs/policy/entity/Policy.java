package cc.mrbird.febs.policy.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("policy")
public class Policy implements Serializable {
    private static final long serialVersionUID = 5702271568363798328L;
    private int id;
    private String name;
    private String type;
    private String category;
    private String range;
    private String document;
    private String form;
    private String organ;
    private Date viadata;
    private Date pubdata;
    private Date perdata;
    private String field;
    private String theme;
    private String keyword;
    private String superior;
    private String precursor;
    private String succeed;
    private String state;
    private String text;
    private String pdf;
    private String redundancy;
    private String policymatters;
    private String policydept;
    private String policytext;
    private String policyorgan;
    private String policymain;
    private int allsum;
    private String rank;

    @TableField(exist = false)
    private Long deptId;
    @TableField(exist = false)
    private Long categoryId;
    /**
     * 部门名称
     */
    @ExcelField(value = "部门")
    @TableField(exist = false)
    private String deptName;

    @TableField(exist = false)
    private String createTimeFrom;
    @TableField(exist = false)
    private String createTimeTo;
    private String newkey;
    private String year;
}
