package com.ice.job.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.ice.job.common.ErrorCode;
import com.ice.job.constant.CacheConstant;
import com.ice.job.constant.CommonConstant;
import com.ice.job.constant.UserHolder;
import com.ice.job.model.vo.IndustryVO;
import com.ice.job.model.vo.PositionVO;
import com.ice.job.exception.BusinessException;
import com.ice.job.exception.ThrowUtils;
import com.ice.job.mapper.CityMapper;
import com.ice.job.mapper.EmployeeWishCareerMapper;
import com.ice.job.mapper.IndustryMapper;
import com.ice.job.mapper.PositionMapper;
import com.ice.job.model.entity.City;
import com.ice.job.model.entity.EmployeeWishCareer;
import com.ice.job.model.entity.Industry;
import com.ice.job.model.entity.Position;
import com.ice.job.model.request.wishcareer.EmployeeWishCareerAddRequest;
import com.ice.job.model.request.wishcareer.EmployeeWishCareerQueryRequest;
import com.ice.job.model.request.wishcareer.EmployeeWishCareerUpdateRequest;
import com.ice.job.model.vo.EmployeeWishCareerVO;
import com.ice.job.service.EmployeeWishCareerService;
import com.ice.job.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author chenjiahan
 * @description 针对表【employee_wish_career】的数据库操作Service实现
 * @createDate 2024-02-26 14:07:53
 */
