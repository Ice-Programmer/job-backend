package com.ice.job.model.request.employer;

import com.ice.job.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/3/25 18:13
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EmployerQueryRequest extends PageRequest {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户ids
     */
    private List<Long> userIdList;

    /**
     * 公司id
     */
    private Long companyId;

    /**
     * 职位id
     */
    private Long positionId;
}
