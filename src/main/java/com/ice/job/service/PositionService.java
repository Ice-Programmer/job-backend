package com.ice.job.service;

import com.ice.job.model.entity.Position;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ice.job.model.vo.CareerVO;

import java.util.List;

/**
 * @author chenjiahan
 * @description 针对表【position(职业)】的数据库操作Service
 * @createDate 2024-02-26 16:05:39
 */
public interface PositionService extends IService<Position> {

    /**
     * 获取职业列表
     *
     * @return 职业列表
     */
    List<CareerVO> getCareerList();
}
