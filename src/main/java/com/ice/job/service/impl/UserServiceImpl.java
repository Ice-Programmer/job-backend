package com.ice.job.service.impl;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.ice.job.common.ErrorCode;
import com.ice.job.constant.AvatarDefaultConstant;
import com.ice.job.constant.UserConstant;
import com.ice.job.constant.UserHolder;
import com.ice.job.exception.BusinessException;
import com.ice.job.exception.ThrowUtils;
import com.ice.job.mapper.CityMapper;
import com.ice.job.model.entity.City;
import com.ice.job.model.entity.User;
import com.ice.job.model.request.user.UserLoginRequest;
import com.ice.job.model.request.user.UserRegisterRequest;
import com.ice.job.model.request.user.UserUpdateRequest;
import com.ice.job.model.vo.UserLoginVO;
import com.ice.job.service.UserService;
import com.ice.job.mapper.UserMapper;
import com.ice.job.utils.PrincipalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.ice.job.constant.CacheConstant.LOGIN_USER_KEY;
import static com.ice.job.constant.CacheConstant.LOGIN_USER_TTL;

/**
 * @author chenjiahan
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2024-02-22 11:25:25
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {

    private static final Gson GSON = new Gson();

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private CityMapper cityMapper;

    /**
     * 盐值，混淆密码
     */
    private static final String SALT = "IceProgrammer";

    @Override
    public UserLoginVO userLogin(UserLoginRequest userLoginRequest) {
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        // 1. 验证字段
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }

        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 3. 查询用户是否存在
        User user = baseMapper.selectOne(Wrappers.<User>lambdaQuery()
                .eq(User::getUserAccount, userAccount)
                .eq(User::getUserPassword, encryptPassword)
                .select(User::getId, User::getUserName, User::getUserAvatar, User::getUserRole, User::getCityId)
                .last("limit 1"));

        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在或密码错误");
        }
        // 判断用户是否被ban
        if (UserConstant.BAN_ROLE.equals(user.getUserRole())) {
            log.warn("user {} has been banned", user.getId());
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "用户被禁用");
        }

        // 保存用户到 redis中
        // 随机生成 token，作为登录令牌
        String token = UUID.randomUUID().toString(true);
        UserLoginVO userLoginVO = new UserLoginVO();
        BeanUtils.copyProperties(user, userLoginVO);
        userLoginVO.setToken(token);
        String userJSON = GSON.toJson(userLoginVO);

        // 7.3 储存
        stringRedisTemplate.opsForValue().set(LOGIN_USER_KEY + token, userJSON, LOGIN_USER_TTL, TimeUnit.SECONDS);

        return userLoginVO;
    }

    @Override
    public Long userRegister(UserRegisterRequest userRegisterRequest) {
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        Long cityId = userRegisterRequest.getCityId();
        String userRole = userRegisterRequest.getUserRole();

        // 校验空值
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, userRole)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        // 1. 校验用户账号
        if (userAccount.length() < 4 || userAccount.length() > 15) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号长度错误");
        }

        // 2. 校验密码
        if (userPassword.length() < 8 || userPassword.length() > 20) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码错误");
        }

        // 3. 校验两次密码是否一致
        if (!Objects.equals(checkPassword, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码不一致");
        }

        // 4. 校验城市
        validCity(cityId);


        synchronized (userAccount.intern()) {
            // 账户不能重复
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userAccount", userAccount);
            long count = this.baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
            }

            // 2. 加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

            // 3. 插入数据
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            // 插入默认头像
            user.setUserAvatar(getRandomAvatar(userRole));
            user.setCityId(cityId);
            // 插入默认名称
            String userName = UserConstant.USER_NAME_PREFIX + RandomUtil.randomString(10);
            user.setUserName(userName);
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            return user.getId();
        }
    }

    @Override
    public Long userUpdate(UserUpdateRequest userUpdateRequest) {
        // 1. 校验用户信息
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        validUser(user);

        // 2. 更新用户信息
        baseMapper.updateById(user);

        // 3. 修改缓存
        // 3.1 取出 token
        UserLoginVO loginUser = UserHolder.getUser();
        String token = loginUser.getToken();
        ThrowUtils.throwIf(StringUtils.isBlank(token), ErrorCode.NOT_LOGIN_ERROR);
        // 3.2 查出最新数据
        User newUser = baseMapper.selectOne(Wrappers.<User>lambdaQuery()
                .eq(User::getId, user.getId())
                .select(User::getId, User::getUserName, User::getUserAvatar, User::getUserRole, User::getCityId)
                .last("limit 1"));
        UserLoginVO userLoginVO = new UserLoginVO();
        BeanUtils.copyProperties(newUser, userLoginVO);
        userLoginVO.setToken(token);
        String userJSON = GSON.toJson(userLoginVO);

        // 3.2 修改参数
        stringRedisTemplate.opsForValue().set(LOGIN_USER_KEY + token, userJSON, LOGIN_USER_TTL, TimeUnit.SECONDS);

        return user.getId();
    }

    @Override
    public boolean userLogout() {
        // 取出 token 值
        String token = UserHolder.getUser().getToken();

        // 删除缓存信息
        stringRedisTemplate.delete(LOGIN_USER_KEY + token);

        log.info("user {} logout successfully!", UserHolder.getUser().getId());

        return true;
    }


    /**
     * 校验用户信息
     *
     * @param user 用户信息
     */
    private void validUser(User user) {
        // 1. 判断用户是否存在
        Long id = user.getId();
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户 id 错误！");
        }
        boolean exist = baseMapper.exists(Wrappers.<User>lambdaQuery()
                .eq(User::getId, id));
        ThrowUtils.throwIf(!exist, ErrorCode.NOT_FOUND_ERROR, "用户信息不存在");

        // 2. 校验用户账号
        String userAccount = user.getUserAccount();
        if (userAccount.length() < 4 || userAccount.length() > 15) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号长度错误!");
        }

        // 3. 校验手机
        String userPhone = user.getUserPhone();
        if (StringUtils.isBlank(userPhone) || !PrincipalUtil.isMobile(userPhone)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "手机号错误!");
        }
        // 2. 校验邮箱
        String email = user.getEmail();
        if (StringUtils.isBlank(email) || !PrincipalUtil.isEmail(email)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱格式错误！");
        }

        // 3. 校验用户名
        String userName = user.getUserName();
        if (StringUtils.isBlank(userName) || userName.length() > 10) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户名格式错误");
        }

        // 4. 校验头像
        String userAvatar = user.getUserAvatar();
        if (StringUtils.isBlank(userAvatar)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "头像为空");
        }

        // 5. 校验城市
        Long cityId = user.getCityId();
        validCity(cityId);

    }


    /**
     * 生成随机头像
     *
     * @param userRole 用户身份
     * @return 头像
     */
    private String getRandomAvatar(String userRole) {
        String randomAvatar = null;
        if (UserConstant.EMPLOYEE_ROLE.equals(userRole)) {
            // 随机选择一个 employee 头像
            int avatarIndex = RandomUtil.randomInt(AvatarDefaultConstant.DEFAULT_EMPLOYEE_AVATAR.length);
            randomAvatar = AvatarDefaultConstant.DEFAULT_EMPLOYEE_AVATAR[avatarIndex];
        }
        if (UserConstant.EMPLOYER_ROLE.equals(userRole)) {
            // 随机选择一个 employer 头像
            int avatarIndex = RandomUtil.randomInt(AvatarDefaultConstant.DEFAULT_EMPLOYER_AVATAR.length);
            randomAvatar = AvatarDefaultConstant.DEFAULT_EMPLOYER_AVATAR[avatarIndex];
        }
        if (UserConstant.ADMIN_ROLE.equals(userRole)) {
            randomAvatar = AvatarDefaultConstant.DEFAULT_ADMIN_AVATAR;
        }
        ThrowUtils.throwIf(StringUtils.isBlank(randomAvatar), ErrorCode.PARAMS_ERROR, "用户身份错误");
        return randomAvatar;
    }

    /**
     * 校验城市是否存在
     * @param cityId 城市id
     */
    private void validCity(Long cityId) {
        if (cityId == null || cityId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "所在城市为空");
        }
        boolean exists = cityMapper.exists(Wrappers.<City>lambdaQuery()
                .eq(City::getId, cityId));
        ThrowUtils.throwIf(!exists, ErrorCode.NOT_FOUND_ERROR, "城市信息不存在");
    }
}



