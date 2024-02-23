package com.ice.job.controller;

import com.ice.job.common.BaseResponse;
import com.ice.job.common.ErrorCode;
import com.ice.job.common.ResultUtils;
import com.ice.job.exception.BusinessException;
import com.ice.job.model.request.education.EducationAddRequest;
import com.ice.job.service.EducationExperienceService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
