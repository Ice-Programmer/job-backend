package com.ice.job.model.request.recruitment;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/4/4 14:45
 */
@Data
public class RecruitmentCommentAddRequest {

    /**
     * 招聘id
     */
    private Long recruitmentId;

    /**
     * 评分
     */
    private BigDecimal star;

    /**
     * 评价内容
     */
    private String commentText;
}
