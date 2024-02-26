package com.ice.job.controller;

import com.ice.job.common.BaseResponse;
import com.ice.job.common.ResultUtils;
import com.ice.job.model.vo.AddressVO;
import com.ice.job.service.CityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/2/26 12:52
 */
@RestController
@RequestMapping("/city")
public class CityController {

    @Resource
    private CityService cityService;


    @GetMapping("/get/list")
    public BaseResponse<List<AddressVO>> getAddressList() {
        List<AddressVO> addressVOList = cityService.getAddressList();
        return ResultUtils.success(addressVOList);
    }

}
