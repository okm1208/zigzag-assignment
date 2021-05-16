package com.okm1208.vacation.register.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-16
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VacationRegisterResponseVo {
    private BigDecimal remainingDays;
    private BigDecimal useDays;
    private BigDecimal occursDays;
}
