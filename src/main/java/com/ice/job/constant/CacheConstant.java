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
    String USER_EMPLOYEE_KEY = "user:employee:info:";
    Long user_employee_ttl = 24 * 60 * 60L;    // 1 day
}
