package com.ice.job.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.ice.job.constant.CacheConstant;
import com.ice.job.model.enums.QualificationTypeEnum;
import com.ice.job.model.vo.QualificationVO;
import com.ice.job.service.QualificationService;
import com.ice.job.model.entity.Qualification;
import com.ice.job.mapper.QualificationMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author chenjiahan
 * @description 针对表【qualification(资格证书)】的数据库操作Service实现
 * @createDate 2024-02-24 16:00:58
 */
@Service
public class QualificationServiceImpl extends ServiceImpl<QualificationMapper, Qualification>
        implements QualificationService {

    private final static Gson GSON = new Gson();

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public List<QualificationVO> getQualificationList() {

        // 查询缓存是否有值
        String cacheValue = stringRedisTemplate.opsForValue().get(CacheConstant.EMPLOYEE_QUALIFICATION_KEY);

        if (!StringUtils.isBlank(cacheValue)) {
            return GSON.fromJson(cacheValue, new TypeToken<List<QualificationVO>>() {
            }.getType());
        }

        List<Qualification> qualificationList = baseMapper.selectList(Wrappers.<Qualification>lambdaQuery()
                .select(Qualification::getId, Qualification::getQualificationName,
                        Qualification::getQualificationType));

        // 判空
        if (CollectionUtils.isEmpty(qualificationList)) {
            return Collections.emptyList();
        }

        List<QualificationVO> qualificationVOList = new ArrayList<>();

        Map<Integer, List<Qualification>> qualificationMap = qualificationList.stream()
                .collect(Collectors.groupingBy(Qualification::getQualificationType));

        // 遍历封装 QualificationVO
        qualificationMap.forEach((qualificationType, qualifications) -> {
            // 获取类型名称
            QualificationVO qualificationVO = new QualificationVO();
            String typeName = Objects.requireNonNull(
                    QualificationTypeEnum.getEnumByName(qualificationType)
            ).getName();

            // 获取分类下的所有证书列表
            List<QualificationVO.QualificationDetail> qualificationDetailList =
                    qualifications.stream()
                            .map(this::getQualificationDetail)
                            .collect(Collectors.toList());

            qualificationVO.setQualificationType(typeName);
            qualificationVO.setQualificationList(qualificationDetailList);
            qualificationVOList.add(qualificationVO);
        });

        // 保存缓存中
        String qualificationListJSON = GSON.toJson(qualificationVOList);
        stringRedisTemplate.opsForValue().set(
                CacheConstant.EMPLOYEE_QUALIFICATION_KEY,
                qualificationListJSON,
                CacheConstant.EMPLOYEE_QUALIFICATION_TTL,
                TimeUnit.SECONDS
        );

        return qualificationVOList;
    }

    /**
     * 封装证书列表VO
     *
     * @param qualification 证书信息
     * @return 证书VO
     */
    private QualificationVO.QualificationDetail getQualificationDetail(Qualification qualification) {
        QualificationVO.QualificationDetail qualificationDetail = new QualificationVO.QualificationDetail();
        BeanUtils.copyProperties(qualification, qualificationDetail);
        qualificationDetail.setQualificationId(qualification.getId());
        return qualificationDetail;
    }
}




