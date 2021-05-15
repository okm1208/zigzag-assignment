package com.okm1208.vacation.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import javax.persistence.GeneratedValue;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
@Getter
@ToString
@AllArgsConstructor
public class CustomizableErrorResponse implements ErrorResponse{
    private HttpStatus status;
    private String errorType;
    private String errorMessage;

    public static CustomizableErrorResponse of(Integer httpStatus, String errorMessage) {
        ErrorResponse errorResponse = ErrorResponse.of(httpStatus);

        return new CustomizableErrorResponse(errorResponse.getStatus(), errorResponse.getErrorType(), errorMessage);
    }

    public static CustomizableErrorResponse of(DefaultErrorResponse errorResponse, String errorMessage) {
        return new CustomizableErrorResponse(errorResponse.getStatus(), errorResponse.getErrorType(), errorMessage);
    }




}
