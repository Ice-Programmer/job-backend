package com.ice.job.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ice.job.model.entity.RecruitmentComment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ice.job.model.request.recruitment.RecruitmentCommentAddRequest;
import com.ice.job.model.request.recruitmentcomment.RecruitmentCommentQueryRequest;
import com.ice.job.model.vo.RecruitmentCommentVO;

/**
* @author chenjiahan
* @description 针对表【recruitment_comment(招聘评价)】的数据库操作Service
* @createDate 2024-04-04 14:43:14
*/
public interface RecruitmentCommentService extends IService<RecruitmentComment> {
    /**
     * 招聘评价添加
     *
     * @param recruitmentCommentAddRequest 招聘评价
     * @return id
     */
    Long addRecruitmentComment(RecruitmentCommentAddRequest recruitmentCommentAddRequest);

    /**
     * 获取招聘评论分页
     *
     * @param recruitmentCommentQueryRequest 查询条件
     * @return 招聘评论分页
     */
    Page<RecruitmentCommentVO> pageRecruitmentComment(RecruitmentCommentQueryRequest recruitmentCommentQueryRequest);
}
