package com.ice.job.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ice.job.annotation.AuthCheck;
import com.ice.job.common.BaseResponse;
import com.ice.job.common.DeleteRequest;
import com.ice.job.common.ErrorCode;
import com.ice.job.common.ResultUtils;
import com.ice.job.constant.UserConstant;
import com.ice.job.exception.BusinessException;
import com.ice.job.exception.ThrowUtils;
import com.ice.job.model.request.recruitment.RecruitmentAddRequest;
import com.ice.job.model.request.recruitment.RecruitmentQueryRequest;
import com.ice.job.model.request.recruitment.RecruitmentUpdateRequest;
import com.ice.job.model.vo.RecruitmentVO;
import com.ice.job.service.RecruitmentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/2/28 12:54
 */
@RestController
@RequestMapping("/recruitment")
public class RecruitmentController {


    @Resource
    private RecruitmentService recruitmentService;

    /**
     * 添加招聘信息接口
     *
     * @param recruitmentAddRequest 招聘信息请求
     * @return recruitmentId
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.EMPLOYER_ROLE)
    public BaseResponse<Long> addRecruitment(@RequestBody RecruitmentAddRequest recruitmentAddRequest) {
        if (recruitmentAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "招聘信息添加参数为空！");
        }

        Long recruitmentId = recruitmentService.addRecruitment(recruitmentAddRequest);

        return ResultUtils.success(recruitmentId);
    }

    /**
     * 根据 id 获取招聘信息接口
     *
     * @param recruitmentId 招聘信息id
     * @return 招聘信息
     */
    @GetMapping("/get/{recruitmentId}")
    public BaseResponse<RecruitmentVO> getRecruitment(@PathVariable Long recruitmentId) {
        if (recruitmentId == null || recruitmentId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "招聘信息 id 为空");
        }

        RecruitmentVO employeeRecruitmentVO = recruitmentService.getRecruitment(recruitmentId);

        return ResultUtils.success(employeeRecruitmentVO);
    }

    /**
     * 删除招聘信息接口
     *
     * @param deleteRequest 教育 id
     * @return 删除结果
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteRecruitment(@RequestBody DeleteRequest deleteRequest) {
        Long id = deleteRequest.getId();
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "id参数错误");
        }

        boolean result = recruitmentService.deleteRecruitment(id);

        return ResultUtils.success(result);
    }

    /**
     * 更新招聘信息接口
     *
     * @param recruitmentUpdateRequest 招聘信息参数
     * @return 更新结果
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateRecruitment(@RequestBody RecruitmentUpdateRequest recruitmentUpdateRequest) {
        if (recruitmentUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "招聘信息更新参数为空");
        }

        boolean result = recruitmentService.updateRecruitment(recruitmentUpdateRequest);

        return ResultUtils.success(result);
    }

    /**
     * 获取招聘信息分页接口
     *
     * @param recruitmentQueryRequest 查询条件
     * @return 招聘信息分页
     */
    @PostMapping("/page")
    public BaseResponse<Page<RecruitmentVO>> pageRecruitment(@RequestBody RecruitmentQueryRequest recruitmentQueryRequest) {
        long size = recruitmentQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);

        Page<RecruitmentVO> recruitmentVOPage = recruitmentService.pageRecruitment(recruitmentQueryRequest);

        return ResultUtils.success(recruitmentVOPage);
    }
}
