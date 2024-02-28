package com.ice.job.model.enums;

import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2023/7/28 11:44
 */
public enum JobTypeEnum {
    // 春招 秋招 暑期实习 实习 兼职 全职
    SPRING("春招", 1),
    AUTUMN("秋招", 2),
    SUMMER_INTERNSHIP("暑期实习", 3),
    INTERNSHIP("实习", 4),
    PART_TIME("兼职", 5),
    FULL_TIME("全职", 6);


    private final Integer value;

    private final String name;

    JobTypeEnum(String name, Integer value) {
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
    public static JobTypeEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (JobTypeEnum anEnum : JobTypeEnum.values()) {
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
    public static JobTypeEnum getEnumByName(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (JobTypeEnum anEnum : JobTypeEnum.values()) {
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
