package com.ice.job.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ice.job.model.entity.Major;
import com.ice.job.service.MajorService;
import com.ice.job.mapper.MajorMapper;
import org.springframework.stereotype.Service;

/**
* @author chenjiahan
* @description 针对表【major(专业)】的数据库操作Service实现
* @createDate 2024-02-22 14:42:10
*/
@Service
public class MajorServiceImpl extends ServiceImpl<MajorMapper, Major>
    implements MajorService{

}




