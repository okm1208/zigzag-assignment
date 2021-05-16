package com.okm1208.vacation.register.service.impl;

import com.okm1208.vacation.account.repository.AccountRepository;
import com.okm1208.vacation.common.entity.VacationInfo;
import com.okm1208.vacation.common.enums.VacationType;
import com.okm1208.vacation.common.utils.HolidayChecker;
import com.okm1208.vacation.register.model.VacationRegisterDto;
import com.okm1208.vacation.register.service.VacationManager;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */

public class AnnualLeaveManager extends VacationManager {

    public AnnualLeaveManager(AccountRepository accountRepository){
        super(accountRepository);
    }

    @Override
    @Transactional
    protected void apply(VacationRegisterDto registerDto, VacationInfo vacationInfo) {

    }

    @Override
    protected void validate(VacationRegisterDto registerDto, VacationInfo vacationInfo){
    }

    @Override
    protected VacationType getType() {
        return VacationType.ANNUAL_LEAVE;
    }
}
