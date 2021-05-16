package com.okm1208.vacation.register.service.impl;

import com.okm1208.vacation.account.repository.AccountRepository;
import com.okm1208.vacation.common.entity.Account;
import com.okm1208.vacation.common.entity.VacationInfo;
import com.okm1208.vacation.common.enums.VacationType;
import com.okm1208.vacation.common.exception.BadRequestException;
import com.okm1208.vacation.common.msg.ErrorMessageProperties;
import com.okm1208.vacation.register.model.VacationRegisterDto;
import com.okm1208.vacation.register.repository.VacationInfoRepository;
import com.okm1208.vacation.register.service.VacationManager;
import com.okm1208.vacation.register.service.VacationManagerTest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-16
 */

@SpringBootTest
public class VacationRegisterIntegrationsTests extends VacationManagerTest {
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    VacationInfoRepository vacationInfoRepository;

    @Autowired
    VacationManager annualLeaveManager;

    @Autowired
    VacationManager halfDayManager;

    @Test
    @Transactional
    public void 연차_등록_통합_테스트(){
        Account account = makeMockAccount("nick" , "test");
        VacationInfo vacationInfo = makeMockVacationInfo(account, BigDecimal.valueOf(5.0));

        account.setVacationInfo(vacationInfo);
        accountRepository.save(account);


        // 공휴일 포함 연차 신청
        VacationRegisterDto vacationRegisterDto =
                makeMockRequestRegisterDto(VacationType.ANNUAL_LEAVE,
                        LocalDate.parse("2021-05-16"),
                        LocalDate.parse("2021-05-18"),
                        account.getAccountId());

        annualLeaveManager.register(vacationRegisterDto);

        assertNotNull(vacationInfo.getVacationHistoryList());
        assertThat(vacationInfo.getVacationHistoryList().size(), is(2));
        assertThat(vacationInfo.getUseDays(), Matchers.comparesEqualTo(BigDecimal.valueOf(2.0)));
        assertThat(vacationInfo.getRemainingDays(), Matchers.comparesEqualTo(BigDecimal.valueOf(3.0)));

        vacationRegisterDto =
                makeMockRequestRegisterDto(VacationType.ANNUAL_LEAVE,
                        LocalDate.parse("2021-06-07"),
                        LocalDate.parse("2021-06-09"),
                        account.getAccountId());

        annualLeaveManager.register(vacationRegisterDto);

        assertNotNull(vacationInfo.getVacationHistoryList());
        assertThat(vacationInfo.getVacationHistoryList().size(), is(5));
        assertThat(vacationInfo.getUseDays(), Matchers.comparesEqualTo(BigDecimal.valueOf(5.0)));
        assertThat(vacationInfo.getRemainingDays(), Matchers.comparesEqualTo(BigDecimal.ZERO));

        final VacationRegisterDto failedVacationRegisterDto =
                makeMockRequestRegisterDto(VacationType.ANNUAL_LEAVE,
                        LocalDate.parse("2021-07-05"),
                        LocalDate.parse("2021-07-06"),
                        account.getAccountId());

        BadRequestException badRequestThrown = assertThrows(
                BadRequestException.class,
                () ->  annualLeaveManager.register(failedVacationRegisterDto)
        );
        assertThat(badRequestThrown.getMessage(), is(ErrorMessageProperties.REGISTER_ERROR_03));
    }

    @Test
    @Transactional
    public void 연차_반차_통합_테스트() {
        Account account =  makeMockAccount("nick" , "test");
        VacationInfo vacationInfo = makeMockVacationInfo(account, BigDecimal.valueOf(3.0));

        account.setVacationInfo(vacationInfo);
        accountRepository.save(account);


        // 반차 신청
        VacationRegisterDto vacationRegisterDto =
                makeMockRequestRegisterDto(VacationType.HALF_DAY_LEAVE,
                        LocalDate.parse("2021-05-17"),
                        account.getAccountId());

        halfDayManager.register(vacationRegisterDto);

        assertNotNull(vacationInfo.getVacationHistoryList());
        assertThat(vacationInfo.getVacationHistoryList().size(), is(1));
        assertThat(vacationInfo.getUseDays(), Matchers.comparesEqualTo(BigDecimal.valueOf(0.5)));
        assertThat(vacationInfo.getRemainingDays(), Matchers.comparesEqualTo(BigDecimal.valueOf(2.5)));

        // 무리한 연차 신청
        VacationRegisterDto failedVacationRegisterDto =
                makeMockRequestRegisterDto(VacationType.ANNUAL_LEAVE,
                        LocalDate.parse("2021-06-16"),
                        LocalDate.parse("2021-07-01"),
                        account.getAccountId());

        BadRequestException badRequestThrown = assertThrows(
                BadRequestException.class,
                () ->  annualLeaveManager.register(failedVacationRegisterDto)
        );
        assertThat(badRequestThrown.getMessage(), is(ErrorMessageProperties.REGISTER_ERROR_03));

        // 반반차 신청
        VacationRegisterDto vacationRegisterDto2 =
                makeMockRequestRegisterDto(VacationType.HALF_AND_HALF_LEAVE,
                        LocalDate.parse("2021-09-16"),
                        account.getAccountId());

        halfDayManager.register(vacationRegisterDto2);

        assertNotNull(vacationInfo.getVacationHistoryList());
        assertThat(vacationInfo.getVacationHistoryList().size(), is(2));
        assertThat(vacationInfo.getUseDays(), Matchers.comparesEqualTo(BigDecimal.valueOf(0.75)));
        assertThat(vacationInfo.getRemainingDays(), Matchers.comparesEqualTo(BigDecimal.valueOf(2.25)));


        VacationRegisterDto vacationRegisterDto3 =
                makeMockRequestRegisterDto(VacationType.HALF_AND_HALF_LEAVE,
                        LocalDate.parse("2021-09-17"),
                        account.getAccountId());
        halfDayManager.register(vacationRegisterDto3);


        // 나머지 연차 신청
        VacationRegisterDto vacationRegisterDto4 =
                makeMockRequestRegisterDto(VacationType.ANNUAL_LEAVE,
                        LocalDate.parse("2021-07-26"),
                        LocalDate.parse("2021-07-27"),
                        account.getAccountId());

        annualLeaveManager.register(vacationRegisterDto4);
        assertNotNull(vacationInfo.getVacationHistoryList());
        assertThat(vacationInfo.getVacationHistoryList().size(), is(5));
        assertThat(vacationInfo.getUseDays(), Matchers.comparesEqualTo(BigDecimal.valueOf(3.0)));
        assertThat(vacationInfo.getRemainingDays(), Matchers.comparesEqualTo(BigDecimal.ZERO));


    }
}
