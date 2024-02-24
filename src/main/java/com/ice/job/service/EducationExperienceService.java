package com.ice.job.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ice.job.model.entity.EducationExperience;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ice.job.model.request.education.EducationAddRequest;
import com.ice.job.model.request.education.EducationQueryRequest;
import com.ice.job.model.request.education.EducationUpdateRequest;
import com.ice.job.model.vo.EducationVO;

/**
 * @author chenjiahan
 * @description 针对表【education_experience】的数据库操作Service
 * @createDate 2024-02-23 18:36:24
 */
public interface EducationExperienceService extends IService<EducationExperience> {

    /**
     * 添加教育经历
     *
     * @param educationAddRequest 教育经历请求
     * @return educationId
     */
    Long addEducation(EducationAddRequest educationAddRequest);

    /**
     * 根据 id 获取教育经历
     *
     * @param educationId 教育经历id
     * @return 教育经历
     */
    EducationVO getEducation(Long educationId);

    /**
     * 获取教育经历分页
     *
     * @param educationQueryRequest 查询条件
     * @return 教育经历分页
     */
    Page<EducationVO> pageEducation(EducationQueryRequest educationQueryRequest);

    /**
     * 删除教育经历
     *
     * @param id 教育 id
     * @return 删除结果
     */
    boolean deleteEducation(Long id);

    /**
     * 更新教育经历
     *
     * @param educationUpdateRequest 教育经历参数
     * @return 更新结果
     */
    boolean updateEducation(EducationUpdateRequest educationUpdateRequest);
}
