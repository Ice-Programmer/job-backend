package com.ice.job.controller;

import com.ice.job.common.BaseResponse;
import com.ice.job.common.ErrorCode;
import com.ice.job.common.ResultUtils;
import com.ice.job.exception.BusinessException;
import com.ice.job.model.request.user.UserLoginRequest;
import com.ice.job.model.request.user.UserRegisterRequest;
import com.ice.job.model.request.user.UserUpdateRequest;
import com.ice.job.model.vo.UserLoginVO;
import com.ice.job.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户登录接口
 *
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/2/22 12:23
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户登录接口
     *
     * @param userLoginRequest 用户登录参数
     * @return 用户信息 & token
     */
    @PostMapping("/login")
    public BaseResponse<UserLoginVO> userLogin(@RequestBody UserLoginRequest userLoginRequest) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "登录参数为空！");
        }

        UserLoginVO userLoginVO = userService.userLogin(userLoginRequest);

        return ResultUtils.success(userLoginVO);
    }

    /**
     * 注册用户信息
     *
     * @param userRegisterRequest 账号 & 密码
     * @return 用户id
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Long userId = userService.userRegister(userRegisterRequest);

        return ResultUtils.success(userId);
    }

    /**
     * 更新用户信息
     *
     * @param userUpdateRequest 用户更新参数
     * @return 用户id
     */
    @PostMapping("/update")
    public BaseResponse<Long> userUpdate(@RequestBody UserUpdateRequest userUpdateRequest) {
        if (userUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户更新请求参数错误!");
        }

        Long userId = userService.userUpdate(userUpdateRequest);

        return ResultUtils.success(userId);
    }

    /**
     * 用户退出登录
     *
     * @return 退出成功
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout() {

        boolean result = userService.userLogout();

        return ResultUtils.success(result);
    }

}