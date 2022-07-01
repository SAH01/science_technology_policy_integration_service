package cc.mrbird.febs.policy.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 根据政策公式，挖掘科技政策的结果。统计不同年份出现的政策次数
 */
@Data
public class PolicyStatisticalResult {
    private String region;
    private Integer year;
    private Integer num;
}
