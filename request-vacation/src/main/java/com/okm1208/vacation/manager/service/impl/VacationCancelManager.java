package com.okm1208.vacation.manager.service.impl;

import com.okm1208.vacation.account.repository.AccountRepository;
import com.okm1208.vacation.common.entity.Account;
import com.okm1208.vacation.common.entity.VacationHistory;
import com.okm1208.vacation.common.exception.BadRequestException;
import com.okm1208.vacation.common.exception.DataNotFoundException;
import com.okm1208.vacation.common.msg.ErrorMessageProperties;
import com.okm1208.vacation.manager.repository.VacationHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-16
 */
@Service
public class VacationCancelManager {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private VacationHistoryRepository vacationHistoryRepository;

    @Transactional
    public VacationHistory cancel(String accountId, Long historyNo, LocalDate cancelReqDt){
        Account account = accountRepository.findByAccountId(accountId);

        VacationHistory cancelHistory =
                account.getVacationInfo()
                        .getVacationHistoryList().stream()
                        .filter(v->v.getHistoryNo().equals(historyNo))
                        .findFirst()
                        .orElse(null );

        if(cancelHistory == null){
            throw DataNotFoundException.of(ErrorMessageProperties.EMPTY_DATA);
        }

        if( cancelReqDt.compareTo(cancelHistory.getRegDt()) >= 0  ) {
            throw BadRequestException.of(ErrorMessageProperties.CANCEL_ERROR_01);
        }

//        vacationHistoryRepository.delete(cancelHistory);
        account.getVacationInfo().getVacationHistoryList().remove(cancelHistory);

        BigDecimal userDays = account.getVacationInfo().getUseDays();
        BigDecimal remainingDays = account.getVacationInfo().getRemainingDays();

        account.getVacationInfo().setUseDays(userDays.subtract(cancelHistory.getVacationType().getUseDays()));
        account.getVacationInfo().setRemainingDays(remainingDays.add(cancelHistory.getVacationType().getUseDays()));

        return cancelHistory;
    }
}
