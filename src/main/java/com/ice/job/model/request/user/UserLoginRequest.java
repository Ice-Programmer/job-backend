package com.ice.job.model.request.user;

import lombok.Data;

import java.io.Serializable;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/2/22 12:25
 */
@Data
public class UserLoginRequest implements Serializable {

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;

    /**
     * 用户身份
     */
    private String userRole;

    private static final long serialVersionUID = -6132531831232799224L;
}
