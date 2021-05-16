package com.okm1208.vacation.manager.service.impl;

import com.okm1208.vacation.account.repository.AccountRepository;
import com.okm1208.vacation.common.entity.VacationInfo;
import com.okm1208.vacation.common.exception.BadRequestException;
import com.okm1208.vacation.common.msg.ErrorMessageProperties;
import com.okm1208.vacation.manager.model.VacationRegisterDto;
import com.okm1208.vacation.manager.service.VacationManagerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static com.okm1208.vacation.common.enums.VacationType.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class AnnualLeaveManagerValidateTests extends VacationManagerTest {

    @Autowired
    AccountRepository accountRepository;

    AnnualLeaveRegisterManager annualLeaveManager = new AnnualLeaveRegisterManager(accountRepository);

    @Test
    public void 연차_등록_공휴일_유효성검사(){
        // CASE1 : 신청일이 공휴일인 경우
        VacationRegisterDto registerDto =
                makeMockRequestRegisterDto(ANNUAL_LEAVE, LocalDate.parse("2021-01-01"),LocalDate.parse("2021-01-01"),null);
        BadRequestException badRequestThrown = assertThrows(
                BadRequestException.class,
                () ->  annualLeaveManager.validate(registerDto, null)
        );
        assertThat(badRequestThrown.getMessage(), is(ErrorMessageProperties.REGISTER_ERROR_01));

        VacationRegisterDto registerDto1 =
                makeMockRequestRegisterDto(ANNUAL_LEAVE, LocalDate.parse("2021-05-01"),LocalDate.parse("2021-05-02"),null);

        badRequestThrown = assertThrows(
                BadRequestException.class,
                () ->  annualLeaveManager.validate(registerDto1, null)
        );
        assertThat(badRequestThrown.getMessage(), is(ErrorMessageProperties.REGISTER_ERROR_01));
    }
    @Test
    public void 연_등록_중복일_유효성검사(){

        VacationRegisterDto registerDto =
                makeMockRequestRegisterDto(ANNUAL_LEAVE, LocalDate.parse("2021-05-17"),LocalDate.parse("2021-05-18"),null);

        // 이미 등롣된 연차가 있는 경우
        VacationInfo vacationInfo =
                makeMockVacationInfo(
                        Arrays.asList
                                (makeMockVacationHistory(ANNUAL_LEAVE,LocalDate.parse("2021-05-17")),
                                (makeMockVacationHistory(HALF_DAY_LEAVE,LocalDate.parse("2021-05-18"))
                        )),BigDecimal.valueOf(10.0));

        BadRequestException badRequestThrown = assertThrows(
                BadRequestException.class,
                () ->  annualLeaveManager.validate(registerDto, vacationInfo)
        );

        assertThat(badRequestThrown.getMessage(), is(ErrorMessageProperties.REGISTER_ERROR_02));

        //반반차가 등록 되어 있는 경우
        VacationRegisterDto registerDto2 =
                makeMockRequestRegisterDto(ANNUAL_LEAVE, LocalDate.parse("2021-05-17"),LocalDate.parse("2021-05-17"),null);

        VacationInfo vacationInfo2 =
                makeMockVacationInfo(
                        Arrays.asList(makeMockVacationHistory(HALF_AND_HALF_LEAVE,LocalDate.parse("2021-05-17"))),
                        BigDecimal.valueOf(10.0));

        badRequestThrown = assertThrows(
                BadRequestException.class,
                () ->  annualLeaveManager.validate(registerDto2, vacationInfo2)
        );

        assertThat(badRequestThrown.getMessage(), is(ErrorMessageProperties.REGISTER_ERROR_02));
    }

    @Test
    public void 연차_등록_잔여일부족_유효성검사(){

        VacationRegisterDto registerDto =
                makeMockRequestRegisterDto(ANNUAL_LEAVE, LocalDate.parse("2021-05-17"),LocalDate.parse("2021-05-18"),null);

        // 잔여 연차 1
        VacationInfo vacationInfo =
                makeMockVacationInfo(
                        Arrays.asList(makeMockVacationHistory(ANNUAL_LEAVE,LocalDate.parse("2021-01-15")))
                        ,BigDecimal.valueOf(1));

        BadRequestException badRequestThrown = assertThrows(
                BadRequestException.class,
                () ->  annualLeaveManager.validate(registerDto, vacationInfo)
        );
        assertThat(badRequestThrown.getMessage(), is(ErrorMessageProperties.REGISTER_ERROR_03));
    }

    @Test
    public void 성공(){
        VacationRegisterDto registerDto =
                makeMockRequestRegisterDto(ANNUAL_LEAVE, LocalDate.parse("2021-05-17"),LocalDate.parse("2021-05-18"),null);

        VacationInfo vacationInfo =
                makeMockVacationInfo(
                        Arrays.asList(makeMockVacationHistory(ANNUAL_LEAVE,LocalDate.parse("2021-01-15")))
                        ,BigDecimal.valueOf(10.00));
        //연차 2일 등록
        annualLeaveManager.validate(registerDto, vacationInfo);

        VacationRegisterDto registerDto2 =
                makeMockRequestRegisterDto(ANNUAL_LEAVE, LocalDate.parse("2021-06-28"),LocalDate.parse("2021-06-30"),null);

        //연차 3일 등록
        VacationInfo vacationInfo2 =
                makeMockVacationInfo(
                        Arrays.asList(makeMockVacationHistory(ANNUAL_LEAVE,LocalDate.parse("2021-01-15")))
                        ,BigDecimal.valueOf(3.00));
        annualLeaveManager.validate(registerDto2, vacationInfo2);
    }
}