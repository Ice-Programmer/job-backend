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
import com.ice.job.exception.BusinessException;
import com.ice.job.exception.ThrowUtils;
import com.ice.job.mapper.*;
import com.ice.job.model.entity.*;
import com.ice.job.model.request.company.CompanyAddRequest;
import com.ice.job.model.request.company.CompanyQueryRequest;
import com.ice.job.model.request.company.CompanyUpdateRequest;
import com.ice.job.model.vo.CompanyVO;
import com.ice.job.service.CompanyService;
import com.ice.job.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author chenjiahan
 * @description 针对表【company(公司信息)】的数据库操作Service实现
 * @createDate 2024-02-27 12:53:24
 */
@Service
public class CompanyServiceImpl extends ServiceImpl<CompanyMapper, Company>
        implements CompanyService {

    private final static Gson GSON = new Gson();

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private IndustryMapper industryMapper;

    @Resource
    private CityMapper cityMapper;

    @Override
    public Long addCompany(CompanyAddRequest companyAddRequest) {
        // 校验参数
        Company company = new Company();
        BeanUtils.copyProperties(companyAddRequest, company);
        validCompany(company);

        // 公司图片
        List<String> companyImgList = companyAddRequest.getCompanyImgList();
        String companyImg = GSON.toJson(companyImgList);
        company.setCompanyImg(companyImg);

        // 插入公司信息
        baseMapper.insert(company);

        return company.getId();
    }

    @Override
    public CompanyVO getCompany(Long companyId) {
        // 获取缓存中公司信息
        String key = CacheConstant.COMPANY_INFO_KEY + companyId;
        String cacheValue = stringRedisTemplate.opsForValue().get(key);
        if (!StringUtils.isBlank(cacheValue)) {
            CompanyVO companyVO = GSON.fromJson(cacheValue, new TypeToken<CompanyVO>() {
            }.getType());
            return companyVO;
        }

        Company company = baseMapper.selectOne(Wrappers.<Company>lambdaQuery()
                .eq(Company::getId, companyId)
                .last("limit 1"));

        // 获取包装类
        CompanyVO companyVO = getCompanyVO(company);

        // 存入缓存
        String companyVOJSON = GSON.toJson(companyVO);
        stringRedisTemplate.opsForValue().set(
                key,
                companyVOJSON,
                CacheConstant.COMPANY_INFO_TTL,
                TimeUnit.SECONDS
        );
        return companyVO;
    }

    @Override
    public Page<CompanyVO> pageCompany(CompanyQueryRequest companyQueryRequest) {
        long current = companyQueryRequest.getCurrent();
        long size = companyQueryRequest.getPageSize();

        Page<Company> companyPage = baseMapper.selectPage(new Page<>(current, size),
                getQueryWrapper(companyQueryRequest));

        List<Company> companyList = companyPage.getRecords();

        // 包装teacherVO类
        List<CompanyVO> employeeCompanyVOList = companyList.stream()
                .map(this::getCompanyVO)
                .collect(Collectors.toList());

        Page<CompanyVO> companyVOPage = new Page<>(companyPage.getCurrent(), companyPage.getSize(), companyPage.getTotal());

        companyVOPage.setRecords(employeeCompanyVOList);

        return companyVOPage;
    }

    @Override
    public boolean deleteCompany(Long id) {

        // 判断是否存在
        boolean flag = baseMapper.delete(Wrappers.<Company>lambdaQuery()
                .eq(Company::getId, id)) != 0;

        if (!flag) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "删除失败，该公司不存在或操作失败！");
        }

        // 删除缓存中公司信息
        stringRedisTemplate.delete(CacheConstant.COMPANY_INFO_KEY + id);

        return true;
    }

    @Override
    public boolean updateCompany(CompanyUpdateRequest companyUpdateRequest) {
        // 判断是否存在
        boolean exists = baseMapper.exists(Wrappers.<Company>lambdaQuery()
                .eq(Company::getId, companyUpdateRequest.getId()));
        ThrowUtils.throwIf(!exists, ErrorCode.NOT_FOUND_ERROR, "公司不存在！");

        // 校验参数
        Company company = new Company();
        BeanUtils.copyProperties(companyUpdateRequest, company);
        validCompany(company);

        // 公司图片
        List<String> companyImgList = companyUpdateRequest.getCompanyImgList();
        String companyImg = GSON.toJson(companyImgList);
        company.setCompanyImg(companyImg);

        // 删除缓存中公司信息
        String key = CacheConstant.COMPANY_INFO_KEY + companyUpdateRequest.getId();
        stringRedisTemplate.delete(key);

        // 更新数据库
        baseMapper.updateById(company);
        return true;
    }

    /**
     * 封装包装类
     *
     * @param company 公司
     * @return companyVO
     */
    private CompanyVO getCompanyVO(Company company) {

        if (company == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "公司不存在！");
        }
        CompanyVO companyVO = new CompanyVO();
        BeanUtils.copyProperties(company, companyVO);

        // 封装公司行业
        Long companyIndustryId = company.getCompanyIndustry();
        Industry companyIndustry = industryMapper.selectOne(Wrappers.<Industry>lambdaQuery()
                .eq(Industry::getId, companyIndustryId)
                .select(Industry::getIndustryName));
        companyVO.setCompanyIndustryId(companyIndustryId);
        companyVO.setCompanyIndustryName(companyIndustry.getIndustryName());

        // 封装公司图片
        String companyImgJSON = company.getCompanyImg();
        List<String> companyImgList = GSON.fromJson(companyImgJSON, new TypeToken<List<String>>() {
        }.getType());
        companyVO.setCompanyImgList(companyImgList);

        return companyVO;
    }


    /**
     * 校验公司参数
     *
     * @param company 公司
     */
    private void validCompany(Company company) {
        // 1. 校验公司名称
        String companyName = company.getCompanyName();
        if (StringUtils.isBlank(companyName) || companyName.length() >= 50) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "公司名称错误！");
        }
        // 2. 校验公司地址
        String companyAddress = company.getCompanyAddress();
        if (StringUtils.isBlank(companyAddress)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "公司地址为空！");
        }
        // 校验所在城市
        Long cityId = company.getCityId();
        if (cityId == null || cityId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "公司id错误");
        }
        boolean exists = cityMapper.exists(Wrappers.<City>lambdaQuery()
                .eq(City::getId, cityId));
        ThrowUtils.throwIf(!exists, ErrorCode.NOT_FOUND_ERROR, "所在城市不存在！");

        // 2. 校验公司logo
        String companyLogo = company.getCompanyLogo();
        if (StringUtils.isBlank(companyLogo)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "公司 logo 为空！");
        }

        // 3. 校验公司产业
        Long companyIndustry = company.getCompanyIndustry();
        if (companyIndustry == null || companyIndustry <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "公司行业选择错误");
        }
        boolean industryExist = industryMapper.exists(Wrappers.<Industry>lambdaQuery()
                .eq(Industry::getId, companyIndustry));
        ThrowUtils.throwIf(!industryExist, ErrorCode.NOT_FOUND_ERROR, "公司行业不存在！");

    }

    /**
     * 拼接查询条件
     *
     * @param companyQueryRequest 查询条件
     * @return QueryWrapper
     */
    private QueryWrapper<Company> getQueryWrapper(CompanyQueryRequest companyQueryRequest) {
        QueryWrapper<Company> queryWrapper = new QueryWrapper<>();

        if (companyQueryRequest == null) {
            return queryWrapper;
        }


        Long id = companyQueryRequest.getId();
        List<Long> ids = companyQueryRequest.getIds();
        Integer companyIndustryType = companyQueryRequest.getCompanyIndustryType();
        String sortField = companyQueryRequest.getSortField();
        String sortOrder = companyQueryRequest.getSortOrder();

        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        if (!CollectionUtils.isEmpty(ids)) {
            queryWrapper.in("id", id);
        }

        queryWrapper.eq(ObjectUtils.isNotEmpty(companyIndustryType), "companyIndustry", companyIndustryType);
        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
}




