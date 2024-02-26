package com.ice.job.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ice.job.model.entity.Position;
import com.ice.job.service.PositionService;
import com.ice.job.mapper.PositionMapper;
import org.springframework.stereotype.Service;

/**
* @author chenjiahan
* @description 针对表【position(职业)】的数据库操作Service实现
* @createDate 2024-02-26 16:05:38
*/
@Service
public class PositionServiceImpl extends ServiceImpl<PositionMapper, Position>
    implements PositionService{

}




