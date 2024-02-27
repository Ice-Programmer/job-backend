package com.ice.job.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ice.job.common.BaseResponse;
import com.ice.job.common.DeleteRequest;
import com.ice.job.common.ErrorCode;
import com.ice.job.common.ResultUtils;
import com.ice.job.exception.BusinessException;
import com.ice.job.exception.ThrowUtils;
import com.ice.job.model.request.company.CompanyAddRequest;
import com.ice.job.model.request.company.CompanyQueryRequest;
import com.ice.job.model.request.company.CompanyUpdateRequest;
import com.ice.job.model.vo.CompanyVO;
import com.ice.job.service.CompanyService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/2/27 12:53
 */
@RestController
@RequestMapping("/company")
public class CompanyController {

    @Resource
    private CompanyService companyService;

    /**
     * 添加公司接口
     *
     * @param companyAddRequest 公司请求
     * @return companyId
     */
    @PostMapping("/add")
    public BaseResponse<Long> addCompany(@RequestBody CompanyAddRequest companyAddRequest) {
        if (companyAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "公司添加参数为空！");
        }

        Long companyId = companyService.addCompany(companyAddRequest);

        return ResultUtils.success(companyId);
    }

    /**
     * 根据 id 获取公司接口
     *
     * @param companyId 公司id
     * @return 公司
     */
    @GetMapping("/get/{companyId}")
    public BaseResponse<CompanyVO> getCompany(@PathVariable Long companyId) {
        if (companyId == null || companyId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "公司 id 为空");
        }

        CompanyVO employeeCompanyVO = companyService.getCompany(companyId);

        return ResultUtils.success(employeeCompanyVO);
    }

    /**
     * 删除公司接口
     *
     * @param deleteRequest 教育 id
     * @return 删除结果
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteCompany(@RequestBody DeleteRequest deleteRequest) {
        Long id = deleteRequest.getId();
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "id参数错误");
        }

        boolean result = companyService.deleteCompany(id);

        return ResultUtils.success(result);
    }

    /**
     * 更新公司接口
     *
     * @param companyUpdateRequest 公司参数
     * @return 更新结果
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updateCompany(@RequestBody CompanyUpdateRequest companyUpdateRequest) {
        if (companyUpdateRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "公司更新参数为空");
        }

        boolean result = companyService.updateCompany(companyUpdateRequest);

        return ResultUtils.success(result);
    }

    /**
     * 获取公司分页接口
     *
     * @param companyQueryRequest 查询条件
     * @return 公司分页
     */
    @PostMapping("/page")
    public BaseResponse<Page<CompanyVO>> pageCompany(@RequestBody CompanyQueryRequest companyQueryRequest) {
        long size = companyQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);

        Page<CompanyVO> companyVOPage = companyService.pageCompany(companyQueryRequest);

        return ResultUtils.success(companyVOPage);
    }
}
