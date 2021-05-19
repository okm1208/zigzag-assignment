package com.okm1208.document.account.repository;

import com.okm1208.document.common.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-17
 */
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountId(String accountId);
}
