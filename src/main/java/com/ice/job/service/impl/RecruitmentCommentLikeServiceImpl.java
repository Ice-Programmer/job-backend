package com.ice.job.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ice.job.common.ErrorCode;
import com.ice.job.exception.BusinessException;
import com.ice.job.mapper.RecruitmentCommentMapper;
import com.ice.job.model.entity.RecruitmentComment;
import com.ice.job.model.entity.RecruitmentCommentLike;
import com.ice.job.service.RecruitmentCommentLikeService;
import com.ice.job.mapper.RecruitmentCommentLikeMapper;
import org.springframework.aop.framework.AopContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author chenjiahan
 * @description 针对表【recruitment_comment_like(招聘评价点赞)】的数据库操作Service实现
 * @createDate 2024-04-04 18:36:44
 */
@Service
public class RecruitmentCommentLikeServiceImpl extends ServiceImpl<RecruitmentCommentLikeMapper, RecruitmentCommentLike>
        implements RecruitmentCommentLikeService {

    @Resource
    private RecruitmentCommentMapper recruitmentCommentMapper;

    @Override
    public Integer likeRecruitmentComment(Long commentId, Long userId) {
        // 判断实体是否存在，根据类别获取实体
        RecruitmentComment recruitmentComment = recruitmentCommentMapper
                .selectOne(Wrappers.<RecruitmentComment>lambdaQuery()
                        .eq(RecruitmentComment::getId, commentId)
                        .select(RecruitmentComment::getId, RecruitmentComment::getLikeNum)
                        .last("limit 1"));
        if (recruitmentComment == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "职位评论不存在");
        }
        // 是否已点赞
        RecruitmentCommentLikeService postThumbService = (RecruitmentCommentLikeService) AopContext.currentProxy();
        synchronized (String.valueOf(userId).intern()) {
            return postThumbService.doCommentLikeInner(userId, commentId);
        }
    }

    /**
     * 封装了事务的方法
     *
     * @param userId    用户id
     * @param commentId 评价id
     * @return 点赞数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int doCommentLikeInner(long userId, long commentId) {
        RecruitmentCommentLike commentLike = baseMapper.selectOne(Wrappers.<RecruitmentCommentLike>lambdaQuery()
                .eq(RecruitmentCommentLike::getUserId, userId)
                .eq(RecruitmentCommentLike::getRecruitmentCommentId, commentId)
                .last("limit 1"));
        boolean result;
        // 已点赞
        if (commentLike != null) {
            result = baseMapper.deleteById(commentLike.getId()) != 0;
            if (result) {
                // 点赞数 - 1
                result = recruitmentCommentMapper.update(null, Wrappers.<RecruitmentComment>lambdaUpdate()
                        .eq(RecruitmentComment::getId, commentId)
                        .gt(RecruitmentComment::getLikeNum, 0)
                        .setSql("likeNum = likeNum - 1")) != 0;
                return result ? -1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        } else {
            // 未点赞
            RecruitmentCommentLike recruitmentCommentLike = new RecruitmentCommentLike();
            recruitmentCommentLike.setUserId(userId);
            recruitmentCommentLike.setRecruitmentCommentId(commentId);
            result = baseMapper.insert(recruitmentCommentLike) != 0;
            if (result) {
                // 点赞数 + 1
                result = recruitmentCommentMapper.update(null, Wrappers.<RecruitmentComment>lambdaUpdate()
                        .eq(RecruitmentComment::getId, commentId)
                        .setSql("likeNum = likeNum + 1")) != 0;
                return result ? 1 : 0;
            } else {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            }
        }
    }
}




