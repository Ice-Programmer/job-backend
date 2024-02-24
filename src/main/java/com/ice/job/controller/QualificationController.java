package com.ice.job.controller;

import com.ice.job.common.BaseResponse;
import com.ice.job.common.ResultUtils;
import com.ice.job.model.request.qualification.QualificationEmployeeUpdateRequest;
import com.ice.job.model.vo.QualificationVO;
import com.ice.job.service.QualificationService;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/2/24 16:10
 */
@RestController
@RequestMapping("/qualification")
public class QualificationController {

    @Resource
    private QualificationService qualificationService;

    /**
     * 获取资格证书列表接口
     *
     * @return 资格证书列表
     */
    @GetMapping("/get/list")
    public BaseResponse<List<QualificationVO>> getQualificationList() {

        List<QualificationVO> qualificationList = qualificationService.getQualificationList();

        return ResultUtils.success(qualificationList);
    }

    /**
     * 应聘者更新资格证书
     *
     * @param qualificationEmployeeUpdateRequest 资格证书 id 列表
     * @return 处理结果
     */
    @PostMapping("/employee/update")
    public BaseResponse<Boolean> updateEmployeeQualification(@RequestBody QualificationEmployeeUpdateRequest qualificationEmployeeUpdateRequest) {
        List<Long> qualificationIdList = qualificationEmployeeUpdateRequest.getQualificationIdList();

        boolean result = qualificationService.updateEmployeeQualification(qualificationIdList);

        return ResultUtils.success(result);
    }
}
