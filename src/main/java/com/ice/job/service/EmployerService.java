package com.ice.job.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ice.job.model.entity.Employer;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ice.job.model.request.employer.EmployerQueryRequest;
import com.ice.job.model.request.employer.EmployerUpdateRequest;
import com.ice.job.model.vo.EmployerVO;

/**
* @author chenjiahan
* @description 针对表【employer(招聘者)】的数据库操作Service
* @createDate 2024-02-27 14:05:32
*/
public interface EmployerService extends IService<Employer> {
    /**
     * 招聘者信息更新
     *
     * @param registerRequest 招聘者基本信息
     * @return 用户id
     */
    Long employUpdate(EmployerUpdateRequest registerRequest);

    /**
     * 获取招聘者信息
     *
     * @param userId 用户id
     * @return 招聘者信息
     */
    EmployerVO getEmployerById(Long userId);

    /**
     * 获取招聘者分页
     *
     * @param employerQueryRequest 查询条件
     * @return 招聘者分页
     */
    Page<EmployerVO> pageEmployer(EmployerQueryRequest employerQueryRequest);
}
