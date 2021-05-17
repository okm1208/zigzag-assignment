package com.okm1208.document.common.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

import java.util.Arrays;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-17
 */
@AllArgsConstructor
public enum ApproveStatusType {
    WAITING("0", "승인 대기"),
    APPROVING("1" , "승인중"),
    APPROVE("2","승인 완료"),
    REJECT("3","거절 완료");

    final String type;
    final String desc;

    public static ApproveStatusType ofApprovalStatusType(String type){
        return Arrays.stream(ApproveStatusType.values())
                .filter(v -> v.getType().equals(type))
                .findAny()
                .orElseThrow(() -> new RuntimeException("유효하지 않은 승인 상태 타입 입니다."));
    }

    @JsonValue
    public String getType() {
        return type;
    }
    public String getDesc() {
        return desc;
    }
}
