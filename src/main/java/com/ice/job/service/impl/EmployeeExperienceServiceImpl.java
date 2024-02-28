package com.ice.job.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ice.job.common.ErrorCode;
import com.ice.job.constant.CacheConstant;
import com.ice.job.constant.CommonConstant;
import com.ice.job.constant.UserHolder;
import com.ice.job.exception.BusinessException;
import com.ice.job.exception.ThrowUtils;
import com.ice.job.mapper.EmployeeExperienceMapper;
import com.ice.job.model.entity.EmployeeExperience;
import com.ice.job.model.enums.ExperienceTypeEnum;
import com.ice.job.model.request.experience.ExperienceAddRequest;
import com.ice.job.model.request.experience.ExperienceQueryRequest;
import com.ice.job.model.request.experience.ExperienceUpdateRequest;
import com.ice.job.model.vo.EmployeeExperienceVO;
import com.ice.job.service.EmployeeExperienceService;
import com.ice.job.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author chenjiahan
* @description 针对表【employee_experience(应聘者经历)】的数据库操作Service实现
* @createDate 2024-02-23 21:12:18
*/
@Service
public class EmployeeExperienceServiceImpl extends ServiceImpl<EmployeeExperienceMapper, EmployeeExperience>
    implements EmployeeExperienceService{

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Long addExperience(ExperienceAddRequest experienceAddRequest) {

        // 校验参数
        EmployeeExperience experience = new EmployeeExperience();
        BeanUtils.copyProperties(experienceAddRequest, experience);
        validExperience(experience);

        // 保存用户信息
        Long userId = UserHolder.getUser().getId();
        experience.setUserId(userId);

        // 插入应聘者主要经历信息
        baseMapper.insert(experience);

        // 删除缓存中应聘者信息
        stringRedisTemplate.delete(CacheConstant.USER_EMPLOYEE_KEY + userId);

        return experience.getId();
    }

    @Override
    public EmployeeExperienceVO getExperience(Long experienceId) {
        EmployeeExperience experience = baseMapper.selectOne(Wrappers.<EmployeeExperience>lambdaQuery()
                .eq(EmployeeExperience::getId, experienceId)
                .last("limit 1"));

        return getEmployeeExperienceVO(experience);
    }

    @Override
    public Page<EmployeeExperienceVO> pageExperience(ExperienceQueryRequest experienceQueryRequest) {
        long current = experienceQueryRequest.getCurrent();
        long size = experienceQueryRequest.getPageSize();

        Page<EmployeeExperience> experiencePage = baseMapper.selectPage(new Page<>(current, size),
                getQueryWrapper(experienceQueryRequest));

        List<EmployeeExperience> experienceList = experiencePage.getRecords();

        // 包装VO类
        List<EmployeeExperienceVO> experienceVOList = experienceList.stream()
                .map(this::getEmployeeExperienceVO)
                .collect(Collectors.toList());

        Page<EmployeeExperienceVO> experienceVOPage = new Page<>(experiencePage.getCurrent(), experiencePage.getSize(), experiencePage.getTotal());

        experienceVOPage.setRecords(experienceVOList);

        return experienceVOPage;
    }

    @Override
    public boolean deleteExperience(Long id) {

        // 判断是否存在
        boolean flag = baseMapper.delete(Wrappers.<EmployeeExperience>lambdaQuery()
                .eq(EmployeeExperience::getId, id)) != 0;

        if (!flag) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "删除失败，该应聘者主要经历不存在或操作失败！");
        }

        // 删除缓存中应聘者信息
        Long userId = UserHolder.getUser().getId();
        stringRedisTemplate.delete(CacheConstant.USER_EMPLOYEE_KEY + userId);

        return true;
    }

    @Override
    public boolean updateExperience(ExperienceUpdateRequest experienceUpdateRequest) {
        // 判断是否存在
        boolean exists = baseMapper.exists(Wrappers.<EmployeeExperience>lambdaQuery()
                .eq(EmployeeExperience::getId, experienceUpdateRequest.getId()));
        ThrowUtils.throwIf(!exists, ErrorCode.NOT_FOUND_ERROR, "应聘者主要经历不存在！");

        // 校验参数
        EmployeeExperience experience = new EmployeeExperience();
        BeanUtils.copyProperties(experienceUpdateRequest, experience);
        validExperience(experience);

        // 删除缓存内容
        Long userId = UserHolder.getUser().getId();
        stringRedisTemplate.delete(CacheConstant.USER_EMPLOYEE_KEY + userId);

        // 更新数据库
        baseMapper.updateById(experience);
        return true;
    }

    /**
     * 封装包装类
     *
     * @param experience 应聘者主要经历
     * @return experienceVO
     */
    private EmployeeExperienceVO getEmployeeExperienceVO(EmployeeExperience experience) {
        if (experience == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "应聘者主要经历不存在！");
        }
        EmployeeExperienceVO experienceVO = new EmployeeExperienceVO();
        BeanUtils.copyProperties(experience, experienceVO);

        return experienceVO;
    }


    /**
     * 校验应聘者主要经历参数
     *
     * @param experience 应聘者主要经历
     */
    private void validExperience(EmployeeExperience experience) {
        String experienceName = experience.getExperienceName();
        String beginTime = experience.getBeginTime();
        String endTime = experience.getEndTime();
        String jobRole = experience.getJobRole();
        String experienceDescript = experience.getExperienceDescript();

        if (StringUtils.isAnyBlank(experienceDescript, experienceName, beginTime, endTime, jobRole)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 1. 校验应聘者主要经历
        Integer experienceType = experience.getExperienceType();
        if (experienceType == null || !ExperienceTypeEnum.getIntegerValues().contains(experienceType)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "应聘者主要经历类型错误");
        }
    }

    /**
     * 拼接查询条件
     *
     * @param experienceQueryRequest 查询条件
     * @return QueryWrapper
     */
    private QueryWrapper<EmployeeExperience> getQueryWrapper(ExperienceQueryRequest experienceQueryRequest) {
        QueryWrapper<EmployeeExperience> queryWrapper = new QueryWrapper<>();

        if (experienceQueryRequest == null) {
            return queryWrapper;
        }

        Long id = experienceQueryRequest.getId();
        List<Long> ids = experienceQueryRequest.getIds();
        Long userId = experienceQueryRequest.getUserId();
        String searchText = experienceQueryRequest.getSearchText();
        Integer experienceType = experienceQueryRequest.getExperienceType();
        String sortField = experienceQueryRequest.getSortField();
        String sortOrder = experienceQueryRequest.getSortOrder();

        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        if (!CollectionUtils.isEmpty(ids)) {
            queryWrapper.in("id", id);
        }

        if (searchText != null) {
            queryWrapper.like("experienceName", searchText);
        }

        if (experienceType != null &&
                ExperienceTypeEnum.getIntegerValues().contains(experienceType)) {
            queryWrapper.eq("experienceType", experienceType);
        }

        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
}




