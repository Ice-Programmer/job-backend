package com.ice.job.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.ice.job.constant.CacheConstant;
import com.ice.job.mapper.PositionTypeMapper;
import com.ice.job.model.entity.Position;
import com.ice.job.model.entity.PositionType;
import com.ice.job.model.vo.CareerVO;
import com.ice.job.model.vo.PositionVO;
import com.ice.job.service.PositionService;
import com.ice.job.mapper.PositionMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author chenjiahan
 * @description 针对表【position(职业)】的数据库操作Service实现
 * @createDate 2024-02-26 16:05:38
 */
@Service
public class PositionServiceImpl extends ServiceImpl<PositionMapper, Position>
        implements PositionService {

    private final static Gson GSON = new Gson();

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private PositionTypeMapper positionTypeMapper;

    @Override
    public List<CareerVO> getCareerList() {

        // 1. 判断缓存中是否存在
        String cacheValue = stringRedisTemplate.opsForValue().get(CacheConstant.CAREER_LIST_KEY);
        if (!StringUtils.isBlank(cacheValue)) {
            List<CareerVO> cacheCareerList = GSON.fromJson(cacheValue, new TypeToken<List<CareerVO>>() {
            }.getType());
            return cacheCareerList;
        }

        // 获取所有职业类型
        List<PositionType> positionTypeList = positionTypeMapper.selectList(Wrappers.<PositionType>lambdaQuery()
                .select(PositionType::getId, PositionType::getPositionTypeName));

        // 获取所有职业
        List<Position> positionList = baseMapper.selectList(Wrappers.<Position>lambdaQuery()
                .select(Position::getId, Position::getPositionName,
                        Position::getPositionDescript, Position::getPositionType));

        List<CareerVO> careerVOList = positionTypeList.stream()
                .map(positionType -> {
                    CareerVO careerVO = new CareerVO();
                    careerVO.setId(positionType.getId());
                    careerVO.setPositionType(positionType.getPositionTypeName());

                    List<PositionVO> positionVOList = positionList.stream()
                            .filter(position ->
                                    position.getPositionType().equals(positionType.getId())
                            ).map(position -> {
                                PositionVO positionVO = new PositionVO();
                                BeanUtils.copyProperties(position, positionVO);
                                return positionVO;
                            }).collect(Collectors.toList());

                    careerVO.setPositionInfoList(positionVOList);
                    return careerVO;
                }).collect(Collectors.toList());

        // 将数据放入缓存中
        String addressListJSON = GSON.toJson(careerVOList);
        stringRedisTemplate.opsForValue().set(
                CacheConstant.CAREER_LIST_KEY,
                addressListJSON,
                CacheConstant.CAREER_LIST_TTL,
                TimeUnit.SECONDS
        );

        return careerVOList;
    }
}




