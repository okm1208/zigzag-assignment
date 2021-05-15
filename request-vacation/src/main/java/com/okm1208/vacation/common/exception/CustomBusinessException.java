package com.okm1208.vacation.common.exception;

import com.okm1208.vacation.common.model.ErrorResponse;
import lombok.Getter;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
public class CustomBusinessException extends RuntimeException {
    @Getter
    protected ErrorResponse errorResponse;

    public CustomBusinessException(ErrorResponse errorResponse){
        super(errorResponse.getErrorMessage());
        this.errorResponse = errorResponse;
    }
}
