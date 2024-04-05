package com.ice.job.service;

import com.ice.job.model.entity.RecruitmentCommentLike;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author chenjiahan
 * @description 针对表【recruitment_comment_like(招聘评价点赞)】的数据库操作Service
 * @createDate 2024-04-04 18:36:44
 */
public interface RecruitmentCommentLikeService extends IService<RecruitmentCommentLike> {

    /**
     * 职位评论点赞
     *
     * @param commentId 职位评论id
     * @return 点赞状态
     */
    Integer likeRecruitmentComment(Long commentId, Long userId);

    /**
     * 帖子点赞（内部服务）
     *
     * @param userId    用户id
     * @param commentId 评价id
     * @return
     */
    int doCommentLikeInner(long userId, long commentId);
}
