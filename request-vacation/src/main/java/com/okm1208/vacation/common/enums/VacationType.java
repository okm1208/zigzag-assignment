package com.okm1208.vacation.common.enums;

import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * @author Nick (okm1208@gmail.com)
 * @created 2021-05-14
 */
@AllArgsConstructor
public enum VacationType {
    ANNUAL_LEAVE("AL","연차" ,BigDecimal.valueOf(1.0) ),
    HALF_DAY_LEAVE("HDL","반차",BigDecimal.valueOf(0.5) ),
    HALF_AND_HALF_LEAVE("HAHL","반반차",BigDecimal.valueOf(0.25) );

    final String type;
    final String desc;
    final BigDecimal useDays;

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
    public BigDecimal getUseDays() {
        return useDays;
    }
}
