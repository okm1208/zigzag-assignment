package com.okm1208.vacation.manager.service;

import com.okm1208.vacation.common.entity.Account;
import com.okm1208.vacation.common.entity.VacationHistory;
import com.okm1208.vacation.common.entity.VacationInfo;
import com.okm1208.vacation.common.enums.VacationType;
import com.okm1208.vacation.manager.model.ApplyRegisterDto;
import com.okm1208.vacation.manager.model.VacationRegisterDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-16
 */
public class VacationManagerTest {
    protected Account makeMockAccount(String id, String password){

        return Account.builder()
                .active(true)
                .status(Account.AccountStatus.ACTIVE)
                .password(password)
                .accountId(id)
                .build();
    }

    protected VacationInfo makeMockVacationInfo(Account account, BigDecimal days){
        return VacationInfo.builder()
                .remainingDays(days)
                .useDays(BigDecimal.ZERO)
                .occursDays(days)
                .account(account)
                .vacationHistoryList(new ArrayList<>())
                .build();
    }
    protected ApplyRegisterDto makeMockApplyRegisterDto(VacationType vacationType, LocalDate regDt){
        return ApplyRegisterDto.builder()
                .vacationType(vacationType)
                .regDt(regDt)
                .build();
    }
    protected VacationRegisterDto makeMockRequestRegisterDto(VacationType vacationType,
                                                             LocalDate startDt,
                                                             LocalDate endDt,
                                                             String accountId){
        return VacationRegisterDto.builder()
                .vacationType(vacationType)
                .startDt(startDt)
                .endDt(endDt)
                .accountId(accountId)
                .build();
    }

    protected VacationRegisterDto makeMockRequestRegisterDto(VacationType vacationType,
                                                             LocalDate startDt,
                                                             String accountId){
        return VacationRegisterDto.builder()
                .vacationType(vacationType)
                .startDt(startDt)
                .accountId(accountId)
                .build();
    }
    protected VacationInfo makeMockVacationInfo(List<VacationHistory> vacationHistoryList , BigDecimal remainingDays){
        return VacationInfo.builder()
                .vacationHistoryList(vacationHistoryList)
                .remainingDays(remainingDays)
                .build();
    }
    protected VacationHistory makeMockVacationHistory(VacationType vacationType, LocalDate regDt){
        return VacationHistory.builder()
                .vacationType(vacationType)
                .regDt(regDt)
                .build();
    }
}
