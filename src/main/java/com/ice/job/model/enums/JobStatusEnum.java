package com.ice.job.model.enums;

import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2023/7/28 11:44
 */
public enum JobStatusEnum {
    AVAILABLE("离校随时到岗", 0),
    WITH_IN_MONTH("在校月内到岗", 1),
    OPEN_OPPORTUNITY("在校考虑机会", 2),
    NOT_INTERESTED("在校暂不考虑", 3);


    private final Integer value;

    private final String name;

    JobStatusEnum(String name, Integer value) {
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
    public static JobStatusEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (JobStatusEnum anEnum : JobStatusEnum.values()) {
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
    public static JobStatusEnum getEnumByName(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (JobStatusEnum anEnum : JobStatusEnum.values()) {
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
