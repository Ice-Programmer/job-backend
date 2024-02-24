package com.ice.job.service;

import com.ice.job.model.entity.Qualification;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ice.job.model.vo.QualificationVO;

import java.util.List;

/**
* @author chenjiahan
* @description 针对表【qualification(资格证书)】的数据库操作Service
* @createDate 2024-02-24 16:00:58
*/
public interface QualificationService extends IService<Qualification> {

    /**
     * 获取资格证书列表
     *
     * @return 资格证书列表
     */
    List<QualificationVO> getQualificationList();

    /**
     * 应聘者更新资格证书
     *
     * @param qualificationIdList 资格证书 id 列表
     * @return 处理结果
     */
    boolean updateEmployeeQualification(List<Long> qualificationIdList);
}
