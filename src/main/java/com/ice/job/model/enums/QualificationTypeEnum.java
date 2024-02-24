package com.ice.job.model.enums;

import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/IceProgramer">chenjiahan</a>
 * @create 2023/7/28 11:44
 */
public enum QualificationTypeEnum {
    ENGLISH("英语类", 0),
    FOREIGN_LANGUAGE("外语类", 1),
    IT("IT类", 2),
    PROJECT_MANAGEMENT("项目管理类", 3),
    ACCOUNTING("会计类", 4),
    AUDITING("审计类", 5),
    STATISTICS("统计类", 6),
    ACTUARY("精算类", 7),
    FINANCE("金融类", 8),
    LAW("法律类", 9),
    EDUCATION("教育类", 10),
    PRESCHOOL_EDUCATION("幼教类", 11),
    CHINESE("汉语类", 12),
    PSYCHOLOGY_CONSULTING("心理咨询类", 13),
    MEDICAL("医疗类", 14),
    SPORTS_FITNESS("体育健身类", 15),
    REAL_ESTATE_CONSTRUCTION("房地产建筑类", 16),
    HR_ADMINISTRATION("人力行政类", 17),
    ART("艺术类", 18),
    NUTRITIONIST("营养师类", 19),
    DRIVING("驾驶类", 20),
    SKILLED_WORKER("技工类", 21),
    PRODUCTION_MANUFACTURING("生产制造类", 22),
    MARKETING("市场类", 23),
    TOURISM("旅游类", 24),
    MEDIA("传媒类", 25),
    AGRICULTURE_FORESTRY_ANIMAL_HUSBANDRY_FISHERY("农/林/牧/渔类", 26),
    ENVIRONMENTAL_PROTECTION("环保类", 27),
    SPECIAL("特殊类", 28),
    TRANSPORTATION("交通运输类", 29);


    private final Integer value;

    private final String name;

    QualificationTypeEnum(String name, Integer value) {
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
    public static QualificationTypeEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (QualificationTypeEnum anEnum : QualificationTypeEnum.values()) {
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
    public static QualificationTypeEnum getEnumByName(Integer value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (QualificationTypeEnum anEnum : QualificationTypeEnum.values()) {
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
