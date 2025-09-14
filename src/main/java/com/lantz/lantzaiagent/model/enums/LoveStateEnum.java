package com.lantz.lantzaiagent.model.enums;

import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Project: lantz-ai-agent
 * <p>Powered by Lantz On 2025/9/13
 *
 * @author Lantz
 * @version 1.0
 * @Description LoveStateEnum
 * @since 1.8
 */
@Getter
public enum LoveStateEnum {

    SINGLE("单身", "单身"),
    MARRIED("已婚", "已婚"),
    LOVE("恋爱", "恋爱");


    private final String text;

    private final String value;

    LoveStateEnum(String text, String value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 获取值列表
     *
     * @return
     */
    public static List<String> getValues() {
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    /**
     * 根据 value 获取枚举
     *
     * @param value
     * @return
     */
    public static LoveStateEnum getEnumByValue(String value) {
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        for (LoveStateEnum anEnum : LoveStateEnum.values()) {
            if (anEnum.value.equals(value)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getValue() {
        return value;
    }

    public String getText() {
        return text;
    }
}
