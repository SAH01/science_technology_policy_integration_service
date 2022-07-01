package cc.mrbird.febs.policy.entity.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 此类用于返回echarts展示所需要的数据格式
 */
@Data
public class ShowData implements Serializable {
    private String name;
    private String[] values;
    private String unit_c;
    private String type;
}
