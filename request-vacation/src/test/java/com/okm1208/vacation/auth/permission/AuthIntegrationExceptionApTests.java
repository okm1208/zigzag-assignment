package com.okm1208.vacation.auth.permission;

import com.okm1208.vacation.auth.userdetails.AccountUserDetails;
import com.okm1208.vacation.common.entity.Account;
import com.okm1208.vacation.common.entity.RefreshToken;
import com.okm1208.vacation.common.model.ErrorResponseEntity;
import com.okm1208.vacation.common.msg.ErrorMessageProperties;
import com.okm1208.vacation.common.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthIntegrationExceptionApTests {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private String getVacationRegisterApiUrl;

    private String accountId = "admin";

    private UserDetails userDetails;
    private Account mockAccount;
    private RefreshToken refreshToken;

    @BeforeEach
    public void init(){
        this.getVacationRegisterApiUrl =  "http://localhost:" + port + "/vacation/register";
        //mock 계정 초기화
        this.mockAccount = new Account();
        this.mockAccount.setAccountId(accountId);
        this.refreshToken = new RefreshToken();
        this.mockAccount.setRefreshTokens(Arrays.asList(refreshToken));
        this.mockAccount.setRoles(Arrays.asList(Account.AccountAuthority.ROLE_ADMIN));
        this.userDetails =  new AccountUserDetails(mockAccount);
    }
    @Test
    public void 토큰_ISNULL_필터_테스트()  {
        ResponseEntity<ErrorResponseEntity> response = testRestTemplate.
                getForEntity(UriComponentsBuilder.fromHttpUrl(getVacationRegisterApiUrl).toUriString(),
                        ErrorResponseEntity.class);
        assertThat(response.getStatusCode(), is(HttpStatus.FORBIDDEN));
        assertThat(response.getBody().getType(),is("NotAuthorized"));
        assertThat(response.getBody().getMessage(),is(ErrorMessageProperties.REQUIRED_TOKEN));
    }

    @Test
    public void 유효하지_않은_토큰_테스트 (){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "invalid_token");

        ResponseEntity<ErrorResponseEntity> response = testRestTemplate.
                exchange(UriComponentsBuilder.fromHttpUrl(getVacationRegisterApiUrl).toUriString(),
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        ErrorResponseEntity.class);
        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
        assertThat(response.getBody().getType(),is("NotAuthenticated"));
        assertThat(response.getBody().getMessage(),is(ErrorMessageProperties.INVALID_TOKEN));
    }

    @Test
    public void 만료된_토큰_테스트(){
        String accessToken = JwtUtils.createAccessToken(userDetails, LocalDateTime.now().minusHours(1));
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", accessToken);

        ResponseEntity<ErrorResponseEntity> response = testRestTemplate.
                exchange(UriComponentsBuilder.fromHttpUrl(getVacationRegisterApiUrl).toUriString(),
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        ErrorResponseEntity.class);
        assertThat(response.getStatusCode(), is(HttpStatus.UNAUTHORIZED));
        assertThat(response.getBody().getType(),is("NotAuthenticated"));
        assertThat(response.getBody().getMessage(),is(ErrorMessageProperties.EXPIRED_ACCESS_TOKEN));
    }
}
