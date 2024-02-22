package com.ice.job.model.vo;

import lombok.Data;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/2/22 12:32
 */
@Data
public class UserLoginVO {

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
     * 用户身份
     */
    private String userRole;

    /**
     * 登录 token 值
     */
    private String token;
}
