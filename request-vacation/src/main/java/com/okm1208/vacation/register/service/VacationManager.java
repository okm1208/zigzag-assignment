package com.okm1208.vacation.register.service;

import com.okm1208.vacation.account.repository.AccountRepository;
import com.okm1208.vacation.common.entity.Account;
import com.okm1208.vacation.common.entity.VacationInfo;
import com.okm1208.vacation.common.enums.VacationType;
import com.okm1208.vacation.register.model.VacationRegisterDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
@Slf4j
public abstract class VacationManager {
    private AccountRepository accountRepository;

    public VacationManager(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void register(VacationRegisterDto registerDto){
        Account account = accountRepository.findByAccountId(registerDto.getAccountId());

        validate(registerDto,account.getVacationInfo());
        apply(registerDto,account.getVacationInfo());
    }

    abstract protected void validate(VacationRegisterDto registerDto, VacationInfo vacationInfo);
    abstract protected void apply(VacationRegisterDto registerDto, VacationInfo vacationInfo);
    abstract protected VacationType getType();

    public void cancel(){

    }


}
