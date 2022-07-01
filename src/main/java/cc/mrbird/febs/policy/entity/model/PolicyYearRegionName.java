package cc.mrbird.febs.policy.entity.model;

import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * 用于辅助政策公式 1.挖掘政策文件  2.按时间轴显示
 */
@Data
public class PolicyYearRegionName {
    private String year;
    private String region;
    private String name;
    private String text;
    private String type;
    private String relation;
    private List<String> sentenceList;
    private List<String> sentenceSimilarityList;

    @Override
    public String toString() {
        return "PolicyYearRegionName{" +
                "year='" + year + '\'' +
                ", region='" + region + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", sentenceList=" + sentenceList +
                '}';
    }
}
