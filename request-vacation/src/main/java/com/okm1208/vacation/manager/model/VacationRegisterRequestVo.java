package com.okm1208.vacation.manager.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.okm1208.vacation.common.enums.VacationType;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
@Data
public class VacationRegisterRequestVo {
    @NotNull
    private LocalDate startDt;
    private LocalDate endDt;

    @NotNull
    private VacationType vacationType;

    private String comment;

    @JsonIgnore
    public boolean isValidRequest(){
        if(VacationType.ANNUAL_LEAVE.equals(vacationType)){
            if(startDt != null && endDt != null){
                if(startDt.equals(endDt) ||  startDt.isBefore(endDt)){
                    return true;
                }
            }
        }else{
            return true;
        }
        return false;
    }

}
