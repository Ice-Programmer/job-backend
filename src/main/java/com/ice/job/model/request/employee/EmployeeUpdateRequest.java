package com.ice.job.model.request.employee;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/2/22 14:05
 */
@Data
public class EmployeeUpdateRequest implements Serializable {

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 性别（0 - 女 1 - 男）
     */
    private Integer gender;

    /**
     * 年龄
     */
    private Integer age;

    /**
     * 个人优势
     */
    private String advantage;

    /**
     * 资格证书列表
     */
    private List<Long> qualificationIdList;

    /**
     * 技能标签
     */
    private List<String> skillTagList;

    /**
     * 最高学历
     */
    private Integer education;

    /**
     * 行业期望
     */
    private List<Long> industryIdLIst;

    /**
     * 毕业年份
     */
    private Integer graduateYear;

    /**
     * 求职状态
     */
    private Integer jobStatus;

    /**
     * 薪水要求（例如：10-15）
     */
    private String salaryExpectation;

    /**
     * 简历地址
     */
    private String biography;

    /**
     * 居住地（省-市-区）
     */
    private String address;

    private static final long serialVersionUID = 1L;
}
