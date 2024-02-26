package com.ice.job.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.ice.job.constant.CacheConstant;
import com.ice.job.mapper.ProvinceTypeMapper;
import com.ice.job.model.entity.City;
import com.ice.job.model.entity.ProvinceType;
import com.ice.job.model.vo.AddressVO;
import com.ice.job.service.CityService;
import com.ice.job.mapper.CityMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author chenjiahan
 * @description 针对表【city(城市)】的数据库操作Service实现
 * @createDate 2024-02-26 12:56:32
 */
@Service
public class CityServiceImpl extends ServiceImpl<CityMapper, City>
        implements CityService {

    private final static Gson GSON = new Gson();

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ProvinceTypeMapper provinceTypeMapper;

    @Override
    public List<AddressVO> getAddressList() {

        // 1. 判断缓存中是否存在
        String cacheValue = stringRedisTemplate.opsForValue().get(CacheConstant.CITY_LIST_KEY);
        if (!StringUtils.isBlank(cacheValue)) {
            List<AddressVO> cacheCityList = GSON.fromJson(cacheValue, new TypeToken<List<AddressVO>>() {
            }.getType());
            return cacheCityList;
        }

        // 获取所有省份
        List<ProvinceType> provinceList = provinceTypeMapper.selectList(
                Wrappers.<ProvinceType>lambdaQuery()
                .select(ProvinceType::getId, ProvinceType::getProvinceName)
        );

        List<City> cityList = baseMapper.selectList(Wrappers.<City>lambdaQuery()
                .select(City::getId, City::getCityName, City::getProvinceType));

        List<AddressVO> addressList = provinceList.stream()
                .map(province -> {
                    AddressVO addressVO = new AddressVO();
                    addressVO.setId(province.getId());
                    addressVO.setProvinceName(province.getProvinceName());

                    List<AddressVO.CityVO> cityVOList = cityList.stream()
                            .filter(city -> city.getProvinceType().equals(province.getId()))
                            .map(city -> {
                                AddressVO.CityVO cityVO = new AddressVO.CityVO();
                                cityVO.setId(city.getId());
                                cityVO.setCityName(city.getCityName());
                                return cityVO;
                            })
                            .collect(Collectors.toList());

                    addressVO.setCityList(cityVOList);
                    return addressVO;
                })
                .collect(Collectors.toList());

        // 将数据放入缓存中
        String addressListJSON = GSON.toJson(addressList);
        stringRedisTemplate.opsForValue().set(
                CacheConstant.CITY_LIST_KEY,
                addressListJSON,
                CacheConstant.CITY_LIST_TTL,
                TimeUnit.SECONDS
        );

        return addressList;
    }
}




