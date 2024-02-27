package com.ice.job.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ice.job.model.entity.Company;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ice.job.model.request.company.CompanyAddRequest;
import com.ice.job.model.request.company.CompanyQueryRequest;
import com.ice.job.model.request.company.CompanyUpdateRequest;
import com.ice.job.model.vo.CompanyVO;

/**
 * @author chenjiahan
 * @description 针对表【company(公司信息)】的数据库操作Service
 * @createDate 2024-02-27 12:53:24
 */
public interface CompanyService extends IService<Company> {

    /**
     * 添加公司
     *
     * @param companyAddRequest 公司请求
     * @return companyId
     */
    Long addCompany(CompanyAddRequest companyAddRequest);

    /**
     * 根据 id 获取公司
     *
     * @param companyId 公司id
     * @return 公司
     */
    CompanyVO getCompany(Long companyId);

    /**
     * 获取公司分页
     *
     * @param companyQueryRequest 查询条件
     * @return 公司分页
     */
    Page<CompanyVO> pageCompany(CompanyQueryRequest companyQueryRequest);

    /**
     * 删除公司
     *
     * @param id 教育 id
     * @return 删除结果
     */
    boolean deleteCompany(Long id);

    /**
     * 更新公司
     *
     * @param companyUpdateRequest 公司参数
     * @return 更新结果
     */
    boolean updateCompany(CompanyUpdateRequest companyUpdateRequest);

}
