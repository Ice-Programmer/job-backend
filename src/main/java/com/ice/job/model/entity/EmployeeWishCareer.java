package com.ice.job.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName employee_wish_career
 */
@TableName(value ="employee_wish_career")
@Data
public class EmployeeWishCareer implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 职业id
     */
    private Long positionId;

    /**
     * 行业id
     */
    private String industryIds;

    /**
     * 薪水要求（例如：10-15）
     */
    private String salaryExpectation;

    /**
     * 工作城市
     */
    private Long cityId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}