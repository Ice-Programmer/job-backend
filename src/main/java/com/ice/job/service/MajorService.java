package com.ice.job.service;

import com.ice.job.model.entity.Major;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ice.job.model.vo.MajorVO;

import java.util.List;

/**
* @author chenjiahan
* @description 针对表【major(专业)】的数据库操作Service
* @createDate 2024-02-22 14:42:10
*/
public interface MajorService extends IService<Major> {

    /**
     * 获取专业列表
     *
     * @return 专业列表
     */
    List<MajorVO> listMajor();
}
