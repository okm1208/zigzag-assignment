package com.okm1208.document.common.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

import java.util.Arrays;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-17
 */
@AllArgsConstructor
public enum ApproveType {
    WAITING("WAITING", "대기"),
    APPROVE("APPROVE" , "승인"),
    REJECT("REJECT","거절");

    final String type;
    final String desc;

    public static ApproveType ofApprovalType(String type){
        return Arrays.stream(ApproveType.values())
                .filter(v -> v.getType().equals(type))
                .findAny()
                .orElseThrow(() -> new RuntimeException("유효하지 않은 승인 타입 입니다."));
    }

    @JsonValue
    public String getType() {
        return type;
    }
    public String getDesc() {
        return desc;
    }
}
