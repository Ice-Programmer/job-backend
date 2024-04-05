package com.ice.job.service;

import com.ice.job.model.entity.School;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ice.job.model.vo.SchoolVO;

import java.util.List;

/**
* @author chenjiahan
* @description 针对表【school(专业)】的数据库操作Service
* @createDate 2024-02-22 14:47:41
*/
public interface SchoolService extends IService<School> {

    /**
     * 获取学校列表
     *
     * @return 学校列表
     */
    List<SchoolVO> listSchool();
}
