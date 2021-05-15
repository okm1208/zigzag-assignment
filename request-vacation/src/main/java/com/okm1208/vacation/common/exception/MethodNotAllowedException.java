package com.okm1208.vacation.common.exception;

import com.okm1208.vacation.common.model.CustomizableErrorResponse;
import com.okm1208.vacation.common.model.DefaultErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
public class MethodNotAllowedException extends CustomBusinessException{
    private static final DefaultErrorResponse defaultErrorResponse = DefaultErrorResponse.METHOD_NOT_ALLOWED;

    public MethodNotAllowedException(String message) {
        super(CustomizableErrorResponse.of(defaultErrorResponse, message));
    }

    public static MethodNotAllowedException of(String message) {
        return new MethodNotAllowedException(message);
    }

}
