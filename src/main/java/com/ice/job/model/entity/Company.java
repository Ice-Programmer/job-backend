package com.ice.job.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 公司信息
 * @TableName company
 */
@TableName(value ="company")
@Data
public class Company implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 公司介绍
     */
    private String companyDescript;

    /**
     * 公司地点
     */
    private String companyAddress;

    /**
     * 所在城市id
     */
    private Long cityId;

    /**
     * 相关数量
     */
    private Integer postNum;

    /**
     * 公司Logo
     */
    private String companyLogo;

    /**
     * 公司图片
     */
    private String companyImg;

    /**
     * 公司产业
     */
    private Long companyIndustry;

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