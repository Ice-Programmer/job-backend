package com.ice.job.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/2/24 13:16
 */
@Data
public class EmployeeVO {

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 城市id
     */
    private Long cityId;

    /**
     * 城市名称
     */
    private String cityName;

    /**
     * email
     */
    private String email;

    /**
     * 手机号
     */
    private String userPhone;

    /**
     * 性别
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
     * 技能标签列表
     */
    private List<String> skillTagList;

    /**
     * 最高学历
     */
    private Integer education;

    /**
     * 毕业年份
     */
    private Integer graduateYear;

    /**
     * 求职状态
     */
    private Integer jobStatus;

    /**
     * 期望职位
     */
    private List<EmployeeWishCareerVO> wishCareerInfoList;

    /**
     * 应聘者学历
     */
    private List<EmployeeEducationVO> employeeEducationList;

    /**
     * 应聘者经验
     */
    private List<EmployeeExperienceVO> employeeExperienceList;

    /**
     * 资格证书
     */
    private List<EmployeeQualificationVO> qualificationList;

    @Data
    @NoArgsConstructor
    public static class EmployeeQualificationVO {
        /**
         * id
         */
        private Long id;

        /**
         * 资格证书名称
         */
        private String qualificationName;

        /**
         * 技能证书种类
         */
        private Integer qualificationType;
    }

}
