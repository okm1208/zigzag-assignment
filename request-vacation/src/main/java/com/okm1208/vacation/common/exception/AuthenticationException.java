package com.okm1208.vacation.common.exception;

import com.okm1208.vacation.common.model.CustomizableErrorResponse;
import com.okm1208.vacation.common.model.DefaultErrorResponse;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationException extends CustomBusinessException{

    private static final DefaultErrorResponse defaultErrorResponse
            = DefaultErrorResponse.UNAUTHORIZED;

    public AuthenticationException(String message){
        super(CustomizableErrorResponse.of(defaultErrorResponse, message));
    }
    public static AuthenticationException of(String message) {
        return new AuthenticationException(message);
    }
}

