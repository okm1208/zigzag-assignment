package com.okm1208.vacation.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenIssueVo {
    private String token;
    private LocalDateTime expiresDt;
}
