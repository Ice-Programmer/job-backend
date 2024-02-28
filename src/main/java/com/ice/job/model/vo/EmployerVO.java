package com.ice.job.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 招聘者
 * @TableName employer
 */
@Data
public class EmployerVO implements Serializable {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 公司id
     */
    private Long companyId;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 职位名称
     */
    private String positionName;

    /**
     * 城市id
     */
    private Long cityId;

    /**
     * 城市名称
     */
    private String cityName;

    private static final long serialVersionUID = 4292464034911492699L;

}