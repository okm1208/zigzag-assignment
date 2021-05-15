package com.okm1208.vacation.common.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */

@Table(name = "refresh_token")
@Entity
@Data
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refreshTokenId;

    @Column(length = 30, nullable = false)
    private String accountId;
    @Column(columnDefinition = "TEXT", nullable = false)
    private String refreshToken;
    @Column(nullable = false)
    private LocalDateTime expiresDt;

}
