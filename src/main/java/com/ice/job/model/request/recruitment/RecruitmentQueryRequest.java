package com.ice.job.model.request.recruitment;

import com.ice.job.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 招聘信息
 *
 * @TableName recruitment
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RecruitmentQueryRequest extends PageRequest {
    /**
     * id
     */
    private Long id;

    /**
     * ids
     */
    private List<Long> ids;

    /**
     * 搜索条件
     */
    private String searchText;

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
     * 最低学历要求
     */
    private Integer educationType;

    /**
     * 职业关键词JSON
     */
    private String jobKeyword;

    /**
     * 职业类型（实习、兼职、春招等）
     */
    private Integer jobType;

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
    private List<Long> cityIdList;

    /**
     * 招聘活跃 （0 - 招聘中 1 - 结束招聘）
     */
    private Integer jobActive;
}