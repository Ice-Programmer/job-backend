package com.ice.job.model.request.experience;

import com.ice.job.model.enums.ExperienceTypeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * 应聘者经历
 * @TableName employee_experience
 */
@Data
public class ExperienceAddRequest implements Serializable {

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
     * @see ExperienceTypeEnum
     */
    private Integer experienceType;

    private static final long serialVersionUID = 7658110454446193355L;

}