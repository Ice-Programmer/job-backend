package com.ice.job.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.ice.job.common.ErrorCode;
import com.ice.job.constant.CacheConstant;
import com.ice.job.constant.UserHolder;
import com.ice.job.exception.BusinessException;
import com.ice.job.exception.ThrowUtils;
import com.ice.job.mapper.CityMapper;
import com.ice.job.mapper.EmployeeMapper;
import com.ice.job.mapper.QualificationMapper;
import com.ice.job.mapper.UserMapper;
import com.ice.job.model.entity.City;
import com.ice.job.model.entity.Employee;
import com.ice.job.model.entity.Qualification;
import com.ice.job.model.entity.User;
import com.ice.job.model.enums.EducationEnum;
import com.ice.job.model.enums.GenderEnum;
import com.ice.job.model.enums.JobStatusEnum;
import com.ice.job.model.request.education.EducationQueryRequest;
import com.ice.job.model.request.employee.EmployeeUpdateRequest;
import com.ice.job.model.request.experience.ExperienceQueryRequest;
import com.ice.job.model.request.wishcareer.EmployeeWishCareerQueryRequest;
import com.ice.job.model.vo.*;
import com.ice.job.service.*;
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
 * @description 针对表【employee(应聘者)】的数据库操作Service实现
 * @createDate 2024-02-22 14:01:45
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
        implements EmployeeService {

    private static final Gson GSON = new Gson();

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserMapper userMapper;

    @Resource
    private EducationExperienceService educationExperienceService;

    @Resource
    private EmployeeExperienceService employeeExperienceService;

    @Resource
    private EmployeeWishCareerService employeeWishCareerService;

    @Resource
    private QualificationMapper qualificationMapper;

    @Resource
    private CityMapper cityMapper;

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

        // 插入数据库
        baseMapper.insert(employee);

        // 删除缓存中应聘者信息
        Long userId = UserHolder.getUser().getId();
        stringRedisTemplate.delete(CacheConstant.USER_EMPLOYEE_KEY + userId);

        return employee.getUserId();
    }

    @Override
    public EmployeeVO getEmployeeById(Long userId) {

        // 判断缓存中是否存在
        String key = CacheConstant.USER_EMPLOYEE_KEY + userId;
        String cacheValue = stringRedisTemplate.opsForValue().get(key);
        if (!StringUtils.isBlank(cacheValue)) {
            EmployeeVO cacheEmployeeVO = GSON.fromJson(cacheValue, new TypeToken<EmployeeVO>() {
            }.getType());
            return cacheEmployeeVO;
        }

        EmployeeVO employeeVO = new EmployeeVO();

        // 1. 获取基础用户信息
        User user = userMapper.selectById(userId);
        BeanUtils.copyProperties(user, employeeVO);

        // 1.1 补充城市信息
        City city = cityMapper.selectOne(Wrappers.<City>lambdaQuery()
                .eq(City::getId, user.getCityId())
                .select(City::getCityName)
                .last("limit 1"));
        employeeVO.setCityName(city.getCityName());

        // 2. 获取应聘者基础信息
        Employee employee = baseMapper.selectOne(Wrappers.<Employee>lambdaQuery()
                .eq(Employee::getUserId, userId)
                .last("limit 1"));
        BeanUtils.copyProperties(employee, employeeVO);


        // 2.1 补充应聘者技能标签 skillTagList
        String skillTag = employee.getSkillTag();
        List<String> skillTagList = GSON.fromJson(skillTag, new TypeToken<List<String>>() {
        }.getType());
        employeeVO.setSkillTagList(skillTagList);

        // 2.2 补充应聘者技能证书 qualificationList
        String qualificationIds = employee.getQualificationIds();
        List<Long> qualificationIdList = GSON.fromJson(qualificationIds, new TypeToken<List<Long>>() {
        }.getType());
        // 查出所有技能证书
        List<Qualification> qualificationList = qualificationMapper.selectList(Wrappers.<Qualification>lambdaQuery()
                .in(Qualification::getId, qualificationIdList)
                .select(Qualification::getId, Qualification::getQualificationName,
                        Qualification::getQualificationType));
        // 包装技能证书VO
        List<EmployeeVO.EmployeeQualificationVO> qualificationVOList = qualificationList.stream()
                .map(qualification -> {
                    EmployeeVO.EmployeeQualificationVO employeeQualificationVO = new EmployeeVO.EmployeeQualificationVO();
                    BeanUtils.copyProperties(qualification, employeeQualificationVO);
                    return employeeQualificationVO;
                }).collect(Collectors.toList());
        employeeVO.setQualificationList(qualificationVOList);


        // 3. 获取应聘者教育经历
        EducationQueryRequest educationQueryRequest = new EducationQueryRequest();
        educationQueryRequest.setUserId(userId);
        Page<EmployeeEducationVO> educationPage =
                educationExperienceService.pageEducation(educationQueryRequest);
        employeeVO.setEmployeeEducationList(educationPage.getRecords());

        // 4. 获取应聘者经历
        ExperienceQueryRequest experienceQueryRequest = new ExperienceQueryRequest();
        experienceQueryRequest.setUserId(userId);
        Page<EmployeeExperienceVO> experiencePage =
                employeeExperienceService.pageExperience(experienceQueryRequest);
        employeeVO.setEmployeeExperienceList(experiencePage.getRecords());

        // 5. 获取应聘者期望工作

        EmployeeWishCareerQueryRequest employeeWishCareerQueryRequest = new EmployeeWishCareerQueryRequest();
        employeeWishCareerQueryRequest.setUserId(userId);
        Page<EmployeeWishCareerVO> wishCareerPage =
                employeeWishCareerService.pageEmployeeWishCareer(employeeWishCareerQueryRequest);
        employeeVO.setWishCareerInfoList(wishCareerPage.getRecords());

        // 将数据放入缓存中
        String employeeVOJSON = GSON.toJson(employeeVO);
        stringRedisTemplate.opsForValue().set(
                key,
                employeeVOJSON,
                CacheConstant.USER_EMPLOYEE_TTL,
                TimeUnit.SECONDS
        );

        return employeeVO;
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

        // 9. 个人优势
        String advantage = employee.getAdvantage();
        if (advantage != null && advantage.length() >= 2000) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "个人优势字数过多");
        }
    }
}




