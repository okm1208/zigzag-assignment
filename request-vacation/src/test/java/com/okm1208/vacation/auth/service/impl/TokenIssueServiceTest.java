package com.okm1208.vacation.auth.service.impl;

import com.okm1208.vacation.account.repository.AccountRepository;
import com.okm1208.vacation.account.repository.RefreshTokenRepository;
import com.okm1208.vacation.auth.model.TokenIssueVo;
import com.okm1208.vacation.auth.userdetails.AccountUserDetails;
import com.okm1208.vacation.common.entity.Account;
import com.okm1208.vacation.common.entity.RefreshToken;
import com.okm1208.vacation.common.exception.AuthenticationException;
import com.okm1208.vacation.common.exception.AuthorityException;
import com.okm1208.vacation.common.msg.ErrorMessageProperties;
import com.okm1208.vacation.common.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@SpringBootTest(classes = {TokenIssueService.class})
public class TokenIssueServiceTest {

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private TokenIssueService tokenIssueService;

    private String accountId = "admin";

    private UserDetails userDetails;
    private Account mockAccount;
    private RefreshToken refreshToken;

    @BeforeEach
    public void init(){
        //객체 초기화
        this.mockAccount = new Account();
        this.mockAccount.setAccountId(accountId);
        this.refreshToken = new RefreshToken();
        this.mockAccount.setRefreshTokens(Arrays.asList(refreshToken));
        this.mockAccount.setRoles(Arrays.asList(Account.AccountAuthority.ROLE_ADMIN));
        this.userDetails =  new AccountUserDetails(mockAccount);
    }


    @Test
    public void 리프레시_토큰_재사용_테스트(){

        //CASE : 유효한 리프레시 토큰이 있다면 재사용

        refreshToken.setRefreshToken("refresh_token");
        refreshToken.setExpiresDt(LocalDateTime.now().plusMinutes(10));

        given(accountRepository.findByAccountId(eq(accountId))).willReturn(mockAccount);

        TokenIssueVo tokenIssueVo = tokenIssueService.issueRefreshToken(userDetails);
        assertNotNull(tokenIssueVo);
        assertThat(tokenIssueVo.getToken() ,is(refreshToken.getRefreshToken()));

        verify(refreshTokenRepository, never()).save(any());
    }


    @Test
    public void 리프레시_토큰_신규발급_테스트(){

        //CASE1 : 리프레시 토큰이 없는 경우 신규 발급
        mockAccount.setRefreshTokens(null);

        given(accountRepository.findByAccountId(eq(accountId))).willReturn(mockAccount);

        TokenIssueVo tokenIssueVo = tokenIssueService.issueRefreshToken(userDetails);
        assertNotNull(tokenIssueVo);
        assertNotNull(tokenIssueVo.getToken());
        assertTrue(tokenIssueVo.getExpiresDt().isAfter(LocalDateTime.now()));

        verify(refreshTokenRepository, times(1)).save(any());

        //CASE2 : 리프레시 토큰이 있지만 유효 기간이 지난 경우 신규 발급
        refreshToken = new RefreshToken();
        refreshToken.setExpiresDt(LocalDateTime.now().minusMinutes(10));
        refreshToken.setRefreshToken("refresh_token");
        mockAccount.setRefreshTokens(Arrays.asList(refreshToken));

        reset(refreshTokenRepository);

        tokenIssueVo = tokenIssueService.issueRefreshToken(userDetails);
        assertNotNull(tokenIssueVo);
        assertNotNull(tokenIssueVo.getToken());
        assertTrue(tokenIssueVo.getExpiresDt().isAfter(LocalDateTime.now()));

        verify(refreshTokenRepository, times(1)).save(any());
    }

    @Test
    public void 리프레시_토큰_재발급_테스트(){

        // CASE1 : 유효하지 않은 토큰
        AuthenticationException thrown = assertThrows(
                AuthenticationException.class,
                () -> tokenIssueService.issueAccessTokenFromRefreshToken("invalid_refresh_token")
        );
        assertThat(thrown.getMessage(), is(ErrorMessageProperties.INVALID_TOKEN));

        // CASE2 : 유효기간이 만료된 토큰
        String expiresRefreshToken = createRefreshToken(LocalDateTime.now().minusMinutes(10));

        thrown = assertThrows(
                AuthenticationException.class,
                () -> tokenIssueService.issueAccessTokenFromRefreshToken(expiresRefreshToken)
        );
        assertThat(thrown.getMessage(), is(ErrorMessageProperties.EXPIRED_ACCESS_TOKEN));

        // CASE3: 사용자의 권한이 없는 경우
        String validRefreshToken = createRefreshToken(LocalDateTime.now().plusHours(1));

        mockAccount.setRoles(null);
        given(accountRepository.findByAccountId(eq(accountId))).willReturn(mockAccount);

        AuthorityException authoriryThrown = assertThrows(
                AuthorityException.class,
                () -> tokenIssueService.issueAccessTokenFromRefreshToken(validRefreshToken)
        );
        assertThat(authoriryThrown.getMessage(), is(ErrorMessageProperties.EMPTY_AUTHORITIES));

    }

    private String createRefreshToken(LocalDateTime expiresDt){
        return JwtUtils.createRefreshToken(userDetails, expiresDt);
    }
}