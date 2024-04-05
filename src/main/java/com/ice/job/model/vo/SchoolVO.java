package com.ice.job.model.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/4/5 19:35
 */
@Data
public class SchoolVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 专业名称
     */
    private String schoolName;

    private static final long serialVersionUID = 5549503992054776616L;

}
