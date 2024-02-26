package com.ice.job.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 应聘者
 *
 * @TableName employee
 */
@TableName(value = "employee")
@Data
public class Employee implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 性别（0 - 女 1 - 男）
     */
    private Integer gender;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 个人优势
     */
    private String advantage;

    /**
     * 技能证书
     */
    private String qualificationIds;

    /**
     * 技能标签
     */
    private String skillTag;

    /**
     * 最高学历
     */
    private Integer education;

    /**
     * 毕业年份
     */
    private Integer graduateYear;

    /**
     * 求职状态
     */
    private Integer jobStatus;

    /**
     * 简历地址
     */
    private String biography;

    /**
     * 居住城市
     */
    private Long cityId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}