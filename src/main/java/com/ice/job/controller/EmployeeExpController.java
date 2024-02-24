package com.ice.job.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ice.job.common.BaseResponse;
import com.ice.job.common.DeleteRequest;
import com.ice.job.common.ErrorCode;
import com.ice.job.common.ResultUtils;
import com.ice.job.exception.BusinessException;
import com.ice.job.exception.ThrowUtils;
import com.ice.job.model.request.experience.ExperienceAddRequest;
import com.ice.job.model.request.experience.ExperienceQueryRequest;
import com.ice.job.model.request.experience.ExperienceUpdateRequest;
import com.ice.job.model.vo.EmployeeExperienceVO;
import com.ice.job.service.EmployeeExperienceService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/2/23 21:18
 */
@RestController
@RequestMapping("/employee/experience")
public class EmployeeExpController {

    @Resource
    private EmployeeExperienceService employeeExperienceService;

    /**
     * 添加应聘者主要经历接口
     *
     * @param experienceAddRequest 应聘者主要经历请求
     * @return experienceId
     */
    @PostMapping("/add")
    public BaseResponse<Long> addExperience(@RequestBody ExperienceAddRequest experienceAddRequest) {
        if (experienceAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "应聘者主要经历添加参数为空！");
        }

        Long experienceId = employeeExperienceService.addExperience(experienceAddRequest);

        return ResultUtils.success(experienceId);
    }

    /**
     * 根据 id 获取应聘者主要经历接口
     *
     * @param experienceId 应聘者主要经历id
     * @return 应聘者主要经历
     */
    @GetMapping("/get/{experienceId}")
    public BaseResponse<EmployeeExperienceVO> getExperience(@PathVariable Long experienceId) {
        if (experienceId == null || experienceId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "应聘者主要经历 id 为空");
        }

        EmployeeExperienceVO employeeExperienceVO = employeeExperienceService.getExperience(experienceId);

        return ResultUtils.success(employeeExperienceVO);
    }

    /**
     * 删除应聘者主要经历接口
     *
     * @param deleteRequest 应聘者主要 id
     * @return 删除结果
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteExperience(@RequestBody DeleteRequest deleteRequest) {
        Long id = deleteRequest.getId();
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "id参数错误");
        }

        boolean result = employeeExperienceService.deleteExperience(id);

        return ResultUtils.success(result);
    }

    /**
     * 更新应聘者主要经历接口
     *
     * @param experienceUpdateRequest 应聘者主要经历参数
     * @return 更新结果
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateExperience(@RequestBody ExperienceUpdateRequest experienceUpdateRequest) {
        if (experienceUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "应聘者主要经历更新参数为空");
        }

        boolean result = employeeExperienceService.updateExperience(experienceUpdateRequest);

        return ResultUtils.success(result);
    }

    /**
     * 获取应聘者主要经历分页接口
     *
     * @param experienceQueryRequest 查询条件
     * @return 应聘者主要经历分页
     */
    @PostMapping("/page")
    public BaseResponse<Page<EmployeeExperienceVO>> pageExperience(@RequestBody ExperienceQueryRequest experienceQueryRequest) {
        long size = experienceQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);

        Page<EmployeeExperienceVO> experienceVOPage = employeeExperienceService.pageExperience(experienceQueryRequest);

        return ResultUtils.success(experienceVOPage);
    }
}
