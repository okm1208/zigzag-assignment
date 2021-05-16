package com.okm1208.vacation.register.service.impl;

import com.okm1208.vacation.account.repository.AccountRepository;
import com.okm1208.vacation.common.entity.VacationHistory;
import com.okm1208.vacation.common.entity.VacationInfo;
import com.okm1208.vacation.common.enums.VacationType;
import com.okm1208.vacation.common.exception.BadRequestException;
import com.okm1208.vacation.common.msg.ErrorMessageProperties;
import com.okm1208.vacation.register.model.VacationRegisterDto;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
public class HalfDayManagerValidateTest {

    @Autowired
    AccountRepository accountRepository;

    HalfDayManager halfDayManager = new HalfDayManager(accountRepository);

    @Test
    public void 반차_등록_공휴일_유효성검사(){

        // CASE1 : 신청일이 공휴일인 경우
        VacationRegisterDto registerDto = makeMockRegisterDto(HALF_DAY_LEAVE, LocalDate.parse("2021-01-01"));

        BadRequestException badRequestThrown = assertThrows(
                BadRequestException.class,
                () ->  halfDayManager.validate(registerDto, null)
        );
        assertThat(badRequestThrown.getMessage(), is(ErrorMessageProperties.REGISTER_ERROR_01));

        VacationRegisterDto registerDto1 = makeMockRegisterDto(HALF_DAY_LEAVE, LocalDate.parse("2021-12-25"));

        badRequestThrown = assertThrows(
                BadRequestException.class,
                () ->  halfDayManager.validate(registerDto1, null)
        );
        assertThat(badRequestThrown.getMessage(), is(ErrorMessageProperties.REGISTER_ERROR_01));
    }
    @Test
    public void 반차_등록_중복일_유효성검사(){

        // CASE 2: 등록일에 이미 다른 휴가등록이 있을경우
        VacationRegisterDto registerDto = makeMockRegisterDto(HALF_DAY_LEAVE, LocalDate.parse("2021-01-05"));

        // 이미 등롣된 연차가 있는 경우
        VacationInfo vacationInfo =
                makeMockVacationInfo(
                        Arrays.asList(makeMockVacationHistory(ANNUAL_LEAVE,LocalDate.parse("2021-01-05")))
                                ,BigDecimal.valueOf(10.0));
        BadRequestException badRequestThrown = assertThrows(
                BadRequestException.class,
                () ->  halfDayManager.validate(registerDto, vacationInfo)
        );
        assertThat(badRequestThrown.getMessage(), is(ErrorMessageProperties.REGISTER_ERROR_02));

        VacationRegisterDto registerDto2 = makeMockRegisterDto(HALF_AND_HALF_LEAVE, LocalDate.parse("2021-01-05"));

        badRequestThrown = assertThrows(
                BadRequestException.class,
                () ->  halfDayManager.validate(registerDto2, vacationInfo)
        );
        assertThat(badRequestThrown.getMessage(), is(ErrorMessageProperties.REGISTER_ERROR_02));

        //반차 + 반반차가 등록되고 , 반차를 요청한 경우

        VacationRegisterDto registerDto3 = makeMockRegisterDto(HALF_DAY_LEAVE, LocalDate.parse("2021-01-05"));
        VacationInfo vacationInfo2 =
                makeMockVacationInfo(
                        Arrays.asList(makeMockVacationHistory(HALF_AND_HALF_LEAVE,LocalDate.parse("2021-01-05")),
                                makeMockVacationHistory(HALF_DAY_LEAVE,LocalDate.parse("2021-01-05")))
                        ,BigDecimal.valueOf(10.0));

        badRequestThrown = assertThrows(
                BadRequestException.class,
                () ->  halfDayManager.validate(registerDto3, vacationInfo2)
        );
        assertThat(badRequestThrown.getMessage(), is(ErrorMessageProperties.REGISTER_ERROR_02));

    }

    @Test
    public void 반차_등록_잔여일부족_유효성검사(){
        // CASE 3 : 잔여 연차가 부족한 경우
        VacationRegisterDto registerDto = makeMockRegisterDto(HALF_DAY_LEAVE, LocalDate.parse("2021-01-05"));

        // 잔여 연차 9
        VacationInfo vacationInfo =
                makeMockVacationInfo(
                        Arrays.asList(makeMockVacationHistory(ANNUAL_LEAVE,LocalDate.parse("2021-01-15")))
                        ,BigDecimal.valueOf(0));

        BadRequestException badRequestThrown = assertThrows(
                BadRequestException.class,
                () ->  halfDayManager.validate(registerDto, vacationInfo)
        );
        assertThat(badRequestThrown.getMessage(), is(ErrorMessageProperties.REGISTER_ERROR_03));
    }

    @Test
    public void 성공(){
        //CASE 1 : 반차 등록중 , 반차 신청

        VacationRegisterDto registerDto = makeMockRegisterDto(HALF_DAY_LEAVE, LocalDate.parse("2021-01-05"));
        VacationInfo vacationInfo =
                makeMockVacationInfo(
                        Arrays.asList(makeMockVacationHistory(HALF_DAY_LEAVE,LocalDate.parse("2021-01-15")))
                        ,BigDecimal.valueOf(10));

        halfDayManager.validate(registerDto, vacationInfo);


        //CASE2 : 반반차 등록중, 반차 신청
        vacationInfo =
                makeMockVacationInfo(
                        Arrays.asList(makeMockVacationHistory(HALF_AND_HALF_LEAVE,LocalDate.parse("2021-01-15")))
                        ,BigDecimal.valueOf(10));
        halfDayManager.validate(registerDto, vacationInfo);

        //CASE3 : 반반차 2개 등록 , 반차 신청
        vacationInfo =
                makeMockVacationInfo(
                        Arrays.asList(
                                makeMockVacationHistory(HALF_AND_HALF_LEAVE,LocalDate.parse("2021-01-15")),
                                makeMockVacationHistory(HALF_AND_HALF_LEAVE,LocalDate.parse("2021-01-15"))
                                )
                        ,BigDecimal.valueOf(10));
        halfDayManager.validate(registerDto, vacationInfo);



    }

    private VacationRegisterDto makeMockRegisterDto(VacationType vacationType, LocalDate startDt){
        return VacationRegisterDto.builder()
                .vacationType(vacationType)
                .startDt(startDt)
                .build();
    }

    private VacationHistory makeMockVacationHistory(VacationType vacationType, LocalDate regDt){
        return VacationHistory.builder()
                .vacationType(vacationType)
                .regDt(regDt)
                .build();
    }
    private VacationInfo makeMockVacationInfo(List<VacationHistory> vacationHistoryList , BigDecimal remainingDays){
        return VacationInfo.builder()
                .vacationHistoryList(vacationHistoryList)
                .remainingDays(remainingDays)
                .build();
    }
}