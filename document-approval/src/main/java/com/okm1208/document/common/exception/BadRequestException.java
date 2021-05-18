package com.okm1208.document.common.exception;


import com.okm1208.document.common.model.CustomizableErrorResponse;
import com.okm1208.document.common.model.DefaultErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends CustomBusinessException{
    private static final DefaultErrorResponse defaultErrorResponse = DefaultErrorResponse.BAD_REQUEST;

    public BadRequestException() {
        super(defaultErrorResponse);
    }

    private BadRequestException(String message) {
        super(CustomizableErrorResponse.of(defaultErrorResponse, message));
    }

    public static BadRequestException of(String message) {
        return new BadRequestException(message);
    }

}
