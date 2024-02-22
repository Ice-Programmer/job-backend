package com.ice.job.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ice.job.model.entity.Industry;
import com.ice.job.service.IndustryService;
import com.ice.job.mapper.IndustryMapper;
import org.springframework.stereotype.Service;

/**
* @author chenjiahan
* @description 针对表【industry(专业)】的数据库操作Service实现
* @createDate 2024-02-22 15:17:20
*/
@Service
public class IndustryServiceImpl extends ServiceImpl<IndustryMapper, Industry>
    implements IndustryService{

}




