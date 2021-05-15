package com.okm1208.vacation.repository;

import com.okm1208.vacation.account.repository.AccountRepository;
import com.okm1208.vacation.common.entity.Account;
import com.okm1208.vacation.common.entity.VacationHistory;
import com.okm1208.vacation.common.entity.VacationInfo;
import com.okm1208.vacation.common.entity.pk.VacationHistoryPk;
import com.okm1208.vacation.common.enums.VacationType;
import com.okm1208.vacation.register.repository.VacationHistoryRepository;
import com.okm1208.vacation.register.repository.VacationInfoRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Nick (okm1208@gmail.com)
 * @created 2021-05-14
 */

@DataJpaTest
@Slf4j
@Import(value = {BCryptPasswordEncoder.class})
public class RepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VacationInfoRepository vacationInfoRepository;

    @Autowired
    private VacationHistoryRepository vacationHistoryRepository;

    @Test
    public void 계정_생성_및_조회_테스트(){
        Account account = makeBaseAccount();
        accountRepository.save(account);

        Account retrieveAccount = accountRepository.findById(account.getAccountNo()).orElseGet(null);
        assertNotNull(retrieveAccount);
        assertThat(retrieveAccount.isActive(),is(true) );
        assertThat(passwordEncoder.matches("nick_password",account.getPassword()),is(true));
        assertThat(retrieveAccount.getStatus().name() , is("ACTIVE"));
    }

    @Test
    public void 계정_중복_등록_테스트(){
        Account account = makeBaseAccount();
        accountRepository.save(account);
        final Account dupAccount = Account.builder()
                .accountId("nick_test")
                .status(Account.AccountStatus.ACTIVE)
                .password(passwordEncoder.encode("nick_password"))
                .build();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            accountRepository.save(dupAccount);
        });
    }

    @Test
    public void 휴가_정보초기화_및_조회_테스트(){
        Account account = makeBaseAccount();
        accountRepository.save(account);

        VacationInfo vacationInfo = VacationInfo.
                builder()
                .account(account)
                .occursDays(BigDecimal.valueOf(15.0))
                .remainingDays(BigDecimal.valueOf(0))
                .useDays(BigDecimal.valueOf(0))
                .build();

        vacationInfoRepository.save(vacationInfo);
        VacationInfo retrieveVacationInfo =
                vacationInfoRepository.findById(vacationInfo.getAccountNo()).orElseGet(null);
        assertNotNull(retrieveVacationInfo);
        assertThat(vacationInfo.getAccountNo(),is(account.getAccountNo()));
        assertThat(vacationInfo.getOccursDays(),is(BigDecimal.valueOf(15.0)));
        assertThat(vacationInfo.getRemainingDays(),is(BigDecimal.valueOf(0)));
        assertThat(vacationInfo.getUseDays(), is(BigDecimal.valueOf(0)));

        final VacationInfo dupVacationInfo =  VacationInfo.
                builder()
                .account(account)
                .occursDays(BigDecimal.valueOf(15.0))
                .remainingDays(BigDecimal.valueOf(0))
                .useDays(BigDecimal.valueOf(0))
                .build();
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            vacationInfoRepository.save(dupVacationInfo);
        });
    }

    @Test
    public void 휴가_연차사용_생성_및_조회테스트(){
        Account account = makeBaseAccount();
        accountRepository.save(account);

        VacationInfo vacationInfo = VacationInfo.
                builder()
                .account(account)
                .occursDays(BigDecimal.valueOf(15.0))
                .remainingDays(BigDecimal.valueOf(0))
                .useDays(BigDecimal.valueOf(0))
                .build();

        vacationInfoRepository.save(vacationInfo);


        VacationHistory annualLeaveHistory = VacationHistory.
                builder()
                .accountNo(account.getAccountNo())
                .historyNo(Long.valueOf(1))
                .regDt(LocalDateTime.now())
                .vacationType(VacationType.ANNUAL_LEAVE)
                .build();

        vacationHistoryRepository.save(annualLeaveHistory);
        VacationHistoryPk pk = new VacationHistoryPk();
        pk.setAccountNo(account.getAccountNo());
        pk.setHistoryNo(annualLeaveHistory.getHistoryNo());

        VacationHistory retrieveVacationHistory =
                vacationHistoryRepository.findById(pk).orElseGet(null);
        assertNotNull(retrieveVacationHistory);
    }

    public Account makeBaseAccount(){
        return Account.builder()
                .accountId("nick_test")
                .status(Account.AccountStatus.ACTIVE)
                .password(passwordEncoder.encode("nick_password"))
                .active(true)
                .build();

    }
}
