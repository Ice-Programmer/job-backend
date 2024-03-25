package com.ice.job.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ice.job.annotation.AuthCheck;
import com.ice.job.common.BaseResponse;
import com.ice.job.common.ErrorCode;
import com.ice.job.common.ResultUtils;
import com.ice.job.constant.UserConstant;
import com.ice.job.constant.UserHolder;
import com.ice.job.exception.BusinessException;
import com.ice.job.exception.ThrowUtils;
import com.ice.job.model.request.employer.EmployerUpdateRequest;
import com.ice.job.model.request.employer.EmployerQueryRequest;
import com.ice.job.model.vo.EmployerVO;
import com.ice.job.model.vo.EmployerVO;
import com.ice.job.service.EmployerService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/2/27 13:55
 */
@RestController
@RequestMapping("/employer")
public class EmployerController {


    @Resource
    private EmployerService employerService;

    /**
     * 招聘者信息更新接口
     *
     * @param registerRequest 招聘者基本信息
     * @return 用户id
     */
    @PostMapping("/update")
    public BaseResponse<Long> employUpdate(@RequestBody EmployerUpdateRequest registerRequest) {
        if (registerRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "注册信息为空");
        }

        Long userId = employerService.employUpdate(registerRequest);

        return ResultUtils.success(userId);
    }


    /**
     * 获取招聘者信息接口
     *
     * @param userId 用户id
     * @return 招聘者信息
     */
    @GetMapping("/get/{userId}")
    public BaseResponse<EmployerVO> getEmployerById(@PathVariable("userId") Long userId) {
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户 id 为空");
        }

        EmployerVO employerVO = employerService.getEmployerById(userId);

        return ResultUtils.success(employerVO);
    }

    /**
     * 获取当前招聘者信息
     *
     * @return 招聘者详细信息
     */
    @GetMapping("/get/current")
    public BaseResponse<EmployerVO> getCurrentEmployer() {
        // 获取当前用户
        Long userId = UserHolder.getUser().getId();

        EmployerVO currentEmployer = employerService.getEmployerById(userId);

        return ResultUtils.success(currentEmployer);
    }

    /**
     * 获取招聘者分页接口
     *
     * @param employerQueryRequest 查询条件
     * @return 招聘者分页
     */
    @PostMapping("/page")
    public BaseResponse<Page<EmployerVO>> pageEmployer(@RequestBody EmployerQueryRequest employerQueryRequest) {
        long size = employerQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);

        Page<EmployerVO> employerVOPage = employerService.pageEmployer(employerQueryRequest);

        return ResultUtils.success(employerVOPage);
    }
}
