package com.okm1208.vacation.auth.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestVo {
    @NotEmpty
    private String id;
    @NotEmpty
    private String password;
}
