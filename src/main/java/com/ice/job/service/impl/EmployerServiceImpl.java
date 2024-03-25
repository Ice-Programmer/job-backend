package com.ice.job.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.ice.job.common.ErrorCode;
import com.ice.job.constant.CacheConstant;
import com.ice.job.constant.UserHolder;
import com.ice.job.exception.BusinessException;
import com.ice.job.exception.ThrowUtils;
import com.ice.job.mapper.*;
import com.ice.job.model.entity.*;
import com.ice.job.model.request.employer.EmployerUpdateRequest;
import com.ice.job.model.vo.EmployerVO;
import com.ice.job.service.EmployerService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author chenjiahan
 * @description 针对表【employer(招聘者)】的数据库操作Service实现
 * @createDate 2024-02-27 14:05:32
 */
@Service
public class EmployerServiceImpl extends ServiceImpl<EmployerMapper, Employer>
        implements EmployerService {

    private static final Gson GSON = new Gson();

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserMapper userMapper;

    @Resource
    private CityMapper cityMapper;

    @Resource
    private PositionMapper positionMapper;

    @Resource
    private CompanyMapper companyMapper;

    @Override
    public Long employUpdate(EmployerUpdateRequest registerRequest) {
        // 1. 校验注册信息
        Employer employer = new Employer();
        BeanUtils.copyProperties(registerRequest, employer);
        validEmployer(employer);


        // 插入数据库
        baseMapper.insert(employer);

        // 删除缓存中招聘者信息
        Long userId = UserHolder.getUser().getId();
        stringRedisTemplate.delete(CacheConstant.USER_EMPLOYER_KEY + userId);

        return employer.getUserId();
    }

    @Override
    public EmployerVO getEmployerById(Long userId) {

        // 判断缓存中是否存在
        String key = CacheConstant.USER_EMPLOYER_KEY + userId;
        String cacheValue = stringRedisTemplate.opsForValue().get(key);
        if (!StringUtils.isBlank(cacheValue)) {
            EmployerVO cacheEmployerVO = GSON.fromJson(cacheValue, new TypeToken<EmployerVO>() {
            }.getType());
            return cacheEmployerVO;
        }

        EmployerVO employerVO = new EmployerVO();

        // 1. 获取基础用户信息
        User user = userMapper.selectById(userId);
        BeanUtils.copyProperties(user, employerVO);

        // 2. 获取招聘者基础信息
        Employer employer = baseMapper.selectOne(Wrappers.<Employer>lambdaQuery()
                .eq(Employer::getUserId, userId)
                .last("limit 1"));
        BeanUtils.copyProperties(employer, employerVO);

        // 补充城市信息
        City city = cityMapper.selectOne(Wrappers.<City>lambdaQuery()
                .eq(City::getId, user.getCityId())
                .select(City::getCityName)
                .last("limit 1"));
        employerVO.setCityName(city.getCityName());

        // 补充职业信息
        Long positionId = employer.getPositionId();
        Position position = positionMapper.selectOne(Wrappers.<Position>lambdaQuery()
                .eq(Position::getId, positionId)
                .select(Position::getPositionName)
                .last("limit 1"));
        employerVO.setPositionName(position.getPositionName());

        // 补充公司信息
        Long companyId = employer.getCompanyId();
        Company company = companyMapper.selectOne(Wrappers.<Company>lambdaQuery()
                .eq(Company::getId, companyId)
                .select(Company::getCompanyName)
                .last("limit 1"));
        employerVO.setCompanyName(company.getCompanyName());

        // 将数据放入缓存中
        String employerVOJSON = GSON.toJson(employerVO);
        stringRedisTemplate.opsForValue().set(
                key,
                employerVOJSON,
                CacheConstant.USER_EMPLOYER_TTL,
                TimeUnit.SECONDS
        );

        return employerVO;
    }

    /**
     * 校验注册信息
     *
     * @param employer 注册信息
     */
    private void validEmployer(Employer employer) {
        // 1. 校验用户是否存在
        Long userId = employer.getUserId();
        ThrowUtils.throwIf(userId == null || userId <= 0, ErrorCode.PARAMS_ERROR, "用户id错误！");
        boolean exists = userMapper.exists(Wrappers.<User>lambdaQuery()
                .eq(User::getId, userId));
        ThrowUtils.throwIf(!exists, ErrorCode.NOT_FOUND_ERROR, "注册用户基本信息不存在！");

        // 2. 验证公司id
        Long companyId = employer.getCompanyId();
        if (companyId == null || companyId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "选择公司错误！");
        }
        boolean companyExist = companyMapper.exists(Wrappers.<Company>lambdaQuery()
                .eq(Company::getId, companyId));
        ThrowUtils.throwIf(!companyExist, ErrorCode.NOT_FOUND_ERROR, "公司信息不存在！");

        // 3. 校验职业id
        Long positionId = employer.getPositionId();
        if (positionId == null || positionId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "选择职业错误！");
        }
        boolean positionExist = positionMapper.exists(Wrappers.<Position>lambdaQuery()
                .eq(Position::getId, positionId));
        ThrowUtils.throwIf(!positionExist, ErrorCode.NOT_FOUND_ERROR, "职业信息不存在!");
    }
}




