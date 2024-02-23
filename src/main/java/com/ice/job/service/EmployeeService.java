package com.ice.job.service;

import com.ice.job.model.entity.Employee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ice.job.model.request.employee.EmployeeUpdateRequest;

/**
* @author chenjiahan
* @description 针对表【employee(应聘者)】的数据库操作Service
* @createDate 2024-02-22 14:01:45
*/
public interface EmployeeService extends IService<Employee> {

    /**
     * 应聘者信息更新
     *
     * @param registerRequest 应聘者基本信息
     * @return 用户id
     */
    Long employUpdate(EmployeeUpdateRequest registerRequest);
}
