package com.okm1208.vacation.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseVo {
    private String accessToken;
    private String refreshToken;
}
