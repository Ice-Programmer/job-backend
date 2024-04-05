package com.ice.job.model.request.recruitmentcomment;

import com.ice.job.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/4/4 14:45
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RecruitmentCommentQueryRequest extends PageRequest {

    /**
     * id
     */
    private Long id;

    /**
     * ids
     */
    private List<Long> ids;

    /**
     * 招聘id
     */
    private Long recruitmentId;

    /**
     * 用户id
     */
    private Long userId;
}
