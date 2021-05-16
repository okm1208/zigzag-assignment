package com.okm1208.vacation.manager.service.impl;

import com.okm1208.vacation.account.repository.AccountRepository;
import com.okm1208.vacation.common.entity.Account;
import com.okm1208.vacation.common.entity.VacationHistory;
import com.okm1208.vacation.common.entity.VacationInfo;
import com.okm1208.vacation.manager.model.ApplyRegisterDto;
import com.okm1208.vacation.manager.service.VacationManagerTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static com.okm1208.vacation.common.enums.VacationType.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class HalfDayManagerApplyTests extends VacationManagerTest {

    @Autowired
    AccountRepository accountRepository;

    HalfDayRegisterManager halfDayManager = new HalfDayRegisterManager(accountRepository);

    @Test
    public void 반차_등록_테스트(){
        Account account = makeMockAccount("nick", "test");
        VacationInfo vacationInfo = makeMockVacationInfo(account, BigDecimal.valueOf(15.0));

        account.setVacationInfo(vacationInfo);
        accountRepository.save(account);

        //반차 등록
        ApplyRegisterDto applyRegisterDto = makeMockApplyRegisterDto(HALF_DAY_LEAVE, LocalDate.parse("2021-01-05"));
        halfDayManager.apply(Arrays.asList(applyRegisterDto), account.getVacationInfo());

        assertNotNull(account.getVacationInfo().getVacationHistoryList());
        assertThat(account.getVacationInfo().getVacationHistoryList().size(), is(1));
        assertThat(account.getVacationInfo().getUseDays(), Matchers.comparesEqualTo(BigDecimal.valueOf(0.5)));
        assertThat(account.getVacationInfo().getRemainingDays(), Matchers.comparesEqualTo(BigDecimal.valueOf(14.5)));


        //반반차 등록
        applyRegisterDto = makeMockApplyRegisterDto(HALF_AND_HALF_LEAVE, LocalDate.parse("2021-01-05"));
        halfDayManager.apply(Arrays.asList(applyRegisterDto), account.getVacationInfo());

        assertNotNull(account.getVacationInfo().getVacationHistoryList());
        assertThat(account.getVacationInfo().getVacationHistoryList().size(), is(2));
        assertThat(account.getVacationInfo().getUseDays(), Matchers.comparesEqualTo(BigDecimal.valueOf(0.75)));
        assertThat(account.getVacationInfo().getRemainingDays(), Matchers.comparesEqualTo(BigDecimal.valueOf(14.25)));

        //반반차 등록
        applyRegisterDto = makeMockApplyRegisterDto(HALF_AND_HALF_LEAVE, LocalDate.parse("2021-01-05"));
        halfDayManager.apply(Arrays.asList(applyRegisterDto), account.getVacationInfo());

        assertNotNull(account.getVacationInfo().getVacationHistoryList());
        assertThat(account.getVacationInfo().getVacationHistoryList().size(), is(3));
        assertThat(account.getVacationInfo().getUseDays(), Matchers.comparesEqualTo(BigDecimal.valueOf(1.0)));
        assertThat(account.getVacationInfo().getRemainingDays(), Matchers.comparesEqualTo(BigDecimal.valueOf(14.0)));
    }
}