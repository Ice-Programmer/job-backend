package com.ice.job.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ice.job.model.entity.EmployeeExperience;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ice.job.model.request.experience.ExperienceAddRequest;
import com.ice.job.model.request.experience.ExperienceQueryRequest;
import com.ice.job.model.request.experience.ExperienceUpdateRequest;
import com.ice.job.model.vo.EmployeeExperienceVO;

/**
* @author chenjiahan
* @description 针对表【employee_experience(应聘者经历)】的数据库操作Service
* @createDate 2024-02-23 21:12:18
*/
public interface EmployeeExperienceService extends IService<EmployeeExperience> {

    /**
     * 添加应聘者主要经历
     *
     * @param experienceAddRequest 应聘者主要经历请求
     * @return experienceId
     */
    Long addExperience(ExperienceAddRequest experienceAddRequest);

    /**
     * 根据 id 获取应聘者主要经历
     *
     * @param experienceId 应聘者主要经历id
     * @return 应聘者主要经历
     */
    EmployeeExperienceVO getExperience(Long experienceId);

    /**
     * 获取应聘者主要经历分页
     *
     * @param experienceQueryRequest 查询条件
     * @return 应聘者主要经历分页
     */
    Page<EmployeeExperienceVO> pageExperience(ExperienceQueryRequest experienceQueryRequest);

    /**
     * 删除应聘者主要经历
     *
     * @param id 教育 id
     * @return 删除结果
     */
    boolean deleteExperience(Long id);

    /**
     * 更新应聘者主要经历
     *
     * @param experienceUpdateRequest 应聘者主要经历参数
     * @return 更新结果
     */
    boolean updateExperience(ExperienceUpdateRequest experienceUpdateRequest);
}
