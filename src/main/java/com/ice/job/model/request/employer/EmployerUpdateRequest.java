package com.ice.job.model.request.employer;

import lombok.Data;

/**
 * 招聘者
 * @TableName employer
 */
@Data
public class EmployerUpdateRequest {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 公司id
     */
    private Long companyId;

    /**
     * 职位id
     */
    private Long positionId;

}