package com.ice.job.model.request.qualification;

import lombok.Data;

import java.util.List;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/2/24 20:28
 */
@Data
public class QualificationEmployeeUpdateRequest {

    /**
     * 证书 id 列表
     */
    private List<Long> qualificationIdList;
}
