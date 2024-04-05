package com.ice.job.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ice.job.common.BaseResponse;
import com.ice.job.common.DeleteRequest;
import com.ice.job.common.ErrorCode;
import com.ice.job.common.ResultUtils;
import com.ice.job.exception.BusinessException;
import com.ice.job.exception.ThrowUtils;
import com.ice.job.model.request.education.EducationAddRequest;
import com.ice.job.model.request.education.EducationQueryRequest;
import com.ice.job.model.request.education.EducationUpdateRequest;
import com.ice.job.model.vo.EmployeeEducationVO;
import com.ice.job.service.EducationExperienceService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/2/23 18:38
 */
@RestController
@RequestMapping("/education/experience")
public class EducationExpController {

    @Resource
    private EducationExperienceService educationExperienceService;

    /**
     * 添加教育经历接口
     *
     * @param educationAddRequest 教育经历请求
     * @return educationId
     */
    @PostMapping("/add")
    public BaseResponse<Long> addEducation(@RequestBody EducationAddRequest educationAddRequest) {
        if (educationAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "教育经历添加参数为空！");
        }

        Long educationId = educationExperienceService.addEducation(educationAddRequest);

        return ResultUtils.success(educationId);
    }

    /**
     * 根据 id 获取教育经历接口
     *
     * @param educationId 教育经历id
     * @return 教育经历
     */
    @GetMapping("/get/{educationId}")
    public BaseResponse<EmployeeEducationVO> getEducation(@PathVariable Long educationId) {
        if (educationId == null || educationId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "教育经历 id 为空");
        }

        EmployeeEducationVO employeeEducationVO = educationExperienceService.getEducation(educationId);

        return ResultUtils.success(employeeEducationVO);
    }

    /**
     * 删除教育经历接口
     *
     * @param deleteRequest 教育 id
     * @return 删除结果
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteEducation(@RequestBody DeleteRequest deleteRequest) {
        Long id = deleteRequest.getId();
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "id参数错误");
        }

        boolean result = educationExperienceService.deleteEducation(id);

        return ResultUtils.success(result);
    }

    /**
     * 更新教育经历接口
     *
     * @param educationUpdateRequest 教育经历参数
     * @return 更新结果
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateEducation(@RequestBody EducationUpdateRequest educationUpdateRequest) {
        if (educationUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "教育经历更新参数为空");
        }

        boolean result = educationExperienceService.updateEducation(educationUpdateRequest);

        return ResultUtils.success(result);
    }

    /**
     * 获取教育经历分页接口
     *
     * @param educationQueryRequest 查询条件
     * @return 教育经历分页
     */
    @PostMapping("/page")
    public BaseResponse<Page<EmployeeEducationVO>> pageEducation(@RequestBody EducationQueryRequest educationQueryRequest) {
        long size = educationQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);

        Page<EmployeeEducationVO> educationVOPage = educationExperienceService.pageEducation(educationQueryRequest);

        return ResultUtils.success(educationVOPage);
    }

}
