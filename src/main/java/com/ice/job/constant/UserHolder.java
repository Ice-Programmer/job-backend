package com.ice.job.constant;

import com.ice.job.model.vo.UserLoginVO;

public class UserHolder {
    private static final ThreadLocal<UserLoginVO> user = new ThreadLocal<>();

    public static void saveUser(UserLoginVO user) {
        UserHolder.user.set(user);
    }

    public static UserLoginVO getUser() {
        return user.get();
    }

    public static void removeUser() {
        user.remove();
    }
}
