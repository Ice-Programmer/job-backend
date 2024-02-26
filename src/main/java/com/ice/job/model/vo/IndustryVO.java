package com.ice.job.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 职位 VO
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/2/26 14:31
 */
@Data
public class IndustryVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 职业名称
     */
    private String industryName;

    private static final long serialVersionUID = -8229700507252366729L;

}
