package com.ice.job.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.ice.job.common.ErrorCode;
import com.ice.job.exception.BusinessException;
import com.ice.job.exception.ThrowUtils;
import com.ice.job.mapper.*;
import com.ice.job.model.entity.*;
import com.ice.job.model.enums.EducationEnum;
import com.ice.job.model.enums.GenderEnum;
import com.ice.job.model.enums.JobStatusEnum;
import com.ice.job.model.request.employee.EmployeeUpdateRequest;
import com.ice.job.service.EmployeeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author chenjiahan
 * @description 针对表【employee(应聘者)】的数据库操作Service实现
 * @createDate 2024-02-22 14:01:45
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
        implements EmployeeService {

    private static final Gson GSON = new Gson();

    @Resource
    private UserMapper userMapper;

    @Override
    public Long employUpdate(EmployeeUpdateRequest registerRequest) {
        // 1. 校验注册信息
        Employee employee = new Employee();
        BeanUtils.copyProperties(registerRequest, employee);
        validEmployee(employee);

        // 技能标签
        List<String> skillTagList = registerRequest.getSkillTagList();
        if (skillTagList.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "个人技能至少 1 个");
        }
        String skillTagJSON = GSON.toJson(skillTagList);
        employee.setSkillTag(skillTagJSON);

        // 行业期望
        List<Long> industryIdLIst = registerRequest.getIndustryIdLIst();
        if (industryIdLIst.isEmpty() || industryIdLIst.size() > 3) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "行业技能至少 1 个，最多选择 3 个");
        }
        String industryIdsJSON = GSON.toJson(industryIdLIst);
        employee.setIndustryIds(industryIdsJSON);

        // 插入数据库
        baseMapper.insert(employee);

        return employee.getUserId();
    }

    /**
     * 校验注册信息
     *
     * @param employee 注册信息
     */
    private void validEmployee(Employee employee) {
        // 1. 校验用户是否存在
        Long userId = employee.getUserId();
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.PARAMS_ERROR, "用户id错误！");
        User user = userMapper.selectOne(Wrappers.<User>lambdaQuery()
                .eq(User::getId, userId)
                .select(User::getId)
                .last("limit 1"));
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR, "注册用户基本信息不存在！");

        // 2. 判断性别
        Integer gender = employee.getGender();
        if (gender == null || !GenderEnum.getIntegerValues().contains(gender)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "性别错误!");
        }

        // 3. 判断学历水平
        Integer education = employee.getEducation();
        if (education != null && !EducationEnum.getIntegerValues().contains(education)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "学历水平错误!");
        }

        // 6. 判断毕业年份
        Integer graduateYear = employee.getGraduateYear();
        ThrowUtils.throwIf(graduateYear == null || graduateYear <= 1950, ErrorCode.PARAMS_ERROR, "毕业年份错误");

        // 7. 判断工作状态
        Integer jobStatus = employee.getJobStatus();
        if (jobStatus == null || !JobStatusEnum.getIntegerValues().contains(jobStatus)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "工作状态错误");
        }

        // 8. 判断地址
        String address = employee.getAddress();
        ThrowUtils.throwIf(StringUtils.isEmpty(address), ErrorCode.PARAMS_ERROR, "地址为空!");
    }
}




