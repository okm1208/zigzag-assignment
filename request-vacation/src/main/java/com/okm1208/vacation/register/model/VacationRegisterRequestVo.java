package com.okm1208.vacation.register.model;

import com.okm1208.vacation.common.exception.BadRequestException;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.authentication.BadCredentialsException;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
@ToString
@Data
public class VacationRegisterRequestVo {
    @NotNull
    private LocalDate startDt;
    @NotNull
    private LocalDate endDt;
    private int useDays;
    private String comment;

    public boolean isValidRequestDate(){
        if(startDt != null && endDt != null){
            if(startDt.equals(endDt) ||  startDt.isBefore(endDt)){
                return true;
            }
        }
        return false;
    }

}
