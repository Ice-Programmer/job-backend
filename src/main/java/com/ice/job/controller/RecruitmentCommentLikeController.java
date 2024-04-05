package com.ice.job.controller;

import com.ice.job.common.BaseResponse;
import com.ice.job.common.ErrorCode;
import com.ice.job.common.ResultUtils;
import com.ice.job.constant.UserHolder;
import com.ice.job.exception.BusinessException;
import com.ice.job.service.RecruitmentCommentLikeService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 职位评论点赞 Controller
 *
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/4/4 18:39
 */
@RestController
@RequestMapping("/recruitment/comment/like")
public class RecruitmentCommentLikeController {

    @Resource
    private RecruitmentCommentLikeService recruitmentCommentLikeService;

    /**
     * 职位评论点赞接口
     *
     * @param id 职位评论id
     * @return 点赞状态
     */
    @PostMapping("/{id}")
    public BaseResponse<Integer> likeOrDislikeRecruitmentComment(
            @PathVariable("id") Long id) {
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "id 为空");
        }

        Long userId = UserHolder.getUser().getId();

        Integer likeNum = recruitmentCommentLikeService.likeRecruitmentComment(id, userId);

        return ResultUtils.success(likeNum);
    }
}
