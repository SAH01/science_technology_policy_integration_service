package cc.mrbird.febs.policy.entity;

import lombok.Data;

import java.util.Date;

/**
 * 根据政策公式，挖掘科技政策的结果。
 */
@Data
public class KeySentence {
    private String createTimeFrom;
    private String createTimeTo;
    private Long deptId;
    private Long formulaId;

    private int type;
    private String name;
    private String pubDate;
    private Date pubData;
    private String organ;
    private String text;
    private String sentence;
}
