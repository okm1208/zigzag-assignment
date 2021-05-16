package com.okm1208.vacation.auth.service.impl;

import com.okm1208.vacation.account.repository.AccountRepository;
import com.okm1208.vacation.auth.service.LoginAuthentication;
import com.okm1208.vacation.auth.userdetails.AccountDetailsService;
import com.okm1208.vacation.common.entity.Account;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Import(value = {
        ApiLoginAuthentication.class,
        AccountDetailsService.class,
        BCryptPasswordEncoder.class} )
public class ApiLoginAuthenticationTests {

    @Autowired
    private LoginAuthentication loginAuthentication;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static String TEST_ID = "nick";
    private static String TEST_PASSWORD = "test_password";

    private static String NOTFOUND_ID = "notfound_nick";
    private static String INVALID_TEST_PASSWORD = "invalid_test_password";

    private Account account;
    @BeforeEach
    public void 테스트계정_초기화(){
        this.account = Account.builder()
                .accountId(TEST_ID)
                .password(passwordEncoder.encode(TEST_PASSWORD))
                .active(true)
                .status(Account.AccountStatus.ACTIVE)
                .build();

        accountRepository.save(account);
    }

    @Test
    public void 인증_성공_테스트(){
        UserDetails userDetails = loginAuthentication.authenticate(TEST_ID,TEST_PASSWORD);
        assertNotNull(userDetails);
        assertThat(userDetails.getUsername(), is(TEST_ID));
        assertTrue(userDetails.isEnabled());
    }

    @Test
    public void 인증_실패_테스트(){
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            loginAuthentication.authenticate(NOTFOUND_ID, TEST_PASSWORD);
        });

        Assertions.assertThrows(BadCredentialsException.class, ()->{
           loginAuthentication.authenticate(TEST_ID, INVALID_TEST_PASSWORD);
        });

        this.account.setActive(false);

        Assertions.assertThrows(BadCredentialsException.class, ()->{
            loginAuthentication.authenticate(TEST_ID, TEST_PASSWORD);
        });

    }

}