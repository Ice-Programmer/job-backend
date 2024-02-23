package com.ice.job.controller;

import com.ice.job.common.BaseResponse;
import com.ice.job.common.ErrorCode;
import com.ice.job.common.ResultUtils;
import com.ice.job.exception.BusinessException;
import com.ice.job.model.request.employee.EmployeeRegisterRequest;
import com.ice.job.service.EmployeeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/2/22 13:57
 */
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Resource
    private EmployeeService employeeService;

    /**
     * 应聘者信息更新接口
     *
     * @param registerRequest 应聘者基本信息
     * @return 用户id
     */
    @PostMapping("/update")
    public BaseResponse<Long> employUpdate(@RequestBody EmployeeRegisterRequest registerRequest) {
        if (registerRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "注册信息为空");
        }

        Long userId = employeeService.employUpdate(registerRequest);

        return ResultUtils.success(userId);
    }
}
