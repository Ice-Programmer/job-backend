package com.ice.job.model.enums;

import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2023/7/28 11:44
 */
public enum SalaryUnitEnum {
    // 日薪 月薪
    daily_pay("日薪", 0),
    monthly_pay("月薪", 1);


    private final Integer value;

    private final String name;

    SalaryUnitEnum(String name, Integer value) {
        this.value = value;
        this.name = name;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.name).collect(Collectors.toList());
    }

    public static List<Integer> getIntegerValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static SalaryUnitEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (SalaryUnitEnum anEnum : SalaryUnitEnum.values()) {
            if (anEnum.name.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    /**
     * 根据 name 获取枚举
     *
     * @param value
     * @return
     */
    public static SalaryUnitEnum getEnumByName(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (SalaryUnitEnum anEnum : SalaryUnitEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }
}
