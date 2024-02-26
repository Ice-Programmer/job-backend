package com.ice.job.model.request.wishcareer;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/2/26 14:19
 */
@Data
public class EmployeeWishCareerAddRequest {

    /**
     * 职业id
     */
    private Long positionId;

    /**
     * 行业id列表
     */
    private List<Long> industryIdList;

    /**
     * 薪水要求（例如：10-15）
     */
    private String salaryExpectation;

    /**
     * 工作城市
     */
    private Long cityId;

}
