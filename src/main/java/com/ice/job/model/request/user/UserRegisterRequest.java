package com.ice.job.model.request.user;

import lombok.Data;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/2/22 18:39
 */
@Data
public class UserRegisterRequest {

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 确认密码
     */
    private String checkPassword;

    /**
     * 城市id
     */
    private Long cityId;

    /**
     * 用户身份
     */
    private String userRole;
}
