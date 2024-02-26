package com.ice.job.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ice.job.model.entity.EmployeeWishCareer;
import com.baomidou.mybatisplus.extension.service.IService;

import com.ice.job.model.request.wishcareer.EmployeeWishCareerAddRequest;
import com.ice.job.model.request.wishcareer.EmployeeWishCareerQueryRequest;
import com.ice.job.model.request.wishcareer.EmployeeWishCareerUpdateRequest;
import com.ice.job.model.vo.EmployeeWishCareerVO;

/**
* @author chenjiahan
* @description 针对表【employee_wish_career】的数据库操作Service
* @createDate 2024-02-26 14:07:53
*/
public interface EmployeeWishCareerService extends IService<EmployeeWishCareer> {

    /**
     * 添加应聘者期望工作
     *
     * @param experienceAddRequest 应聘者期望工作请求
     * @return experienceId
     */
    Long addEmployeeWishCareer(EmployeeWishCareerAddRequest experienceAddRequest);

    /**
     * 根据 id 获取应聘者期望工作
     *
     * @param experienceId 应聘者期望工作id
     * @return 应聘者期望工作
     */
    EmployeeWishCareerVO getEmployeeWishCareer(Long experienceId);

    /**
     * 获取应聘者期望工作分页
     *
     * @param experienceQueryRequest 查询条件
     * @return 应聘者期望工作分页
     */
    Page<EmployeeWishCareerVO> pageEmployeeWishCareer(EmployeeWishCareerQueryRequest experienceQueryRequest);

    /**
     * 删除应聘者期望工作
     *
     * @param id 教育 id
     * @return 删除结果
     */
    boolean deleteEmployeeWishCareer(Long id);

    /**
     * 更新应聘者期望工作
     *
     * @param experienceUpdateRequest 应聘者期望工作参数
     * @return 更新结果
     */
    boolean updateEmployeeWishCareer(EmployeeWishCareerUpdateRequest experienceUpdateRequest);
}
