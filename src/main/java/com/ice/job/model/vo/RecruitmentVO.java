package com.ice.job.model.vo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 招聘信息
 *
 * @TableName recruitment
 */
@Data
@NoArgsConstructor
public class RecruitmentVO implements Serializable {

    /**
     * id
     */
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
    private CompanyInfo companyInfo;

    @Data
    @NoArgsConstructor
    public static class CompanyInfo {
        /**
         * id
         */
        private Long id;

        /**
         * 公司名称
         */
        private String companyName;

        /**
         * 公司logo
         */
        private String companyLogo;
    }

    /**
     * 职业信息
     */
    private PositionVO positionInfo;

    /**
     * 行业信息
     */
    private IndustryVO industryInfo;

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
    private List<String> jobSkillList;

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

    private static final long serialVersionUID = 6818879897980635896L;

}