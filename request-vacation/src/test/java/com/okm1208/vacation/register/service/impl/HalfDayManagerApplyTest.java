package com.okm1208.vacation.register.service.impl;

import com.okm1208.vacation.account.repository.AccountRepository;
import com.okm1208.vacation.common.entity.Account;
import com.okm1208.vacation.common.entity.VacationHistory;
import com.okm1208.vacation.common.entity.VacationInfo;
import com.okm1208.vacation.common.enums.VacationType;
import com.okm1208.vacation.common.exception.BadRequestException;
import com.okm1208.vacation.common.msg.ErrorMessageProperties;
import com.okm1208.vacation.register.model.VacationRegisterDto;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.okm1208.vacation.common.enums.VacationType.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class HalfDayManagerApplyTest {

    @Autowired
    AccountRepository accountRepository;

    HalfDayManager halfDayManager = new HalfDayManager(accountRepository);

    @Test
    public void 반차_등록_테스트(){
        Account account = Account.builder()
                .active(true)
                .status(Account.AccountStatus.ACTIVE)
                .password("test")
                .accountId("nick")
                .build();
        VacationInfo vacationInfo = VacationInfo.builder()
                .remainingDays(BigDecimal.valueOf(15.0))
                .useDays(BigDecimal.valueOf(0))
                .occursDays(BigDecimal.valueOf(15.0))
                .account(account)
                .vacationHistoryList(new ArrayList<>())
                .build();

        account.setVacationInfo(vacationInfo);
        accountRepository.save(account);

        //반차 등록
        VacationRegisterDto registerDto = makeMockRegisterDto(HALF_DAY_LEAVE, LocalDate.parse("2021-01-05"));
        halfDayManager.apply(registerDto, account.getVacationInfo());

        assertNotNull(account.getVacationInfo().getVacationHistoryList());
        assertThat(account.getVacationInfo().getVacationHistoryList().size(), is(1));
        VacationHistory fistHistory=
                account.getVacationInfo().getVacationHistoryList().stream().filter(v->v.getHistoryNo()==1).findFirst().get();

        assertThat(fistHistory.getHistoryNo(), is(Long.valueOf(1)));
        assertThat(fistHistory.getVacationType(), is(HALF_DAY_LEAVE));
        assertThat(account.getVacationInfo().getUseDays(), Matchers.comparesEqualTo(BigDecimal.valueOf(0.5)));
        assertThat(account.getVacationInfo().getRemainingDays(), Matchers.comparesEqualTo(BigDecimal.valueOf(14.5)));


        //반반차 등록
        registerDto = makeMockRegisterDto(HALF_AND_HALF_LEAVE, LocalDate.parse("2021-01-05"));
        halfDayManager.apply(registerDto, account.getVacationInfo());

        assertNotNull(account.getVacationInfo().getVacationHistoryList());
        assertThat(account.getVacationInfo().getVacationHistoryList().size(), is(2));
        VacationHistory secondHistory=
                account.getVacationInfo().getVacationHistoryList().stream().filter(v->v.getHistoryNo()==2).findFirst().get();
        assertThat(secondHistory.getHistoryNo(), is(Long.valueOf(2)));
        assertThat(secondHistory.getVacationType(), is(HALF_AND_HALF_LEAVE));
        assertThat(account.getVacationInfo().getUseDays(), Matchers.comparesEqualTo(BigDecimal.valueOf(0.75)));
        assertThat(account.getVacationInfo().getRemainingDays(), Matchers.comparesEqualTo(BigDecimal.valueOf(14.25)));

        //반반차 등록
        registerDto = makeMockRegisterDto(HALF_AND_HALF_LEAVE, LocalDate.parse("2021-01-05"));
        halfDayManager.apply(registerDto, account.getVacationInfo());

        assertNotNull(account.getVacationInfo().getVacationHistoryList());
        assertThat(account.getVacationInfo().getVacationHistoryList().size(), is(3));
        VacationHistory thirdHistory=
                account.getVacationInfo().getVacationHistoryList().stream().filter(v->v.getHistoryNo()==3).findFirst().get();
        assertThat(thirdHistory.getHistoryNo(), is(Long.valueOf(3)));
        assertThat(thirdHistory.getVacationType(), is(HALF_AND_HALF_LEAVE));
        assertThat(account.getVacationInfo().getUseDays(), Matchers.comparesEqualTo(BigDecimal.valueOf(1.0)));
        assertThat(account.getVacationInfo().getRemainingDays(), Matchers.comparesEqualTo(BigDecimal.valueOf(14.0)));


    }

    private VacationRegisterDto makeMockRegisterDto(VacationType vacationType, LocalDate startDt){
        return VacationRegisterDto.builder()
                .vacationType(vacationType)
                .startDt(startDt)
                .build();
    }
}