package com.okm1208.document.common.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

import java.util.Arrays;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-17
 */
@AllArgsConstructor
public enum DocumentType {
    NORMAL("NM" , "일반"),
    CONTRACT("CNT","계약"),
    BUSINESS_SUPPORT("BS" , "경영지원"),
    ETC("ETC","기타");

    final String type;
    final String desc;

    public static DocumentType ofDocumentType(String type){
        return Arrays.stream(DocumentType.values())
                .filter(v -> v.getType().equals(type))
                .findAny()
                .orElseThrow(() -> new RuntimeException("유효 하지 않은 문서 타입 입니다."));
    }

    @JsonValue
    public String getType() {
        return type;
    }
    public String getDesc() {
        return desc;
    }
}