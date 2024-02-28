package com.ice.job.controller;

import com.ice.job.annotation.AuthCheck;
import com.ice.job.common.BaseResponse;
import com.ice.job.common.ErrorCode;
import com.ice.job.common.ResultUtils;
import com.ice.job.constant.UserConstant;
import com.ice.job.constant.UserHolder;
import com.ice.job.exception.BusinessException;
import com.ice.job.model.entity.User;
import com.ice.job.model.request.employee.EmployeeUpdateRequest;
import com.ice.job.model.vo.EmployeeVO;
import com.ice.job.service.EmployeeService;
import org.springframework.web.bind.annotation.*;

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
    public BaseResponse<Long> employUpdate(@RequestBody EmployeeUpdateRequest registerRequest) {
        if (registerRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "注册信息为空");
        }

        Long userId = employeeService.employUpdate(registerRequest);

        return ResultUtils.success(userId);
    }


    /**
     * 获取应聘者信息接口
     *
     * @param userId 用户id
     * @return 应聘者信息
     */
    @GetMapping("/get/{userId}")
    @AuthCheck(mustRole = UserConstant.EMPLOYER_ROLE)
    public BaseResponse<EmployeeVO> getEmployeeById(@PathVariable("userId") Long userId) {
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户 id 为空");
        }

        EmployeeVO employeeVO = employeeService.getEmployeeById(userId);

        return ResultUtils.success(employeeVO);
    }

    /**
     * 获取当前应聘者信息
     *
     * @return 应聘者详细信息
     */
    @GetMapping("/get/current")
    public BaseResponse<EmployeeVO> getCurrentEmployee() {
        // 获取当前用户
        Long userId = UserHolder.getUser().getId();

        EmployeeVO currentEmployee = employeeService.getEmployeeById(userId);

        return ResultUtils.success(currentEmployee);
    }
}
