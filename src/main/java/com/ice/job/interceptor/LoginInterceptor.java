package com.ice.job.interceptor;

import com.ice.job.common.ErrorCode;
import com.ice.job.constant.UserHolder;
import com.ice.job.exception.BusinessException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/2/16 19:52
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 1. 判断是否需要拦截(ThreadLocal 中是否有用户)
        if (UserHolder.getUser() == null) {
            // 没有，需要拦截，设置状态码
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }

        // 有用户则放行
        return true;
    }
}
