package com.ice.job.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.ice.job.constant.CacheConstant;
import com.ice.job.mapper.SchoolMapper;
import com.ice.job.model.entity.School;
import com.ice.job.model.vo.SchoolVO;
import com.ice.job.service.SchoolService;
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
 * @description 针对表【school(专业)】的数据库操作Service实现
 * @createDate 2024-02-22 14:47:41
 */
@Service
public class SchoolServiceImpl extends ServiceImpl<SchoolMapper, School>
        implements SchoolService {

    private final static Gson GSON = new Gson();

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<SchoolVO> listSchool() {

        // 1. 判断缓存中是否存在
        String cacheValue = stringRedisTemplate.opsForValue().get(CacheConstant.SCHOOL_LIST_KEY);
        if (!StringUtils.isBlank(cacheValue)) {
            List<SchoolVO> cacheSchoolList = GSON.fromJson(cacheValue, new TypeToken<List<SchoolVO>>() {
            }.getType());
            return cacheSchoolList;
        }


        List<School> schoolList = baseMapper.selectList(Wrappers.<School>lambdaQuery()
                .select(School::getId, School::getSchoolName));

        if (schoolList == null) {
            return Collections.emptyList();
        }

        List<SchoolVO> schoolVOList = schoolList.stream()
                .map(school -> {
                    SchoolVO schoolVO = new SchoolVO();
                    BeanUtils.copyProperties(school, schoolVO);
                    return schoolVO;
                })
                .collect(Collectors.toList());

        // 将数据放入缓存中
        String schoolListJSON = GSON.toJson(schoolVOList);
        stringRedisTemplate.opsForValue().set(
                CacheConstant.SCHOOL_LIST_KEY,
                schoolListJSON,
                CacheConstant.SCHOOL_LIST_TTL,
                TimeUnit.SECONDS
        );

        return schoolVOList;
    }
}




