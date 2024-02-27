package com.ice.job.model.request.company;

import lombok.Data;

import java.util.List;

/**
 * 公司信息
 * @TableName company
 */
@Data
public class CompanyUpdateRequest {

    /**
     * id
     */
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
     * 公司Logo
     */
    private String companyLogo;

    /**
     * 公司图片
     */
    private List<String> companyImgList;

    /**
     * 公司产业
     */
    private Long companyIndustry;

}