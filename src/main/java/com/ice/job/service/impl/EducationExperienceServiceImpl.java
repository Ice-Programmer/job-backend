package com.ice.job.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ice.job.common.ErrorCode;
import com.ice.job.constant.CommonConstant;
import com.ice.job.constant.UserHolder;
import com.ice.job.exception.BusinessException;
import com.ice.job.exception.ThrowUtils;
import com.ice.job.mapper.MajorMapper;
import com.ice.job.mapper.SchoolMapper;
import com.ice.job.model.entity.EducationExperience;
import com.ice.job.model.entity.Major;
import com.ice.job.model.entity.School;
import com.ice.job.model.enums.EducationEnum;
import com.ice.job.model.request.education.EducationAddRequest;
import com.ice.job.model.request.education.EducationQueryRequest;
import com.ice.job.model.vo.EducationVO;
import com.ice.job.service.EducationExperienceService;
import com.ice.job.mapper.EducationExperienceMapper;
import com.ice.job.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chenjiahan
 * @description 针对表【education_experience】的数据库操作Service实现
 * @createDate 2024-02-23 18:36:24
 */
@Service
public class EducationExperienceServiceImpl extends ServiceImpl<EducationExperienceMapper, EducationExperience>
        implements EducationExperienceService {

    @Resource
    private SchoolMapper schoolMapper;

    @Resource
    private MajorMapper majorMapper;

    @Override
    public Long addEducation(EducationAddRequest educationAddRequest) {
        // 校验参数
        EducationExperience education = new EducationExperience();
        BeanUtils.copyProperties(educationAddRequest, education);
        validEducation(education);

        // 保存用户信息
        Long userId = UserHolder.getUser().getId();
        education.setUserId(userId);

        // 插入教育经历信息
        baseMapper.insert(education);

        // todo 删除缓存中应聘者信息
        return education.getId();
    }

    @Override
    public EducationVO getEducation(Long educationId) {
        EducationExperience education = baseMapper.selectOne(Wrappers.<EducationExperience>lambdaQuery()
                .eq(EducationExperience::getId, educationId)
                .last("limit 1"));

        return getEducationVO(education);
    }

    @Override
    public Page<EducationVO> pageEducation(EducationQueryRequest educationQueryRequest) {
        long current = educationQueryRequest.getCurrent();
        long size = educationQueryRequest.getPageSize();

        Page<EducationExperience> educationPage = baseMapper.selectPage(new Page<>(current, size),
                getQueryWrapper(educationQueryRequest));

        List<EducationExperience> educationList = educationPage.getRecords();

        // 包装teacherVO类
        List<EducationVO> educationVOList = educationList.stream()
                .map(this::getEducationVO)
                .collect(Collectors.toList());

        Page<EducationVO> educationVOPage = new Page<>(educationPage.getCurrent(), educationPage.getSize(), educationPage.getTotal());

        educationVOPage.setRecords(educationVOList);

        return educationVOPage;
    }

    @Override
    public boolean deleteEducation(Long id) {

        // 判断是否存在
        boolean flag = baseMapper.delete(Wrappers.<EducationExperience>lambdaQuery()
                .eq(EducationExperience::getId, id)) != 0;

        if (!flag) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "删除失败，该教育经历不存在或操作失败！");
        }

        return true;
    }

    /**
     * 封装包装类
     *
     * @param education 教育经历
     * @return educationVO
     */
    private EducationVO getEducationVO(EducationExperience education) {
        if (education == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "教育经历不存在！");
        }
        EducationVO educationVO = new EducationVO();
        BeanUtils.copyProperties(education, educationVO);

        // 学校名称
        Long schoolId = education.getSchoolId();
        School school = schoolMapper.selectOne(Wrappers.<School>lambdaQuery()
                .eq(School::getId, schoolId)
                .select(School::getSchoolName)
                .last("limit 1"));
        ThrowUtils.throwIf(school == null, ErrorCode.NOT_FOUND_ERROR, "学校信息不存在！");
        educationVO.setSchoolName(school.getSchoolName());

        // 专业名称
        Long majorId = education.getMajorId();
        Major major = majorMapper.selectOne(Wrappers.<Major>lambdaQuery()
                .eq(Major::getId, majorId)
                .select(Major::getMajorName)
                .last("limit 1"));
        ThrowUtils.throwIf(major == null, ErrorCode.NOT_FOUND_ERROR, "专业信息不存在！");
        educationVO.setMajorName(major.getMajorName());

        return educationVO;
    }


    /**
     * 校验教育经历参数
     *
     * @param education 教育经历
     */
    private void validEducation(EducationExperience education) {
        // 1. 校验学校id
        Long schoolId = education.getSchoolId();
        if (schoolId == null || schoolId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "学校id错误!");
        }
        boolean existSchool = schoolMapper.exists(Wrappers.<School>lambdaQuery()
                .eq(School::getId, schoolId));
        ThrowUtils.throwIf(!existSchool, ErrorCode.NOT_FOUND_ERROR, "学校信息不存在！");
        // 2. 校验教育经历
        Integer educationType = education.getEducationType();
        if (educationType == null || !EducationEnum.getIntegerValues().contains(educationType)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "教育经历类型错误");
        }
        // 3. 判断开始结束年份
        Integer beginYear = education.getBeginYear();
        Integer endYear = education.getEndYear();
        ThrowUtils.throwIf(beginYear == null || endYear == null, ErrorCode.PARAMS_ERROR, "入学或毕业年份不得为空");
        if (endYear <= beginYear) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "毕业年份必须晚于入学年份！");
        }
        // 4. 校验专业id
        Long majorId = education.getMajorId();
        if (majorId == null || majorId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "专业id错误！");
        }
        boolean existMajor = majorMapper.exists(Wrappers.<Major>lambdaQuery()
                .eq(Major::getId, majorId));
        ThrowUtils.throwIf(!existMajor, ErrorCode.NOT_FOUND_ERROR, "专业信息不存在");
    }

    private QueryWrapper<EducationExperience> getQueryWrapper(EducationQueryRequest educationQueryRequest) {
        QueryWrapper<EducationExperience> queryWrapper = new QueryWrapper<>();

        if (educationQueryRequest == null) {
            return queryWrapper;
        }

        Long id = educationQueryRequest.getId();
        List<Long> ids = educationQueryRequest.getIds();
        Long schoolId = educationQueryRequest.getSchoolId();
        Long majorId = educationQueryRequest.getMajorId();
        Long userId = educationQueryRequest.getUserId();
        String sortField = educationQueryRequest.getSortField();
        String sortOrder = educationQueryRequest.getSortOrder();

        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        if (!CollectionUtils.isEmpty(ids)) {
            queryWrapper.in("id", id);
        }
        queryWrapper.eq(ObjectUtils.isNotEmpty(schoolId), "schoolId", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(majorId), "majorId", id);
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", id);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
}




