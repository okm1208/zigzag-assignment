package com.okm1208.vacation.manager.repository;

import com.okm1208.vacation.common.entity.VacationHistory;
import com.okm1208.vacation.common.entity.pk.VacationHistoryPk;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Nick (okm1208@gmail.com)
 * @created 2021-05-14
 */
public interface VacationHistoryRepository extends JpaRepository<VacationHistory, VacationHistoryPk> {
}
