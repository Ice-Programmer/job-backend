package com.ice.job.model.vo;

import lombok.Data;

import java.util.List;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/2/26 21:13
 */
@Data
public class CareerVO {

    /**
     * id
     */
    private Long id;

    /**
     * 职业类型
     */
    private String positionType;

    /**
     * 职业列表
     */
    private List<PositionVO> positionInfoList;
}
