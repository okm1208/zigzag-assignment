package com.okm1208.vacation.common.config;

import com.okm1208.vacation.account.repository.AccountRepository;
import com.okm1208.vacation.register.service.VacationManager;
import com.okm1208.vacation.register.service.impl.AnnualLeaveManager;
import com.okm1208.vacation.register.service.impl.HalfDayManager;
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
    public VacationManager halfDayManager(){
        return new HalfDayManager(accountRepository);
    }
    @Bean
    public VacationManager annualLeaveManager(){
        return new AnnualLeaveManager(accountRepository);
    }
}
