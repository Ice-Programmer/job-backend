package com.ice.job.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 公司信息
 * @TableName company
 */
@Data
public class CompanyVO implements Serializable {

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
     * 公司Logo
     */
    private String companyLogo;

    /**
     * 公司图片
     */
    private List<String> companyImgList;

    /**
     * 公司产业id
     */
    private Long companyIndustryId;

    /**
     * 公司产业名称
     */
    private String companyIndustryName;

    private static final long serialVersionUID = 6458325359951487588L;
}