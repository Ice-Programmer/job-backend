package com.ice.job.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 应聘者
 * @TableName employee
 */
@TableName(value ="employee")
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
     * 技能标签
     */
    private String skillTag;

    /**
     * 最高学历
     */
    private Integer education;

    /**
     * 专业id
     */
    private Long majorId;

    /**
     * 学校id
     */
    private Long schoolId;

    /**
     * 行业期望
     */
    private String industryIds;

    /**
     * 毕业年份
     */
    private Integer graduateYear;

    /**
     * 求职状态
     */
    private Integer jobStatus;

    /**
     * 薪水要求（例如：10-15）
     */
    private String salaryExpectation;

    /**
     * 简历地址
     */
    private String biography;

    /**
     * 居住地（省-市-区）
     */
    private String address;

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
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}