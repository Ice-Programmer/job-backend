package com.ice.job.model.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 城市 VO
 *
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2024/2/26 12:53
 */
@Data
@NoArgsConstructor
public class AddressVO implements Serializable {

    /**
     * 省份id
     */
    private Long id;

    /**
     * 省份名称
     */
    private String provinceName;

    /**
     * 城市列表
     */
    private List<CityVO> cityList;

    @Data
    @NoArgsConstructor
    public static class CityVO {
        /**
         * 城市id
         */
        private Long id;

        /**
         * 城市名称
         */
        private String cityName;
    }

    private static final long serialVersionUID = 7810513841803117163L;

}
