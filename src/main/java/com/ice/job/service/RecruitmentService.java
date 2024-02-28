package com.ice.job.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ice.job.model.entity.Recruitment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ice.job.model.request.recruitment.RecruitmentAddRequest;
import com.ice.job.model.request.recruitment.RecruitmentQueryRequest;
import com.ice.job.model.request.recruitment.RecruitmentUpdateRequest;
import com.ice.job.model.vo.RecruitmentVO;

/**
* @author chenjiahan
* @description 针对表【recruitment(招聘信息)】的数据库操作Service
* @createDate 2024-02-28 12:53:31
*/
public interface RecruitmentService extends IService<Recruitment> {
    /**
     * 添加招聘信息
     *
     * @param recruitmentAddRequest 招聘信息请求
     * @return recruitmentId
     */
    Long addRecruitment(RecruitmentAddRequest recruitmentAddRequest);

    /**
     * 根据 id 获取招聘信息
     *
     * @param recruitmentId 招聘信息id
     * @return 招聘信息
     */
    RecruitmentVO getRecruitment(Long recruitmentId);

    /**
     * 获取招聘信息分页
     *
     * @param recruitmentQueryRequest 查询条件
     * @return 招聘信息分页
     */
    Page<RecruitmentVO> pageRecruitment(RecruitmentQueryRequest recruitmentQueryRequest);

    /**
     * 删除招聘信息
     *
     * @param id 教育 id
     * @return 删除结果
     */
    boolean deleteRecruitment(Long id);

    /**
     * 更新招聘信息
     *
     * @param recruitmentUpdateRequest 招聘信息参数
     * @return 更新结果
     */
    boolean updateRecruitment(RecruitmentUpdateRequest recruitmentUpdateRequest);
}
