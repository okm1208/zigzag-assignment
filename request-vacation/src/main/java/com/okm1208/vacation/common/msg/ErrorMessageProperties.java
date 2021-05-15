package com.okm1208.vacation.common.msg;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
public class ErrorMessageProperties {
    public static final String REQUIRED_TOKEN = "필수값 누락 ( 인증 토큰 ) ";

    public static final String MISMATCH_PASSWORD = "패스워드가 일치 하지 않습니다.";
    public static final String INVALID_ACCOUNT = "유효하지 않은 계정입니다.";
    public static final String ACCOUNT_NOT_FOUND = "존재하지 않은 사용자 입니다.";

    public static final String EXPIRED_ACCESS_TOKEN = "엑세스 토큰이 만료되었습니다.";
    public static final String INVALID_TOKEN = "유효하지 않은 토큰입니다.";

    public static final String EMPTY_AUTHORITIES = "유효한 권한이 없습니다.";
    public static final String CREATE_TOKEN_FAILED = "토큰 생성 실패.";

}
