package com.ice.job.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ice.job.model.entity.EmployeeBiography;
import com.ice.job.service.EmployeeBiographyService;
import com.ice.job.mapper.EmployeeBiographyMapper;
import org.springframework.stereotype.Service;

/**
* @author chenjiahan
* @description 针对表【employee_biography(应聘者简历)】的数据库操作Service实现
* @createDate 2024-04-05 14:07:54
*/
@Service
public class EmployeeBiographyServiceImpl extends ServiceImpl<EmployeeBiographyMapper, EmployeeBiography>
    implements EmployeeBiographyService{

}




