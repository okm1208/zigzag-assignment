package com.okm1208.vacation.common.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T>{
    private String message;
    private T data;

    public static <T> CommonResponse<T> success(T t){
        return CommonResponse.<T>builder()
                .data(t)
                .build();
    }

    public static <Void> CommonResponse<Void> success(){
        return CommonResponse.<Void>builder()
                .build();
    }
}
