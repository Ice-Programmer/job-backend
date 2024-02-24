package com.ice.job.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 应聘者经历
 * @TableName employee_experience
 */
@Data
public class EmployeeExperienceVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 经历名称
     */
    private String experienceName;

    /**
     * 开始时间
     */
    private String beginTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 担任职务
     */
    private String jobRole;

    /**
     * 经历描述
     */
    private String experienceDescript;

    /**
     * 经历种类
     */
    private Integer experienceType;

    private static final long serialVersionUID = 4595068109965924389L;

}