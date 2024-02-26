package com.ice.job.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 教育经历 VO
 */
@Data
public class EmployeeEducationVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 学校id
     */
    private Long schoolId;

    /**
     * 学校名称
     */
    private String schoolName;

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
     * 专业名称
     */
    private String majorName;

    /**
     * 在校经历
     */
    private String activity;

    /**
     * 相关数量
     */
    private Integer postNum;

    private static final long serialVersionUID = 3700233681518370464L;
}
