package com.okm1208.vacation.manager.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-16
 */
@Data
@Builder
public class VacationCancelDto {
    private Long historyNo;
    private String accountId;
    private LocalDate requestDt;
}
