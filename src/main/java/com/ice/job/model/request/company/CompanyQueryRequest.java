package com.ice.job.model.request.company;

import com.ice.job.common.PageRequest;
import com.ice.job.model.enums.ExperienceTypeEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 应聘者经历
 * @TableName employee_experience
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CompanyQueryRequest extends PageRequest {

    /**
     * id
     */
    private Long id;

    /**
     * ids
     */
    private List<Long> ids;

    /**
     * 搜索内容
     */
    private String searchText;

    /**
     * 公司类型
     */
    private Integer companyIndustryType;

    /**
     * 所在城市id
     */
    private Long cityId;

}