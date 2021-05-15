package com.okm1208.vacation.common.exception;

import com.okm1208.vacation.common.model.CustomizableErrorResponse;
import com.okm1208.vacation.common.model.DefaultErrorResponse;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
public class DataNotFoundException extends CustomBusinessException{
    private static final DefaultErrorResponse defaultErrorResponse = DefaultErrorResponse.DATA_NOT_FOUND;

    public DataNotFoundException() {
        super(defaultErrorResponse);
    }

    private DataNotFoundException(String message) {
        super(CustomizableErrorResponse.of(defaultErrorResponse, message));
    }

    public static DataNotFoundException of(String message) {
        return new DataNotFoundException(message);
    }

}
