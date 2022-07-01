package cc.mrbird.febs.policy.entity;

import lombok.Data;

import java.util.List;

/**
 * @Auther: wzs
 * @Date: 2022/2/28 15:05
 * @Description:
 */
@Data
public class Tree {
    String title;
    String id;
    List<Tree>children;


}
