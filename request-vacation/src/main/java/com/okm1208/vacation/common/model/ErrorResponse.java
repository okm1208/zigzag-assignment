package com.okm1208.vacation.common.model;

import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
public interface ErrorResponse {
    HttpStatus getStatus();
    String getErrorType();
    String getErrorMessage();

    static ErrorResponse of(int status){
        Stream<ErrorResponse> stream = Stream.of(DefaultErrorResponse.values());

        return stream
                .filter(e -> e.getStatus().value() == status)
                .findFirst()
                .orElse(new UnknownErrorResponse(status))
                ;
    }
}
