package com.ice.job.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 招聘评价 VO
 */
@Data
@NoArgsConstructor
public class RecruitmentCommentVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 发布者信息
     */
    private publisherInfo publisherInfo;

    @Data
    @NoArgsConstructor
    public static class publisherInfo {
        /**
         * 发布者id
         */
        private Long id;

        /**
         * 发布者名称
         */
        private String userName;

        /**
         * 发布者头像
         */
        private String userAvatar;
    }

    /**
     * 招聘id
     */
    private Long recruitmentId;

    /**
     * 评分
     */
    private BigDecimal star;

    /**
     * 评价内容
     */
    private String commentText;

    /**
     * 点赞数
     */
    private Integer likeNum;

    /**
     * 是否点赞
     */
    private Boolean HasLiked;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = -8158715796400493350L;
}