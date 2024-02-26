package com.ice.job.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ice.job.common.BaseResponse;
import com.ice.job.common.DeleteRequest;
import com.ice.job.common.ErrorCode;
import com.ice.job.common.ResultUtils;
import com.ice.job.exception.BusinessException;
import com.ice.job.exception.ThrowUtils;
import com.ice.job.model.request.wishcareer.EmployeeWishCareerAddRequest;
import com.ice.job.model.request.wishcareer.EmployeeWishCareerQueryRequest;
import com.ice.job.model.request.wishcareer.EmployeeWishCareerUpdateRequest;
import com.ice.job.model.vo.EmployeeWishCareerVO;
import com.ice.job.service.EmployeeWishCareerService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/2/26 14:02
 */
@RestController
@RequestMapping("/employee/career/wish")
public class EmployeeWishCareerController {

    @Resource
    private EmployeeWishCareerService employeeWishCareerService;


    /**
     * 添加应聘者期望工作接口
     *
     * @param employeeWishCareerAddRequest 应聘者期望工作请求
     * @return employeeWishCareerId
     */
    @PostMapping("/add")
    public BaseResponse<Long> addEmployeeWishCareer(@RequestBody EmployeeWishCareerAddRequest employeeWishCareerAddRequest) {
        if (employeeWishCareerAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "应聘者期望工作添加参数为空！");
        }

        Long employeeWishCareerId = employeeWishCareerService
                .addEmployeeWishCareer(employeeWishCareerAddRequest);

        return ResultUtils.success(employeeWishCareerId);
    }

    /**
     * 根据 id 获取应聘者期望工作接口
     *
     * @param employeeWishCareerId 应聘者期望工作id
     * @return 应聘者期望工作
     */
    @GetMapping("/get/{employeeWishCareerId}")
    public BaseResponse<EmployeeWishCareerVO> getEmployeeWishCareer(@PathVariable Long employeeWishCareerId) {
        if (employeeWishCareerId == null || employeeWishCareerId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "应聘者期望工作 id 为空");
        }

        EmployeeWishCareerVO employeeWishCareerVO = employeeWishCareerService
                .getEmployeeWishCareer(employeeWishCareerId);

        return ResultUtils.success(employeeWishCareerVO);
    }

    /**
     * 删除应聘者期望工作接口
     *
     * @param deleteRequest 应聘者主要 id
     * @return 删除结果
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteEmployeeWishCareer(@RequestBody DeleteRequest deleteRequest) {
        Long id = deleteRequest.getId();
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "id参数错误");
        }

        boolean result = employeeWishCareerService.deleteEmployeeWishCareer(id);

        return ResultUtils.success(result);
    }

    /**
     * 更新应聘者期望工作接口
     *
     * @param employeeWishCareerUpdateRequest 应聘者期望工作参数
     * @return 更新结果
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateEmployeeWishCareer(@RequestBody EmployeeWishCareerUpdateRequest employeeWishCareerUpdateRequest) {
        if (employeeWishCareerUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "应聘者期望工作更新参数为空");
        }

        boolean result = employeeWishCareerService
                .updateEmployeeWishCareer(employeeWishCareerUpdateRequest);

        return ResultUtils.success(result);
    }

    /**
     * 获取应聘者期望工作分页接口
     *
     * @param employeeWishCareerQueryRequest 查询条件
     * @return 应聘者期望工作分页
     */
    @PostMapping("/page")
    public BaseResponse<Page<EmployeeWishCareerVO>> pageEmployeeWishCareer(@RequestBody EmployeeWishCareerQueryRequest employeeWishCareerQueryRequest) {
        long size = employeeWishCareerQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);

        Page<EmployeeWishCareerVO> employeeWishCareerVOPage = employeeWishCareerService
                .pageEmployeeWishCareer(employeeWishCareerQueryRequest);

        return ResultUtils.success(employeeWishCareerVOPage);
    }
}
