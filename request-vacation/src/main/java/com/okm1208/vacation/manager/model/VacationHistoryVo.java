package com.okm1208.vacation.manager.model;

import com.okm1208.vacation.common.enums.VacationType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-16
 */

@Data
@Builder
public class VacationHistoryVo {
    private Long no;
    private String vacationType;
    private String comment;
    private LocalDate regDt;

}
