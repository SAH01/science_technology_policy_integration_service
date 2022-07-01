package cc.mrbird.febs.policy.entity.model;

import lombok.Data;

import java.io.Serializable;

/**
 * model下的类是在已有的数据库基础上进行分析而设计的展示用的类，没有数据库
 * 统计各年不同类型政策数量，用堆叠条形图展示
 */
@Data
public class YearCategoryNum implements Serializable {
    String year;
    String category;
    Integer num;
    String regionName;
}
