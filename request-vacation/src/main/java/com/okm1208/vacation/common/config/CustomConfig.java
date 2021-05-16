package com.okm1208.vacation.common.config;

import com.okm1208.vacation.account.repository.AccountRepository;
import com.okm1208.vacation.manager.service.VacationRegisterManager;
import com.okm1208.vacation.manager.service.impl.AnnualLeaveRegisterManager;
import com.okm1208.vacation.manager.service.impl.HalfDayRegisterManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
@Configuration
public class CustomConfig {

    @Autowired
    private AccountRepository accountRepository;

    @Bean
    public VacationRegisterManager halfDayManager(){
        return new HalfDayRegisterManager(accountRepository);
    }
    @Bean
    public VacationRegisterManager annualLeaveManager(){
        return new AnnualLeaveRegisterManager(accountRepository);
    }
}
