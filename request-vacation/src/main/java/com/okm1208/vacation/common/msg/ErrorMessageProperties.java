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

    public static final String KAKAO_API_BAD_REQUEST = "카카오 API : 잘못된 요청을 감지 하였습니다.";
    public static final String KAKAO_API_UNAUTHORIZED = "카카오 API : 인증이 실패 하였습니다.";
    public static final String KAKAO_API_FORBIDDEN = "카카오 API : 권한이 없습니다.";
    public static final String KAKAO_API_SERVICE_UNAVAILABLE = "카카오 API : 서비스 점검중 입니다.";
    public static final String KAKAO_API_INTERNAL_SERVER_ERROR = "카카오 API : 서버 에러 입니다.";


    public static final String NAVER_API_BAD_REQUEST = "네이버 API : 잘못된 요청을 감지 하였습니다.";
    public static final String NAVER_API_UNAUTHORIZED = "네이버 API : 인증이 실패 하였습니다.";
    public static final String NAVER_API_FORBIDDEN = "네이버 API : 권한이 없습니다.";
    public static final String NAVER_API_NOT_FOIND = "네이버 API : 요청 API가 없습니다.";
    public static final String NAVER_METHOD_NOT_ALLOWED = "네이버 API : 허용하지 않은 메서드 입니다.";
    public static final String NAVER_TOO_MANY_REQUESTS = "네이버 API : 호출 한도 초과 오류 입니다.";
    public static final String NAVER_API_INTERNAL_SERVER_ERROR = "네이버 API : 서버 에러 입니다.";

}
