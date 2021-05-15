package com.okm1208.vacation.auth.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
@Data
public class LoginRequestVo {
    @NotEmpty
    private String id;
    @NotEmpty
    private String password;
}
