//package com.ice.job.controller;
//
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.ice.job.common.BaseResponse;
//import com.ice.job.common.DeleteRequest;
//import com.ice.job.common.ErrorCode;
//import com.ice.job.common.ResultUtils;
//import com.ice.job.exception.BusinessException;
//import com.ice.job.exception.ThrowUtils;
//import com.ice.job.model.request.biography.BiographyAddRequest;
//import com.ice.job.model.request.biography.BiographyQueryRequest;
//import com.ice.job.model.request.biography.BiographyUpdateRequest;
//import com.ice.job.model.vo.EmployeeBiographyVO;
//import com.ice.job.service.EmployeeBiographyService;
//import org.springframework.web.bind.annotation.*;
//
//import javax.annotation.Resource;
//
///**
// * 应聘者简历 Controller
// *
// * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
// * @create 2024/2/23 21:18
// */
//@RestController
//@RequestMapping("/employee/biography")
//public class EmployeeBiographyController {
//
//    @Resource
//    private EmployeeBiographyService employeeBiographyService;
//
//    /**
//     * 根据 id 获取应聘者主要经历接口
//     *
//     * @param biographyId 应聘者主要经历id
//     * @return 应聘者主要经历
//     */
//    @GetMapping("/get/{biographyId}")
//    public BaseResponse<EmployeeBiographyVO> getBiography(@PathVariable Long biographyId) {
//        if (biographyId == null || biographyId <= 0) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "应聘者主要经历 id 为空");
//        }
//
//        EmployeeBiographyVO employeeBiographyVO = employeeBiographyService.getBiography(biographyId);
//
//        return ResultUtils.success(employeeBiographyVO);
//    }
//
//    /**
//     * 删除应聘者主要经历接口
//     *
//     * @param deleteRequest 应聘者主要 id
//     * @return 删除结果
//     */
//    @PostMapping("/delete")
//    public BaseResponse<Boolean> deleteBiography(@RequestBody DeleteRequest deleteRequest) {
//        Long id = deleteRequest.getId();
//        if (id == null || id <= 0) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "id参数错误");
//        }
//
//        boolean result = employeeBiographyService.deleteBiography(id);
//
//        return ResultUtils.success(result);
//    }
//
//    /**
//     * 更新应聘者主要经历接口
//     *
//     * @param biographyUpdateRequest 应聘者主要经历参数
//     * @return 更新结果
//     */
//    @PostMapping("/update")
//    public BaseResponse<Boolean> updateBiography(@RequestBody BiographyUpdateRequest biographyUpdateRequest) {
//        if (biographyUpdateRequest == null) {
//            throw new BusinessException(ErrorCode.PARAMS_ERROR, "应聘者主要经历更新参数为空");
//        }
//
//        boolean result = employeeBiographyService.updateBiography(biographyUpdateRequest);
//
//        return ResultUtils.success(result);
//    }
//
//    /**
//     * 获取应聘者主要经历分页接口
//     *
//     * @param biographyQueryRequest 查询条件
//     * @return 应聘者主要经历分页
//     */
//    @PostMapping("/page")
//    public BaseResponse<Page<EmployeeBiographyVO>> pageBiography(@RequestBody BiographyQueryRequest biographyQueryRequest) {
//        long size = biographyQueryRequest.getPageSize();
//        // 限制爬虫
//        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
//
//        Page<EmployeeBiographyVO> biographyVOPage = employeeBiographyService.pageBiography(biographyQueryRequest);
//
//        return ResultUtils.success(biographyVOPage);
//    }
//}
