package cc.mrbird.febs.policy.entity.model;

import lombok.Data;

import java.io.Serializable;

/**
 * model下的类是在已有的数据库基础上进行分析而设计的展示用的类，没有数据库
 * 统计各部门出台的政策数量
 */
@Data
public class OrganPolicyNum implements Serializable {
    String organ;
    Integer num;
}
