package com.okm1208.vacation.register.repository;

import com.okm1208.vacation.common.entity.VacationInfo;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Nick (okm1208@gmail.com)
 * @created 2021-05-14
 */
public interface VacationInfoRepository extends JpaRepository<VacationInfo, Long> {
}
