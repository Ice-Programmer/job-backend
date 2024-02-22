package com.ice.job.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ice.job.entity.User;
import com.ice.job.service.UserService;
import com.ice.job.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author chenjiahan
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2024-02-22 11:25:25
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




