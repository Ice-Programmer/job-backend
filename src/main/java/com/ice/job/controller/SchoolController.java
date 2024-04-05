package com.ice.job.controller;

import com.ice.job.common.BaseResponse;
import com.ice.job.common.ResultUtils;
import com.ice.job.model.vo.SchoolVO;
import com.ice.job.service.SchoolService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/4/5 19:37
 */
@RestController
@RequestMapping("/school")
public class SchoolController {

    @Resource
    private SchoolService schoolService;

    /**
     * 获取学校列表接口
     *
     * @return 学校列表
     */
    @GetMapping("/get/list")
    public BaseResponse<List<SchoolVO>> listSchool() {

        List<SchoolVO> schoolVOList = schoolService.listSchool();

        return ResultUtils.success(schoolVOList);
    }
}
