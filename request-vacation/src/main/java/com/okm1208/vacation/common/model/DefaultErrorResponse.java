package com.okm1208.vacation.common.model;

import org.springframework.http.HttpStatus;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
public enum DefaultErrorResponse implements ErrorResponse {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "BadRequest", "유효하지 않은 파라미티 입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "NotAuthenticated", "인증이 필요한 서비스 입니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "NotAuthorized", "접근 권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "Resource Not Found", "존재 하지 않는 주소 입니다."),
    DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "Data Not Found", "조회 하려는 데이터가 없습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "Not allowed method", "허용되지 않는 메서드 타입 입니다."),
    NOT_ACCEPTABLE(HttpStatus.NOT_ACCEPTABLE, "Not acceptable", "접근을 허용하지 않습니다."),
    INVALID(HttpStatus.CONFLICT, "Invalid Business", "유효 하지 않은 요청입니다."),
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Not allowed media-type", "혀용되지 않는 요청입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error", "일시 적인 오류가 발생 하였습니다. 잠시 후 다시 시도 해 주세요.");

    final HttpStatus status;
    final String errorType;
    final String errorMessage;

    DefaultErrorResponse(HttpStatus status, String errorType, String errorMessage){
        this.status = status;
        this.errorType = errorType;
        this.errorMessage = errorMessage;
    }


    @Override
    public HttpStatus getStatus() {
        return this.status;
    }

    @Override
    public String getErrorType() {
        return this.errorType;
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }

}