@Service
public class EmployeeWishCareerServiceImpl extends ServiceImpl<EmployeeWishCareerMapper, EmployeeWishCareer>
        implements EmployeeWishCareerService {

    private final static Gson GSON = new Gson();

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private PositionMapper positionMapper;

    @Resource
    private CityMapper cityMapper;

    @Resource
    private IndustryMapper industryMapper;

    @Override
    public Long addEmployeeWishCareer(EmployeeWishCareerAddRequest employeeWishCareerAddRequest) {

        // 校验参数
        EmployeeWishCareer employeeWishCareer = new EmployeeWishCareer();
        BeanUtils.copyProperties(employeeWishCareerAddRequest, employeeWishCareer);
        validEmployeeWishCareer(employeeWishCareer);

        // 保存用户信息
        Long userId = UserHolder.getUser().getId();
        employeeWishCareer.setUserId(userId);

        // 保存行业id
        List<Long> industryIdList = employeeWishCareerAddRequest.getIndustryIdList();
        String industryIds = GSON.toJson(industryIdList);
        employeeWishCareer.setIndustryIds(industryIds);

        // 插入应聘者主要经历信息
        baseMapper.insert(employeeWishCareer);

        // 删除缓存中应聘者信息
        stringRedisTemplate.delete(CacheConstant.USER_EMPLOYEE_KEY + userId);

        return employeeWishCareer.getId();
    }

    @Override
    public EmployeeWishCareerVO getEmployeeWishCareer(Long employeeWishCareerId) {
        EmployeeWishCareer employeeWishCareer = baseMapper.selectOne(Wrappers.<EmployeeWishCareer>lambdaQuery()
                .eq(EmployeeWishCareer::getId, employeeWishCareerId)
                .last("limit 1"));

        return getEmployeeWishCareerVO(employeeWishCareer);
    }

    @Override
    public Page<EmployeeWishCareerVO> pageEmployeeWishCareer(EmployeeWishCareerQueryRequest employeeWishCareerQueryRequest) {
        long current = employeeWishCareerQueryRequest.getCurrent();
        long size = employeeWishCareerQueryRequest.getPageSize();

        Page<EmployeeWishCareer> employeeWishCareerPage = baseMapper.selectPage(new Page<>(current, size),
                getQueryWrapper(employeeWishCareerQueryRequest));

        List<EmployeeWishCareer> employeeWishCareerList = employeeWishCareerPage.getRecords();

        // 包装VO类
        List<EmployeeWishCareerVO> employeeWishCareerVOList = employeeWishCareerList.stream()
                .map(this::getEmployeeWishCareerVO)
                .collect(Collectors.toList());

        Page<EmployeeWishCareerVO> employeeWishCareerVOPage = new Page<>(employeeWishCareerPage.getCurrent(), employeeWishCareerPage.getSize(), employeeWishCareerPage.getTotal());

        employeeWishCareerVOPage.setRecords(employeeWishCareerVOList);

        return employeeWishCareerVOPage;
    }

    @Override
    public boolean deleteEmployeeWishCareer(Long id) {

        // 判断是否存在
        boolean flag = baseMapper.delete(Wrappers.<EmployeeWishCareer>lambdaQuery()
                .eq(EmployeeWishCareer::getId, id)) != 0;

        if (!flag) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "删除失败，该应聘者主要经历不存在或操作失败！");
        }

        // 删除缓存中应聘者信息
        Long userId = UserHolder.getUser().getId();
        stringRedisTemplate.delete(CacheConstant.USER_EMPLOYEE_KEY + userId);

        return true;
    }

    @Override
    public boolean updateEmployeeWishCareer(EmployeeWishCareerUpdateRequest employeeWishCareerUpdateRequest) {
        // 判断是否存在
        boolean exists = baseMapper.exists(Wrappers.<EmployeeWishCareer>lambdaQuery()
                .eq(EmployeeWishCareer::getId, employeeWishCareerUpdateRequest.getId()));
        ThrowUtils.throwIf(!exists, ErrorCode.NOT_FOUND_ERROR, "应聘者主要经历不存在！");

        // 校验参数
        EmployeeWishCareer employeeWishCareer = new EmployeeWishCareer();
        BeanUtils.copyProperties(employeeWishCareerUpdateRequest, employeeWishCareer);
        validEmployeeWishCareer(employeeWishCareer);

        // 获取行业id
        List<Long> industryIdList = employeeWishCareerUpdateRequest.getIndustryIdList();
        String industryIds = GSON.toJson(industryIdList);
        employeeWishCareer.setIndustryIds(industryIds);

        // 删除缓存内容
        Long userId = UserHolder.getUser().getId();

        // 更新数据
        stringRedisTemplate.delete(CacheConstant.USER_EMPLOYEE_KEY + userId);

        // 更新数据库
        baseMapper.updateById(employeeWishCareer);
        return true;
    }

    /**
     * 封装包装类
     *
     * @param employeeWishCareer 应聘者主要经历
     * @return employeeWishCareerVO
     */
    private EmployeeWishCareerVO getEmployeeWishCareerVO(EmployeeWishCareer employeeWishCareer) {
        if (employeeWishCareer == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "应聘者主要经历不存在！");
        }
        EmployeeWishCareerVO employeeWishCareerVO = new EmployeeWishCareerVO();
        BeanUtils.copyProperties(employeeWishCareer, employeeWishCareerVO);

        // 封装行业信息
        String industryIds = employeeWishCareer.getIndustryIds();
        List<Long> industryIdList = GSON.fromJson(industryIds, new TypeToken<List<Long>>() {
        }.getType());
        List<Industry> industryList = industryMapper.selectList(Wrappers.<Industry>lambdaQuery()
                .in(Industry::getId, industryIdList)
                .select(Industry::getId, Industry::getIndustryName));

        List<IndustryVO> industryVOList = industryList.stream().map(industry -> {
            IndustryVO industryVO = new IndustryVO();
            BeanUtils.copyProperties(industry, industryVO);
            return industryVO;
        }).collect(Collectors.toList());
        employeeWishCareerVO.setIndustryInfoList(industryVOList);

        // 封装职业信息
        Long positionId = employeeWishCareer.getPositionId();
        Position position = positionMapper.selectOne(Wrappers.<Position>lambdaQuery()
                .eq(Position::getId, positionId)
                .select(Position::getId, Position::getPositionName, Position::getPositionDescript)
                .last("limit 1"));
        PositionVO positionVO = new PositionVO();
        BeanUtils.copyProperties(position, positionVO);
        employeeWishCareerVO.setPositionInfo(positionVO);

        // 封装城市
        Long cityId = employeeWishCareer.getCityId();
        City city = cityMapper.selectOne(Wrappers.<City>lambdaQuery()
                .eq(City::getId, cityId)
                .select(City::getCityName)
                .last("limit 1"));
        employeeWishCareerVO.setCityName(city.getCityName());

        return employeeWishCareerVO;
    }


    /**
     * 校验应聘者主要经历参数
     *
     * @param employeeWishCareer 应聘者主要经历
     */
    private void validEmployeeWishCareer(EmployeeWishCareer employeeWishCareer) {
        Long positionId = employeeWishCareer.getPositionId();
        Long cityId = employeeWishCareer.getCityId();

        // 1. 校验职业类型
        if (positionId == null || positionId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "职业 id 为空");
        }
        boolean positionExist = !positionMapper.exists(Wrappers.<Position>lambdaQuery()
                .eq(Position::getId, positionId));
        ThrowUtils.throwIf(positionExist, ErrorCode.NOT_FOUND_ERROR, "选择职业不存在");

        // 2. 校验cityId
        if (cityId == null || cityId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "城市 id 为空");
        }
        boolean cityExist = !cityMapper.exists(Wrappers.<City>lambdaQuery()
                .eq(City::getId, cityId));
        ThrowUtils.throwIf(cityExist, ErrorCode.NOT_FOUND_ERROR, "选择城市不存在");

    }

    /**
     * 拼接查询条件
     *
     * @param employeeWishCareerQueryRequest 查询条件
     * @return QueryWrapper
     */
    private QueryWrapper<EmployeeWishCareer> getQueryWrapper(EmployeeWishCareerQueryRequest employeeWishCareerQueryRequest) {
        QueryWrapper<EmployeeWishCareer> queryWrapper = new QueryWrapper<>();

        if (employeeWishCareerQueryRequest == null) {
            return queryWrapper;
        }

        Long id = employeeWishCareerQueryRequest.getId();
        List<Long> ids = employeeWishCareerQueryRequest.getIds();
        Long positionId = employeeWishCareerQueryRequest.getPositionId();
        Long userId = employeeWishCareerQueryRequest.getUserId();
        Long cityId = employeeWishCareerQueryRequest.getCityId();
        String sortField = employeeWishCareerQueryRequest.getSortField();
        String sortOrder = employeeWishCareerQueryRequest.getSortOrder();


        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        if (!CollectionUtils.isEmpty(ids)) {
            queryWrapper.in("id", id);
        }

        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(positionId), "positionId", positionId);
        queryWrapper.eq(ObjectUtils.isNotEmpty(cityId), "cityId", cityId);

        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
}




