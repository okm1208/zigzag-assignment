package com.okm1208.vacation.manager.service.impl;

import com.okm1208.vacation.account.repository.AccountRepository;
import com.okm1208.vacation.common.entity.Account;
import com.okm1208.vacation.common.entity.VacationHistory;
import com.okm1208.vacation.common.entity.VacationInfo;
import com.okm1208.vacation.common.enums.VacationType;
import com.okm1208.vacation.common.exception.BadRequestException;
import com.okm1208.vacation.common.msg.ErrorMessageProperties;
import com.okm1208.vacation.manager.model.ApplyRegisterDto;
import com.okm1208.vacation.manager.model.VacationRegisterDto;
import com.okm1208.vacation.manager.service.VacationManagerTest;
import com.okm1208.vacation.manager.service.VacationRegisterManager;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static com.okm1208.vacation.common.enums.VacationType.ANNUAL_LEAVE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CancelManagerTests extends VacationManagerTest {

    @Autowired
    VacationCancelManager cancelManager;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    VacationRegisterManager annualLeaveManager;

    @Autowired
    VacationRegisterManager halfDayManager;

    @Test
    @Transactional
    public void 연차_취소_테스트(){
        //연차 취소하기
        Account account =  makeMockAccount("nick" , "test");
        VacationInfo vacationInfo = makeMockVacationInfo(account, BigDecimal.valueOf(15.0));

        account.setVacationInfo(vacationInfo);
        accountRepository.save(account);

        //총 등록된 휴가 3
        VacationRegisterDto vacationRegisterDto =
                makeMockRequestRegisterDto(VacationType.ANNUAL_LEAVE,
                        LocalDate.parse("2021-05-14"),
                        LocalDate.parse("2021-05-18"),
                        account.getAccountId());

        annualLeaveManager.register(vacationRegisterDto);

        //flush
        accountRepository.save(account);

        LocalDate cancelReqDt = LocalDate.parse("2021-05-17");
        //아직 미사용 휴가
        VacationHistory cancelHistory = vacationInfo.
                getVacationHistoryList()
                .stream()
                .filter(v->v.getRegDt().equals(LocalDate.parse("2021-05-18")))
                .findFirst()
                .orElse(null);


        VacationHistory successCancelHistory = cancelManager.cancel(account.getAccountId(),
                cancelHistory.getHistoryNo()
                ,cancelReqDt);

        assertThat(cancelHistory.getHistoryNo(), is(successCancelHistory.getHistoryNo()));
        assertThat(vacationInfo.getVacationHistoryList().size(), is(2));

        //환수 확인
        assertThat(vacationInfo.getRemainingDays(),Matchers.comparesEqualTo(BigDecimal.valueOf(13.00)));
        assertThat(vacationInfo.getUseDays(),Matchers.comparesEqualTo(BigDecimal.valueOf(2.00)));


        VacationHistory usedHistory = vacationInfo.
                getVacationHistoryList()
                .stream()
                .filter(v->v.getRegDt().equals(LocalDate.parse("2021-05-14")))
                .findFirst()
                .orElse(null);

        BadRequestException badRequestThrown = assertThrows(
                BadRequestException.class,
                () ->  cancelManager.cancel(account.getAccountId(),
                        usedHistory.getHistoryNo()
                        ,cancelReqDt)
        );
        assertThat(badRequestThrown.getMessage(), is(ErrorMessageProperties.CANCEL_ERROR_01));
    }
}