package com.okm1208.document.common.exception;

import com.okm1208.document.common.model.CustomizableErrorResponse;
import com.okm1208.document.common.model.DefaultErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerException extends CustomBusinessException{
    private static final DefaultErrorResponse defaultErrorResponse = DefaultErrorResponse.INTERNAL_SERVER_ERROR;

    private InternalServerException(String message) {
        super(CustomizableErrorResponse.of(defaultErrorResponse, message));
    }

    public static InternalServerException of(String message) {
        return new InternalServerException(message);
    }

}
