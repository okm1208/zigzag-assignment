package com.okm1208.document.common.msg;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
public class ErrorMessageProperties {

    public static final String MISMATCH_PASSWORD = "패스워드가 일치 하지 않습니다.";
    public static final String INVALID_ACCOUNT = "유효하지 않은 계정입니다.";
    public static final String ACCOUNT_NOT_FOUND = "존재하지 않은 사용자 입니다.";

    public static final String EXPIRED_ACCESS_TOKEN = "엑세스 토큰이 만료되었습니다.";
    public static final String INVALID_TOKEN = "유효하지 않은 토큰입니다.";

    public static final String EMPTY_DATA = "유효한 데이터가 없습니다.";

    public static final String EMPTY_AUTHORITIES = "유효한 권한이 없습니다.";
    public static final String CREATE_TOKEN_FAILED = "토큰 생성 실패.";

    public static final String CREATE_ERROR_01 ="결제자를 한명 이상 지정해주세요.";
    public static final String CREATE_ERROR_02 ="결제자가 존재 하지 않습니다.";

    public static final String APPROVE_ERROR_01 = "해당 문서에 결제 권한이 존재 하지 않습니다.";
    public static final String APPROVE_ERROR_02 = "현재 결제 가능한 상태의 문서가 아닙니다.";
    public static final String APPROVE_ERROR_03 = "먼저 결제 해야할 건이 존재합니다.";
    public static final String APPROVE_ERROR_04 = "해당 문서는 거절된 문서입니다.";
    public static final String APPROVE_ERROR_05 = "요청 할 수 없는 결제 상태입니다.";

}
