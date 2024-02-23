package com.ice.job.service;

import com.ice.job.model.entity.EducationExperience;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ice.job.model.request.education.EducationAddRequest;

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
}
