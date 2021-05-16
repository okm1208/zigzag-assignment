package com.okm1208.vacation.register.model;

import com.okm1208.vacation.common.enums.VacationType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
@Data
@Builder
public class VacationRegisterDto {
    private String accountId;
    private LocalDate startDt;
    private LocalDate endDt;
    private String comment;
    private VacationType vacationType;
}
