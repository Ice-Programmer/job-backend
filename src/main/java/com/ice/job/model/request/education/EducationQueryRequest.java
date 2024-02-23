package com.ice.job.model.request.education;

import com.ice.job.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/2/23 20:21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EducationQueryRequest extends PageRequest {

    /**
     * id
     */
    private Long id;

    /**
     * ids
     */
    private List<Long> ids;

    /**
     * 学校id
     */
    private Long schoolId;

    /**
     * 专业id
     */
    private Long majorId;

    /**
     * 用户id
     */
    private Long userId;
}
