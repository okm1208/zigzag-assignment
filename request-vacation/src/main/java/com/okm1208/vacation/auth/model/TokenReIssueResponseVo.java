package com.okm1208.vacation.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
@Data
@AllArgsConstructor
public class TokenReIssueResponseVo {
    String accessToken;
}
