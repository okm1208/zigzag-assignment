package com.okm1208.vacation.manager.repository;

import com.okm1208.vacation.common.entity.VacationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author Nick (okm1208@gmail.com)
 * @created 2021-05-14
 */
public interface VacationInfoRepository extends JpaRepository<VacationInfo, Long> {
    @Query("SELECT b FROM Account a join VacationInfo b ON a.accountNo = b.accountNo WHERE a.accountId = :accountId")
    VacationInfo findOneByAccountId(@Param("accountId") String accountId);
}
