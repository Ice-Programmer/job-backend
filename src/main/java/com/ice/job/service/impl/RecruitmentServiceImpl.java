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
import com.ice.job.model.enums.JobTypeEnum;
import com.ice.job.model.request.recruitment.RecruitmentAddRequest;
import com.ice.job.model.request.recruitment.RecruitmentQueryRequest;
import com.ice.job.model.request.recruitment.RecruitmentUpdateRequest;
import com.ice.job.model.vo.IndustryVO;
import com.ice.job.model.vo.PositionVO;
import com.ice.job.model.vo.RecruitmentVO;
import com.ice.job.service.RecruitmentService;
import com.ice.job.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.joda.time.LocalTime;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author chenjiahan
 * @description 针对表【recruitment(招聘信息)】的数据库操作Service实现
 * @createDate 2024-02-28 12:53:31
 */
@Service
public class RecruitmentServiceImpl extends ServiceImpl<RecruitmentMapper, Recruitment>
        implements RecruitmentService {

    private final static Gson GSON = new Gson();

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private EmployerMapper employerMapper;

    @Resource
    private IndustryMapper industryMapper;

    @Resource
    private PositionMapper positionMapper;

    @Resource
    private CompanyMapper companyMapper;

    @Resource
    private CityMapper cityMapper;

    @Override
    public Long addRecruitment(RecruitmentAddRequest recruitmentAddRequest) {
        // 校验参数
        Recruitment recruitment = new Recruitment();
        BeanUtils.copyProperties(recruitmentAddRequest, recruitment);
        validRecruitment(recruitment);

        // 获取招聘者信息
        Long userId = UserHolder.getUser().getId();
        Employer employer = employerMapper.selectOne(Wrappers.<Employer>lambdaQuery()
                .eq(Employer::getUserId, userId)
                .select(Employer::getCompanyId));

        // 补充招聘关键词
        List<String> jobKeywordList = recruitmentAddRequest.getJobKeywords();
        if (CollectionUtils.isEmpty(jobKeywordList) || jobKeywordList.size() < 3) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "关键词至少为3个");
        }
        String jobKeywords = GSON.toJson(jobKeywordList);
        recruitment.setJobKeyword(jobKeywords);

        // 补充招聘技术栈
        List<String> jobSkillList = recruitmentAddRequest.getJobSkills();
        String jobSkills = GSON.toJson(jobSkillList);
        recruitment.setJobSkills(jobSkills);

        // 补充公司
        recruitment.setCompanyId(employer.getCompanyId());
        recruitment.setUserId(userId);

        // 插入招聘信息信息
        baseMapper.insert(recruitment);

        return recruitment.getId();
    }

    @Override
    public RecruitmentVO getRecruitment(Long recruitmentId) {
        // 获取缓存中招聘信息信息
        String key = CacheConstant.RECRUITMENT_INFO_KEY + recruitmentId;
        String cacheValue = stringRedisTemplate.opsForValue().get(key);
        if (!StringUtils.isBlank(cacheValue)) {
            RecruitmentVO recruitmentVO = GSON.fromJson(cacheValue, new TypeToken<RecruitmentVO>() {
            }.getType());
            return recruitmentVO;
        }

        Recruitment recruitment = baseMapper.selectOne(Wrappers.<Recruitment>lambdaQuery()
                .eq(Recruitment::getId, recruitmentId)
                .last("limit 1"));

        // 获取包装类
        RecruitmentVO recruitmentVO = getRecruitmentVO(recruitment);

        // 存入缓存
        String recruitmentVOJSON = GSON.toJson(recruitmentVO);
        stringRedisTemplate.opsForValue().set(
                key,
                recruitmentVOJSON,
                CacheConstant.RECRUITMENT_INFO_TTL,
                TimeUnit.SECONDS
        );
        return recruitmentVO;
    }

    @Override
    public Page<RecruitmentVO> pageRecruitment(RecruitmentQueryRequest recruitmentQueryRequest) {
        long current = recruitmentQueryRequest.getCurrent();
        long size = recruitmentQueryRequest.getPageSize();

        Page<Recruitment> recruitmentPage = baseMapper.selectPage(new Page<>(current, size),
                getQueryWrapper(recruitmentQueryRequest));

        List<Recruitment> recruitmentList = recruitmentPage.getRecords();

        // 包装VO类
        List<RecruitmentVO> employeeRecruitmentVOList = recruitmentList.stream()
                .map(this::getRecruitmentVO)
                .collect(Collectors.toList());

        Page<RecruitmentVO> recruitmentVOPage = new Page<>(recruitmentPage.getCurrent(), recruitmentPage.getSize(), recruitmentPage.getTotal());

        recruitmentVOPage.setRecords(employeeRecruitmentVOList);

        return recruitmentVOPage;
    }

    @Override
    public boolean deleteRecruitment(Long id) {

        // 判断是否存在
        boolean flag = baseMapper.delete(Wrappers.<Recruitment>lambdaQuery()
                .eq(Recruitment::getId, id)) != 0;

        if (!flag) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "删除失败，该招聘信息不存在或操作失败！");
        }

        // 删除缓存中招聘信息信息
        stringRedisTemplate.delete(CacheConstant.RECRUITMENT_INFO_KEY + id);

        return true;
    }

    @Override
    public boolean updateRecruitment(RecruitmentUpdateRequest recruitmentUpdateRequest) {
        // 判断是否存在
        boolean exists = baseMapper.exists(Wrappers.<Recruitment>lambdaQuery()
                .eq(Recruitment::getId, recruitmentUpdateRequest.getId()));
        ThrowUtils.throwIf(!exists, ErrorCode.NOT_FOUND_ERROR, "招聘信息不存在！");

        // 校验参数
        Recruitment recruitment = new Recruitment();
        BeanUtils.copyProperties(recruitmentUpdateRequest, recruitment);
        validRecruitment(recruitment);

        // 补充招聘关键词
        List<String> jobKeywordList = recruitmentUpdateRequest.getJobKeywords();
        if (CollectionUtils.isEmpty(jobKeywordList) || jobKeywordList.size() < 3) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "关键词至少为3个");
        }
        String jobKeywords = GSON.toJson(jobKeywordList);
        recruitment.setJobKeyword(jobKeywords);

        // 补充招聘技术栈
        List<String> jobSkillList = recruitmentUpdateRequest.getJobSkills();
        String jobSkills = GSON.toJson(jobSkillList);
        recruitment.setJobSkills(jobSkills);

        // 删除缓存中招聘信息信息
        String key = CacheConstant.RECRUITMENT_INFO_KEY + recruitmentUpdateRequest.getId();
        stringRedisTemplate.delete(key);

        // 更新数据库
        baseMapper.updateById(recruitment);
        return true;
    }

    /**
     * 封装包装类
     *
     * @param recruitment 招聘信息
     * @return recruitmentVO
     */
    private RecruitmentVO getRecruitmentVO(Recruitment recruitment) {

        if (recruitment == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "招聘信息不存在！");
        }
        RecruitmentVO recruitmentVO = new RecruitmentVO();
        BeanUtils.copyProperties(recruitment, recruitmentVO);

        // 封装公司信息
        Company company = companyMapper.selectOne(Wrappers.<Company>lambdaQuery()
                .eq(Company::getId, recruitment.getCompanyId())
                .select(Company::getCompanyName, Company::getCompanyLogo, Company::getId)
                .last("limit 1"));
        RecruitmentVO.CompanyInfo companyInfo = new RecruitmentVO.CompanyInfo();
        BeanUtils.copyProperties(company, companyInfo);
        recruitmentVO.setCompanyInfo(companyInfo);

        // 封装职业信息
        Position position = positionMapper.selectOne(Wrappers.<Position>lambdaQuery()
                .eq(Position::getId, recruitment.getPositionId())
                .select(Position::getId, Position::getPositionDescript, Position::getPositionName));
        PositionVO positionVO = new PositionVO();
        BeanUtils.copyProperties(position, positionVO);
        recruitmentVO.setPositionInfo(positionVO);

        // 封装行业信息
        IndustryVO industryVO = new IndustryVO();
        Industry industry = industryMapper.selectOne(Wrappers.<Industry>lambdaQuery()
                .eq(Industry::getId, recruitment.getIndustryId())
                .select(Industry::getId, Industry::getIndustryName));
        BeanUtils.copyProperties(industry, industryVO);
        recruitmentVO.setIndustryInfo(industryVO);

        // 补充职业技术栈
        String jobSkills = recruitment.getJobSkills();
        List<String> jobSkillList = GSON.fromJson(jobSkills, new TypeToken<List<String>>() {
        }.getType());
        recruitmentVO.setJobSkillList(jobSkillList);

        // 补充职业关键词
        String jobKeyword = recruitment.getJobKeyword();
        List<String> jobKeywordList = GSON.fromJson(jobKeyword, new TypeToken<List<String>>() {
        }.getType());
        recruitmentVO.setJobKeywords(jobKeywordList);

        return recruitmentVO;
    }


    /**
     * 校验招聘信息参数
     *
     * @param recruitment 招聘信息
     */
    private void validRecruitment(Recruitment recruitment) {
        // 1. 校验岗位名称
        String jobName = recruitment.getJobName();
        ThrowUtils.throwIf(StringUtils.isBlank(jobName), ErrorCode.PARAMS_ERROR, "招聘信息标题为空！");
        ThrowUtils.throwIf(jobName.length() > 20, ErrorCode.PARAMS_ERROR, "招聘信息标题字数20字以内！");

        // 2. 校验招聘岗位介绍
        String jobDescription = recruitment.getJobDescription();
        ThrowUtils.throwIf(StringUtils.isBlank(jobDescription), ErrorCode.PARAMS_ERROR, "招聘岗位介绍为空！");
        ThrowUtils.throwIf(jobDescription.length() > 1000, ErrorCode.PARAMS_ERROR, "招聘岗位介绍字数1000字以内！");

        // 2. 校验招聘岗位介绍
        String jobRequirements = recruitment.getJobRequirements();
        ThrowUtils.throwIf(StringUtils.isBlank(jobRequirements), ErrorCode.PARAMS_ERROR, "招聘岗位要求为空！");
        ThrowUtils.throwIf(jobRequirements.length() > 1000, ErrorCode.PARAMS_ERROR, "招聘岗位要求字数1000字以内！");

        // 3. 校验职位类型
        Long positionId = recruitment.getPositionId();
        if (positionId == null || positionId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "职位选择错误!");
        }
        boolean positionExist = positionMapper.exists(Wrappers.<Position>lambdaQuery()
                .eq(Position::getId, positionId));
        ThrowUtils.throwIf(!positionExist, ErrorCode.NOT_FOUND_ERROR, "职位信息不存在！");

        // 4. 校验行业类型
        Long industryId = recruitment.getIndustryId();
        if (industryId == null || industryId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "行业选择错误!");
        }
        boolean industryExist = industryMapper.exists(Wrappers.<Industry>lambdaQuery()
                .eq(Industry::getId, industryId));
        ThrowUtils.throwIf(!industryExist, ErrorCode.NOT_FOUND_ERROR, "行业信息不存在！");

        // 5. 校验招聘工作类型
        Integer jobType = recruitment.getJobType();
        if (jobType == null || !JobTypeEnum.getIntegerValues().contains(jobType)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "工作类型选择错误！");
        }

        // 6. 校验工作地址
        String jobAddress = recruitment.getJobAddress();
        if (StringUtils.isBlank(jobAddress)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "工作地点为空！");
        }

        // 7. 校验城市id
        Long cityId = recruitment.getCityId();
        if (cityId == null || cityId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "城市选择错误！");
        }
        boolean cityExist = cityMapper.exists(Wrappers.<City>lambdaQuery()
                .eq(City::getId, cityId));
        ThrowUtils.throwIf(!cityExist, ErrorCode.NOT_FOUND_ERROR, "城市不存在!");
    }

    /**
     * 拼接查询条件
     *
     * @param recruitmentQueryRequest 查询条件
     * @return QueryWrapper
     */
    private QueryWrapper<Recruitment> getQueryWrapper(RecruitmentQueryRequest recruitmentQueryRequest) {
        QueryWrapper<Recruitment> queryWrapper = new QueryWrapper<>();

        if (recruitmentQueryRequest == null) {
            return queryWrapper;
        }

        Long id = recruitmentQueryRequest.getId();
        List<Long> ids = recruitmentQueryRequest.getIds();
        String searchText = recruitmentQueryRequest.getSearchText();


        String sortField = recruitmentQueryRequest.getSortField();
        String sortOrder = recruitmentQueryRequest.getSortOrder();

        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        if (!CollectionUtils.isEmpty(ids)) {
            queryWrapper.in("id", id);
        }

        Long userId = recruitmentQueryRequest.getUserId();
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        Long positionId = recruitmentQueryRequest.getPositionId();
        queryWrapper.eq(ObjectUtils.isNotEmpty(positionId), "positionId", positionId);
        Long industryId = recruitmentQueryRequest.getIndustryId();
        queryWrapper.eq(ObjectUtils.isNotEmpty(industryId), "industryId", industryId);
        Integer educationType = recruitmentQueryRequest.getEducationType();
        queryWrapper.ge(ObjectUtils.isNotEmpty(educationType), "educationType", educationType);
        Long companyId = recruitmentQueryRequest.getCompanyId();
        queryWrapper.eq(ObjectUtils.isNotEmpty(companyId), "companyId", companyId);
        Integer jobType = recruitmentQueryRequest.getJobType();
        queryWrapper.eq(ObjectUtils.isNotEmpty(jobType), "jobType", jobType);
        Integer salaryUpper = recruitmentQueryRequest.getSalaryUpper();
        queryWrapper.le(ObjectUtils.isNotEmpty(salaryUpper), "salaryUpper", salaryUpper);
        Integer salaryLower = recruitmentQueryRequest.getSalaryLower();
        queryWrapper.ge(ObjectUtils.isNotEmpty(salaryLower), "salaryLower", salaryLower);
        Integer jobActive = recruitmentQueryRequest.getJobActive();
        queryWrapper.eq(ObjectUtils.isNotEmpty(jobActive), "jobActive", jobActive);
        List<Long> cityIdList = recruitmentQueryRequest.getCityIdList();


        if (searchText != null) {
            queryWrapper.like("jobName", searchText).or()
                    .like("jobDescription", searchText).or()
                    .like("jobRequirements", searchText);
        }

        if (!CollectionUtils.isEmpty(cityIdList)) {
            queryWrapper.in("cityId", cityIdList);
        }

        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }
}




