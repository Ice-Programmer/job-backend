package com.ice.job.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ice.job.common.BaseResponse;
import com.ice.job.common.ErrorCode;
import com.ice.job.common.ResultUtils;
import com.ice.job.exception.BusinessException;
import com.ice.job.exception.ThrowUtils;
import com.ice.job.model.request.recruitment.RecruitmentCommentAddRequest;
import com.ice.job.model.request.recruitmentcomment.RecruitmentCommentQueryRequest;
import com.ice.job.model.vo.RecruitmentCommentVO;
import com.ice.job.model.vo.RecruitmentVO;
import com.ice.job.service.RecruitmentCommentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/4/4 14:57
 */
@RestController
@RequestMapping("/recruitment/comment")
public class RecruitmentCommentController {

    @Resource
    private RecruitmentCommentService recruitmentCommentService;

    /**
     * 招聘评价添加接口
     *
     * @param recruitmentCommentAddRequest 招聘评价
     * @return id
     */
    @PostMapping("/add")
    public BaseResponse<Long> addRecruitmentComment(
            @RequestBody RecruitmentCommentAddRequest recruitmentCommentAddRequest) {
        if (recruitmentCommentAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "评价参数为空！");
        }

        Long id = recruitmentCommentService.addRecruitmentComment(recruitmentCommentAddRequest);

        return ResultUtils.success(id);
    }

    /**
     * 获取招聘评论分页接口
     *
     * @param recruitmentCommentQueryRequest 查询条件
     * @return 招聘评论分页
     */
    @PostMapping("/page/vo")
    public BaseResponse<Page<RecruitmentCommentVO>> pageRecruitmentComment(
            @RequestBody RecruitmentCommentQueryRequest recruitmentCommentQueryRequest) {
        long size = recruitmentCommentQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);

        Page<RecruitmentCommentVO> recruitmentCommentVOPage =
                recruitmentCommentService.pageRecruitmentComment(recruitmentCommentQueryRequest);

        return ResultUtils.success(recruitmentCommentVOPage);
    }
}
