package com.okm1208.vacation.account.repository;

import com.okm1208.vacation.common.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Nick (okm1208@gmail.com)
 * @created 2021-05-14
 */
public interface AccountRepository extends JpaRepository<Account, Long> {
}
