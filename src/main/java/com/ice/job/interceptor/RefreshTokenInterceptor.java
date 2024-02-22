package com.ice.job.interceptor;

import cn.hutool.core.util.StrUtil;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.ice.job.constant.CacheConstant;
import com.ice.job.constant.UserHolder;
import com.ice.job.model.vo.UserLoginVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/2/16 19:52
 */
public class RefreshTokenInterceptor implements HandlerInterceptor {

    private static final Gson GSON = new Gson();

    private final StringRedisTemplate stringRedisTemplate;

    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 1. 获取请求头中的 token
        String token = request.getHeader("authorization");
        if (StrUtil.isBlank(token)) {
            return true;
        }

        // 2. 基于 token 获取 redis 中的用户
        String userJSON = stringRedisTemplate.opsForValue().get(CacheConstant.LOGIN_USER_KEY + token);

        // 3. 判断用户是否存在
        if (StringUtils.isBlank(userJSON)) {
            return true;
        }
        // 5. 将查询到的 JSON 数据转为对象
        UserLoginVO userVO = GSON.fromJson(userJSON, new TypeToken<UserLoginVO>() {
        }.getType());
        // 6. 保存 UserDTO 到ThreadLocal
        UserHolder.saveUser(userVO);

        // 7. 刷新 token 有效期
        stringRedisTemplate.expire(CacheConstant.LOGIN_USER_KEY, CacheConstant.LOGIN_USER_TTL, TimeUnit.MINUTES);
        // 6. 放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 移除用户，避免内存泄漏
        UserHolder.removeUser();
    }
}
