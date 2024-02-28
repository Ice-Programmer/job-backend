package com.ice.job.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 招聘信息
 * @TableName recruitment
 */
@TableName(value ="recruitment")
@Data
public class Recruitment implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 岗位招聘标题
     */
    private String jobName;

    /**
     * 岗位发布者id
     */
    private Long userId;

    /**
     * 公司id
     */
    private Long companyId;

    /**
     * 职业id
     */
    private Long positionId;

    /**
     * 行业id
     */
    private Long industryId;

    /**
     * 职位详情
     */
    private String jobDescription;

    /**
     * 职位要求
     */
    private String jobRequirements;

    /**
     * 最低学历要求
     */
    private Integer educationType;

    /**
     * 职业关键词JSON
     */
    private String jobKeyword;

    /**
     * 职业技术栈JSON
     */
    private String jobSkills;

    /**
     * 职业类型（实习、兼职、春招等）
     */
    private Integer jobType;

    /**
     * 岗位要求地址
     */
    private String jobAddress;

    /**
     * 薪水上限
     */
    private Integer salaryUpper;

    /**
     * 薪水下限
     */
    private Integer salaryLower;

    /**
     * 薪水种类
     */
    private Integer salaryUnit;

    /**
     * 所在城市id
     */
    private Long cityId;

    /**
     * 招聘活跃 （0 - 招聘中 1 - 结束招聘）
     */
    private Integer jobActive;

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