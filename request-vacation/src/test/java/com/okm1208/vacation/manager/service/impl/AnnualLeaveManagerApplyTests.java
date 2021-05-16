package com.okm1208.vacation.manager.service.impl;

import com.okm1208.vacation.account.repository.AccountRepository;
import com.okm1208.vacation.common.entity.Account;
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
import java.util.List;

import static com.okm1208.vacation.common.enums.VacationType.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class AnnualLeaveManagerApplyTests extends VacationManagerTest {

    @Autowired
    AccountRepository accountRepository;

    AnnualLeaveRegisterManager annualLeaveManager = new AnnualLeaveRegisterManager(accountRepository);

    @Test
    public void 연차_등록_테스트(){
        Account account = makeMockAccount("nick","test");
        VacationInfo vacationInfo = makeMockVacationInfo(account, BigDecimal.valueOf(15.0));

        account.setVacationInfo(vacationInfo);
        accountRepository.save(account);

        //연차 등록
        List<ApplyRegisterDto> applyRegisterDtoList =
                Arrays.asList(
                        makeMockApplyRegisterDto(ANNUAL_LEAVE, LocalDate.parse("2021-05-10")),
                        makeMockApplyRegisterDto(ANNUAL_LEAVE, LocalDate.parse("2021-05-11"))
                );
        annualLeaveManager.apply(applyRegisterDtoList, account.getVacationInfo());

        assertNotNull(account.getVacationInfo().getVacationHistoryList());
        assertThat(account.getVacationInfo().getVacationHistoryList().size(), is(2));
        assertThat(account.getVacationInfo().getUseDays(), Matchers.comparesEqualTo(BigDecimal.valueOf(2.0)));
        assertThat(account.getVacationInfo().getRemainingDays(), Matchers.comparesEqualTo(BigDecimal.valueOf(13.0)));

        applyRegisterDtoList =
                Arrays.asList(
                        makeMockApplyRegisterDto(ANNUAL_LEAVE, LocalDate.parse("2021-05-26")),
                        makeMockApplyRegisterDto(ANNUAL_LEAVE, LocalDate.parse("2021-05-27")),
                        makeMockApplyRegisterDto(ANNUAL_LEAVE, LocalDate.parse("2021-05-28"))
                );
        annualLeaveManager.apply(applyRegisterDtoList, account.getVacationInfo());

        assertNotNull(account.getVacationInfo().getVacationHistoryList());
        assertThat(account.getVacationInfo().getVacationHistoryList().size(), is(5));
        assertThat(account.getVacationInfo().getUseDays(), Matchers.comparesEqualTo(BigDecimal.valueOf(5.0)));
        assertThat(account.getVacationInfo().getRemainingDays(), Matchers.comparesEqualTo(BigDecimal.valueOf(10.0)));
    }
}