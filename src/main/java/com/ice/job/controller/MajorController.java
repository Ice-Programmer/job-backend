package com.ice.job.controller;

import com.ice.job.common.BaseResponse;
import com.ice.job.common.ResultUtils;
import com.ice.job.model.vo.MajorVO;
import com.ice.job.service.MajorService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/4/5 20:49
 */
@RestController
@RequestMapping("/major")
public class MajorController {

    @Resource
    private MajorService majorService;

    /**
     * 获取专业列表接口
     *
     * @return 专业列表
     */
    @GetMapping("/get/list")
    public BaseResponse<List<MajorVO>> listMajor() {

        List<MajorVO> majorVOList = majorService.listMajor();

        return ResultUtils.success(majorVOList);
    }
}
