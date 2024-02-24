package com.ice.job.controller;

import com.ice.job.common.BaseResponse;
import com.ice.job.common.ResultUtils;
import com.ice.job.model.vo.QualificationVO;
import com.ice.job.service.QualificationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
