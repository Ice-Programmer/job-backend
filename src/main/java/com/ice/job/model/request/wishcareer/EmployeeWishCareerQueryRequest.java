package com.ice.job.model.request.wishcareer;

import com.ice.job.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/2/26 14:19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EmployeeWishCareerQueryRequest extends PageRequest {

    /**
     * id
     */
    private Long id;

    /**
     * ids
     */
    private List<Long> ids;

    /**
     * 职业id
     */
    private Long positionId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 工作城市
     */
    private Long cityId;

}
