package cc.mrbird.febs.policy.entity;

import cc.mrbird.febs.policy.entity.model.PolicyYearRegionName;
import lombok.Data;

import java.util.List;

/**
 * 用于传值的中间对象，保存临时对象
 */
@Data
public class IntermediateVariable {
    private String createTimeFrom;
    private String createTimeTo;
    //地域id集合
    private String regionIds;
    //政策公式内容
    private String formulaContent;
    //实体指标id集合（叶子节点）
    private String entityIndexIds;
    //地域id
    private String regionId;
    //是否选中
    private String checked;
    //政策作用对象id（激发院所激情……）
    private String categoryId;
    //用户点击某一地域某一年份
    private String regionName;
    private String yearData;

    //作用对象
    private String category;

    //对一个政策深入分析
    private String policyName;

    @Override
    public String toString() {
        return "IntermediateVariable{" +
                "createTimeFrom='" + createTimeFrom + '\'' +
                ", createTimeTo='" + createTimeTo + '\'' +
                ", regionIds='" + regionIds + '\'' +
                ", formulaContent='" + formulaContent + '\'' +
                ", entityIndexIds='" + entityIndexIds + '\'' +
                ", regionId='" + regionId + '\'' +
                ", checked='" + checked + '\'' +
                ", categoryId='" + categoryId + '\'' +
                ", regionName='" + regionName + '\'' +
                ", yearData='" + yearData + '\'' +
                ", policyName='" + policyName + '\'' +
                '}';
    }
}
