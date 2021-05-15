package com.okm1208.vacation.account.repository;

import com.okm1208.vacation.common.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    @Modifying
    @Query("update RefreshToken rt set rt.expiresDt = :expiresDt WHERE rt.accountId = :accountId")
    void updateExpiresDatetimeByUserId(@Param("accountId") String accountId, @Param("expiresDt") LocalDateTime expiresDt);
}
