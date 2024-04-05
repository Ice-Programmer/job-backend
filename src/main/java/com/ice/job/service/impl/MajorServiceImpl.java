package com.ice.job.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.ice.job.constant.CacheConstant;
import com.ice.job.model.entity.Major;
import com.ice.job.model.entity.Major;
import com.ice.job.model.vo.MajorVO;
import com.ice.job.model.vo.MajorVO;
import com.ice.job.service.MajorService;
import com.ice.job.mapper.MajorMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
* @author chenjiahan
* @description 针对表【major(专业)】的数据库操作Service实现
* @createDate 2024-02-22 14:42:10
*/
@Service
public class MajorServiceImpl extends ServiceImpl<MajorMapper, Major>
    implements MajorService{
    private final static Gson GSON = new Gson();

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<MajorVO> listMajor() {

        // 1. 判断缓存中是否存在
        String cacheValue = stringRedisTemplate.opsForValue().get(CacheConstant.MAJOR_LIST_KEY);
        if (!StringUtils.isBlank(cacheValue)) {
            List<MajorVO> cacheMajorList = GSON.fromJson(cacheValue, new TypeToken<List<MajorVO>>() {
            }.getType());
            return cacheMajorList;
        }

        List<Major> majorList = baseMapper.selectList(Wrappers.<Major>lambdaQuery()
                .select(Major::getId, Major::getMajorName));

        if (majorList == null) {
            return Collections.emptyList();
        }

        List<MajorVO> majorVOList = majorList.stream()
                .map(major -> {
                    MajorVO majorVO = new MajorVO();
                    BeanUtils.copyProperties(major, majorVO);
                    return majorVO;
                })
                .collect(Collectors.toList());

        // 将数据放入缓存中
        String majorListJSON = GSON.toJson(majorVOList);
        stringRedisTemplate.opsForValue().set(
                CacheConstant.MAJOR_LIST_KEY,
                majorListJSON,
                CacheConstant.MAJOR_LIST_TTL,
                TimeUnit.SECONDS
        );

        return majorVOList;
    }
}




