package com.okm1208.vacation.manager.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-16
 */
@Data
public class VacationSearchResponseVo {
    private BigDecimal occursDays;
    private BigDecimal useDays;
    private BigDecimal remainingDays;

    private List<VacationHistoryVo> history;
}
