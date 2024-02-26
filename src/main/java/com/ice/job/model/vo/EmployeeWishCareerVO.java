package com.ice.job.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/2/26 14:12
 */
@Data
public class EmployeeWishCareerVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 职业id
     */
    private PositionVO positionInfo;

    /**
     * 行业id
     */
    private List<IndustryVO> industryInfoList;

    /**
     * 薪水要求（例如：10-15）
     */
    private String salaryExpectation;

    /**
     * 工作城市
     */
    private String cityName;

    private static final long serialVersionUID = 3669433319517821894L;
}
