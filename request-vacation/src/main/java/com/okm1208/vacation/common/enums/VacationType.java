package com.okm1208.vacation.common.enums;

import lombok.AllArgsConstructor;

import java.util.Arrays;

/**
 * @author Nick (okm1208@gmail.com)
 * @created 2021-05-14
 */
@AllArgsConstructor
public enum VacationType {
    ANNUAL_LEAVE("AL","활성"),
    HALF_DAY_LEAVE("HDL","잠김"),
    HALF_AND_HALF_LEAVE("HAHL","탈퇴");

    final String type;
    final String desc;

    public static VacationType ofVacationType(String type){
        return Arrays.stream(VacationType.values())
                .filter(v -> v.getType().equals(type))
                .findAny()
                .orElseThrow(() -> new RuntimeException(""));
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
