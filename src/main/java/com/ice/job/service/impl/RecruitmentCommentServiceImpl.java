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
import com.ice.job.mapper.RecruitmentCommentLikeMapper;
import com.ice.job.mapper.RecruitmentCommentMapper;
import com.ice.job.mapper.RecruitmentMapper;
import com.ice.job.mapper.UserMapper;
import com.ice.job.model.entity.Recruitment;
import com.ice.job.model.entity.RecruitmentComment;
import com.ice.job.model.entity.RecruitmentCommentLike;
import com.ice.job.model.entity.User;
import com.ice.job.model.request.recruitment.RecruitmentCommentAddRequest;
import com.ice.job.model.request.recruitmentcomment.RecruitmentCommentQueryRequest;
import com.ice.job.model.vo.RecruitmentCommentVO;
import com.ice.job.model.vo.UserLoginVO;
import com.ice.job.service.RecruitmentCommentService;
import com.ice.job.utils.SqlUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author chenjiahan
 * @description 针对表【recruitment_comment(招聘评价)】的数据库操作Service实现
 * @createDate 2024-04-04 14:43:14
 */
@Service
public class RecruitmentCommentServiceImpl extends ServiceImpl<RecruitmentCommentMapper, RecruitmentComment>
        implements RecruitmentCommentService {

    @Resource
    private RecruitmentMapper recruitmentMapper;

    @Resource
    private RecruitmentCommentLikeMapper recruitmentCommentLikeMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public Long addRecruitmentComment(RecruitmentCommentAddRequest recruitmentCommentAddRequest) {
        RecruitmentComment recruitmentComment = new RecruitmentComment();
        BeanUtils.copyProperties(recruitmentCommentAddRequest, recruitmentComment);
        // 校验职位评价
        validComment(recruitmentComment);

        Long userId = UserHolder.getUser().getId();
        recruitmentComment.setUserId(userId);

        baseMapper.insert(recruitmentComment);

        return recruitmentComment.getId();
    }

    @Override
    public Page<RecruitmentCommentVO> pageRecruitmentComment(RecruitmentCommentQueryRequest recruitmentCommentQueryRequest) {
        long current = recruitmentCommentQueryRequest.getCurrent();
        long size = recruitmentCommentQueryRequest.getPageSize();

        Page<RecruitmentComment> recruitmentCommentPage = baseMapper.selectPage(new Page<>(current, size),
                getQueryWrapper(recruitmentCommentQueryRequest));

        List<RecruitmentComment> recruitmentCommentList = recruitmentCommentPage.getRecords();

        // 包装VO类
        List<RecruitmentCommentVO> recruitmentCommentVOList = getRecruitmentCommentVOList(recruitmentCommentList);

        Page<RecruitmentCommentVO> recruitmentCommentVOPage = new Page<>(recruitmentCommentPage.getCurrent(), recruitmentCommentPage.getSize(), recruitmentCommentPage.getTotal());

        recruitmentCommentVOPage.setRecords(recruitmentCommentVOList);

        return recruitmentCommentVOPage;
    }

    private List<RecruitmentCommentVO> getRecruitmentCommentVOList(List<RecruitmentComment> recruitmentCommentList) {
        if (CollectionUtils.isEmpty(recruitmentCommentList)) {
            return Collections.emptyList();
        }
        // 1. 关联查询用户信息
        Set<Long> userIdSet = recruitmentCommentList.stream().map(RecruitmentComment::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userMapper.selectList(Wrappers.<User>lambdaQuery()
                        .in(User::getId, userIdSet)
                        .select(User::getId, User::getUserName, User::getUserAvatar))
                .stream()
                .collect(Collectors.groupingBy(User::getId));

        // 2. 已登录，获取用户点赞、收藏状态
        Map<Long, Boolean> commentIdHasLikeMap = new HashMap<>();
        UserLoginVO loginUser = UserHolder.getUser();
        Set<Long> recruitmentCommentIdSet = recruitmentCommentList.stream().map(RecruitmentComment::getId).collect(Collectors.toSet());
        // 获取点赞
        List<RecruitmentCommentLike> recruitmentCommentLikeList = recruitmentCommentLikeMapper.selectList(Wrappers.<RecruitmentCommentLike>lambdaQuery()
                .in(RecruitmentCommentLike::getRecruitmentCommentId, recruitmentCommentIdSet)
                .eq(RecruitmentCommentLike::getUserId, loginUser.getId())
                .select(RecruitmentCommentLike::getRecruitmentCommentId));
        recruitmentCommentLikeList.forEach(recruitmentCommentLike ->
                commentIdHasLikeMap.put(
                        recruitmentCommentLike.getRecruitmentCommentId(), true
                ));

        // 填充信息
        List<RecruitmentCommentVO> recruitmentCommentVOList = recruitmentCommentList.stream().map(comment -> {
            RecruitmentCommentVO recruitmentCommentVO = new RecruitmentCommentVO();
            BeanUtils.copyProperties(comment, recruitmentCommentVO);

            Long userId = comment.getUserId();

            // 封装用户信息
            RecruitmentCommentVO.publisherInfo publisherInfo = new RecruitmentCommentVO.publisherInfo();
            BeanUtils.copyProperties(userIdUserListMap.get(userId).get(0), publisherInfo);
            recruitmentCommentVO.setPublisherInfo(publisherInfo);

            // 封装是否点赞
            recruitmentCommentVO.setHasLiked(commentIdHasLikeMap.getOrDefault(comment.getId(), false));
            return recruitmentCommentVO;
        }).collect(Collectors.toList());
        return recruitmentCommentVOList;

    }

    private QueryWrapper<RecruitmentComment> getQueryWrapper(RecruitmentCommentQueryRequest recruitmentCommentQueryRequest) {
        QueryWrapper<RecruitmentComment> queryWrapper = new QueryWrapper<>();

        if (recruitmentCommentQueryRequest == null) {
            return queryWrapper;
        }

        Long id = recruitmentCommentQueryRequest.getId();
        List<Long> ids = recruitmentCommentQueryRequest.getIds();

        String sortField = recruitmentCommentQueryRequest.getSortField();
        String sortOrder = recruitmentCommentQueryRequest.getSortOrder();

        queryWrapper.eq(ObjectUtils.isNotEmpty(id), "id", id);
        if (!CollectionUtils.isEmpty(ids)) {
            queryWrapper.in("id", id);
        }

        Long userId = recruitmentCommentQueryRequest.getUserId();
        queryWrapper.eq(ObjectUtils.isNotEmpty(userId), "userId", userId);
        Long recruitmentId = recruitmentCommentQueryRequest.getRecruitmentId();
        queryWrapper.eq(ObjectUtils.isNotEmpty(recruitmentId), "recruitmentId", recruitmentId);

        queryWrapper.eq("isDelete", false);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    private void validComment(RecruitmentComment recruitmentComment) {
        // 1. 校验评价信息
        String commentText = recruitmentComment.getCommentText();
        if (StringUtils.isBlank(commentText) || commentText.length() >= 300) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "评价内容为空");
        }
        // 2. 校验评分
        BigDecimal star = recruitmentComment.getStar();
        if (star == null || star.compareTo(BigDecimal.valueOf(5)) > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "评价评分为空或分数大于 5");
        }
        // 3. 校验评价id
        Long recruitmentId = recruitmentComment.getRecruitmentId();
        if (recruitmentId == null || recruitmentId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "评价id为空");
        }
        boolean exists = recruitmentMapper.exists(Wrappers.<Recruitment>lambdaQuery()
                .eq(Recruitment::getId, recruitmentId));
        ThrowUtils.throwIf(!exists, ErrorCode.NOT_FOUND_ERROR, "评价职位信息为空");

    }
}




