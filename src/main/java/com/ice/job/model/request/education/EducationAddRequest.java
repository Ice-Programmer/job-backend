package com.ice.job.model.request.education;

import lombok.Data;

import java.io.Serializable;

/**
 * 教育经历添加 Request
 */
@Data
public class EducationAddRequest implements Serializable {

    /**
     * 学校id
     */
    private Long schoolId;

    /**
     * 学历类型
     */
    private Integer educationType;

    /**
     * 开始年份
     */
    private Integer beginYear;

    /**
     * 结束年份
     */
    private Integer endYear;

    /**
     * 专业id
     */
    private Long majorId;

    /**
     * 在校经历
     */
    private String activity;

    private static final long serialVersionUID = -6177921978159366316L;
}