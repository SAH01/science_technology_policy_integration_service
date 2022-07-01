package cc.mrbird.febs.policy.entity;

import java.util.Date;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 *  Entity
 *
 * @author WangRenyi
 * @date 2022-04-03 11:42:16
 */
@Data
@TableName("policy_enter")
public class PolicyEnter {
    /**
     * 
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 发布人
     */
    @TableField("publisher")
    private String publisher;

    /**
     * 
     */
    @TableField("name")
    private String name;

    /**
     * 
     */
    @TableField("document_type")
    private String documentType;

    /**
     * 
     */
    @TableField("document_area")
    private String documentArea;

    /**
     * 
     */
    @TableField("document_id")
    private String documentId;

    /**
     * 
     */
    @TableField("publish_type")
    private String publishType;

    /**
     * 
     */
    @TableField("organ")
    private String organ;

    /**
     * 
     */
    @TableField("pass_date")
    private String passDate;

    /**
     * 
     */
    @TableField("publish_date")
    private String publishDate;

    /**
     * 
     */
    @TableField("exe_date")
    private String exeDate;

    @TableField("document_target")
    private String documentTarget;

    @TableField("theme_words")
    private String themeWords;

    @TableField("key_words")
    private String keyWords;

    @TableField("up_document")
    private String upDocument;

    @TableField("pre_document")
    private String preDocument;

    @TableField("post_document")
    private String postDocument;

    @TableField("document_state")
    private String documentState;
    @TableField("sur_field")
    private String surField;

    @TableField("text")
    private String text;

    @TableField("index_state")
    private String indexState;

    @Override
    public String toString() {
        return "PolicyEnter{" +
                "id=" + id +
                ", publisher='" + publisher + '\'' +
                ", name='" + name + '\'' +
                ", documentType='" + documentType + '\'' +
                ", documentArea='" + documentArea + '\'' +
                ", documentId='" + documentId + '\'' +
                ", publishType='" + publishType + '\'' +
                ", organ='" + organ + '\'' +
                ", passDate='" + passDate + '\'' +
                ", publishDate='" + publishDate + '\'' +
                ", exeDate='" + exeDate + '\'' +
                ", documentTarget='" + documentTarget + '\'' +
                ", themeWords='" + themeWords + '\'' +
                ", keyWords='" + keyWords + '\'' +
                ", upDocument='" + upDocument + '\'' +
                ", preDocument='" + preDocument + '\'' +
                ", postDocument='" + postDocument + '\'' +
                ", documentState='" + documentState + '\'' +
                ", surField='" + surField + '\'' +
                ", text='" + text + '\'' +
                ", indexState='" + indexState + '\'' +
                '}';
    }
}
