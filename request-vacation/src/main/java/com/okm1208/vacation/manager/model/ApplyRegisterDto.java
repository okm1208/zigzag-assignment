package com.okm1208.vacation.manager.model;

import com.okm1208.vacation.common.enums.VacationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplyRegisterDto {
    private LocalDate regDt;
    private VacationType vacationType;
    private String comment;

}
