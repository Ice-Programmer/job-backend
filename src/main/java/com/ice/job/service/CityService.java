package com.ice.job.service;

import com.ice.job.model.entity.City;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ice.job.model.vo.AddressVO;

import java.util.List;

/**
 * @author chenjiahan
 * @description 针对表【city(城市)】的数据库操作Service
 * @createDate 2024-02-26 12:56:32
 */
public interface CityService extends IService<City> {
    /**
     * 获取所有地区信息接口
     *
     * @return 地区列表（省份-城市）
     */
    List<AddressVO> getAddressList();

}
