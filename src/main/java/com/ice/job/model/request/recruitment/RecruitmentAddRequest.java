package com.ice.job.model.request.recruitment;

import lombok.Data;

import java.util.List;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/2/28 12:55
 */
@Data
public class RecruitmentAddRequest {

    /**
     * 岗位招聘标题
     */
    private String jobName;

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
    private List<String> jobKeywords;

    /**
     * 职业技术栈JSON
     */
    private List<String> jobSkills;

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
}
