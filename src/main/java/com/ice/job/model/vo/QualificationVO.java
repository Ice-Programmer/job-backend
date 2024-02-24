package com.ice.job.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 资格证书 VO
 *
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/2/24 16:06
 */
@Data
@NoArgsConstructor
public class QualificationVO {

    /**
     * 证书类型
     */
    private String qualificationType;

    /**
     * 证书列表
     */
    private List<QualificationDetail> qualificationList;

    @Data
    @NoArgsConstructor
    public static class QualificationDetail {

        /**
         * 证书id
         */
        private Long qualificationId;

        /**
         * 证书名称
         */
        private String qualificationName;
    }
}

