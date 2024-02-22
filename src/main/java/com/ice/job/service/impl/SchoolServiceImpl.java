package com.ice.job.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ice.job.model.entity.School;
import com.ice.job.service.SchoolService;
import com.ice.job.mapper.SchoolMapper;
import org.springframework.stereotype.Service;

/**
* @author chenjiahan
* @description 针对表【school(专业)】的数据库操作Service实现
* @createDate 2024-02-22 14:47:41
*/
@Service
public class SchoolServiceImpl extends ServiceImpl<SchoolMapper, School>
    implements SchoolService{

}




