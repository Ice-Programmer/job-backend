package com.ice.job.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ice.job.common.ErrorCode;
import com.ice.job.exception.BusinessException;
import com.ice.job.exception.ThrowUtils;
import com.ice.job.mapper.MajorMapper;
import com.ice.job.mapper.SchoolMapper;
import com.ice.job.model.entity.EducationExperience;
import com.ice.job.model.entity.Major;
import com.ice.job.model.entity.School;
import com.ice.job.model.enums.EducationEnum;
import com.ice.job.model.request.education.EducationAddRequest;
import com.ice.job.service.EducationExperienceService;
import com.ice.job.mapper.EducationExperienceMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

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

        // 插入教育经历信息
        baseMapper.insert(education);

        // todo 删除缓存中应聘者信息
        return education.getId();
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
}




