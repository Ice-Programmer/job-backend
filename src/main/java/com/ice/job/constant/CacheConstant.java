package com.ice.job.constant;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/2/22 12:19
 */
public interface CacheConstant {

    /**
     * 用户 token
     */
    String LOGIN_USER_KEY = "user:token:";
    Long LOGIN_USER_TTL = 24 * 60 * 60L;    // 1 day

    /**
     * 应聘者信息
     */
    String USER_EMPLOYEE_KEY = "user:employer:info:";
    Long USER_EMPLOYEE_TTL = 24 * 60 * 60L;    // 1 day

    /**
     * 招聘者信息
     */
    String USER_EMPLOYER_KEY = "user:employer:info:";
    Long USER_EMPLOYER_TTL = 24 * 60 * 60L;    // 1 day

    /**
     * 应聘者资格证书
     */
    String EMPLOYEE_QUALIFICATION_KEY = "employee:qualification:list";
    Long EMPLOYEE_QUALIFICATION_TTL = 30 * 24 * 60 * 60L;    // 30 day

    /**
     * 城市列表类型
     */
    String CITY_LIST_KEY = "others:city:list";
    Long CITY_LIST_TTL = 30 * 24 * 60 * 60L;    // 30 day

    /**
     * 职业列表类型
     */
    String CAREER_LIST_KEY = "other:career:list";
    Long CAREER_LIST_TTL = 30 * 24 * 60 * 60L;    // 30 day

    /**
     * 公司类型
     */
    String COMPANY_INFO_KEY = "company:info:";
    Long COMPANY_INFO_TTL = 30 * 24 * 60 * 60L;    // 30 day
}
