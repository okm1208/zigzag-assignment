package com.okm1208.document.common.model;

import org.springframework.http.HttpStatus;

import java.util.Optional;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
public class UnknownErrorResponse implements ErrorResponse {
    private HttpStatus status;

    public UnknownErrorResponse(int status){
        this.status = HttpStatus.valueOf(status);
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getErrorType() {
        return Optional.ofNullable(this.status)
                .map(HttpStatus::getReasonPhrase)
                .orElse(DefaultErrorResponse.INTERNAL_SERVER_ERROR.getErrorType())
                ;
    }

    @Override
    public String getErrorMessage() {
        return DefaultErrorResponse.INTERNAL_SERVER_ERROR.getErrorMessage();
    }
}
