package com.ice.job.service;

import com.ice.job.model.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ice.job.model.request.user.UserLoginRequest;
import com.ice.job.model.request.user.UserRegisterRequest;
import com.ice.job.model.vo.UserLoginVO;

/**
 * @author chenjiahan
 * @description 针对表【user(用户)】的数据库操作Service
 * @createDate 2024-02-22 11:25:25
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录
     *
     * @param userLoginRequest 用户登录参数
     * @return 用户信息 & token
     */
    UserLoginVO userLogin(UserLoginRequest userLoginRequest);

    /**
     * 注册用户信息
     *
     * @param userRegisterRequest 账号 & 密码
     * @return 用户id
     */
    Long userRegister(UserRegisterRequest userRegisterRequest);
}
