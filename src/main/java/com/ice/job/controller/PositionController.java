package com.ice.job.controller;

import com.ice.job.common.BaseResponse;
import com.ice.job.common.ResultUtils;
import com.ice.job.model.vo.CareerVO;
import com.ice.job.service.PositionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/2/26 21:15
 */
@RestController
@RequestMapping("/position")
public class PositionController {

    @Resource
    private PositionService positionService;

    /**
     * 获取职业列表接口
     *
     * @return 职业列表
     */
    @GetMapping("/career/get/list")
    public BaseResponse<List<CareerVO>> getCareerList() {
        List<CareerVO> careerVOList = positionService.getCareerList();

        return ResultUtils.success(careerVOList);
    }
}
