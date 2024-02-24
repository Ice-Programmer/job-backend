package com.ice.job.model.request.experience;

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
public class ExperienceQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * ids
     */
    private List<Long> ids;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 搜索内容
     */
    private String searchText;

    /**
     * 主要经历类型
     * @see ExperienceTypeEnum
     */
    private Integer experienceType;

    private static final long serialVersionUID = 5694687108772094129L;

}