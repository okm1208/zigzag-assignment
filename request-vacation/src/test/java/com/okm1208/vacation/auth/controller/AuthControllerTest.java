package com.okm1208.vacation.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.okm1208.vacation.account.repository.AccountRepository;
import com.okm1208.vacation.auth.model.LoginRequestVo;
import com.okm1208.vacation.common.entity.Account;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String adminAccountId = "admin";

    private String authLoginUrl = "/auth/login";


    @Test
    public void 로그인_파라미터_유효성_테스트()throws Exception{

        LoginRequestVo request = new LoginRequestVo();
        this.mockMvc.perform(
                post(authLoginUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        request.setId(adminAccountId);

        this.mockMvc.perform(
                post(authLoginUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))

                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void 존재하지_않은_사용자_테스트()throws Exception{

        LoginRequestVo request = makeDefaultLoginRequestVo(adminAccountId, "admin" );
        given(accountRepository.findByAccountId(eq(adminAccountId))).willReturn(null);

        this.mockMvc.perform(
                post(authLoginUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void 패스워드_불일치및_유효하지않은_상태_테스트()throws Exception{
        LoginRequestVo request =makeDefaultLoginRequestVo(adminAccountId,"invalid_password");
        Account passwordInvalidAccount =  makeMockAccount(adminAccountId , "invalid_password" , true);

        given(accountRepository.findByAccountId(eq(adminAccountId))).willReturn(passwordInvalidAccount);

        this.mockMvc.perform(
                post(authLoginUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andDo(print())
                .andExpect(status().isForbidden());

        Account passiveAccount =  makeMockAccount(adminAccountId , "admin" , false);
        given(accountRepository.findByAccountId(eq(adminAccountId))).willReturn(passiveAccount);

        this.mockMvc.perform(
                post(authLoginUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    public void 로그인_성공()throws Exception{

        String validEncodedPassword = passwordEncoder.encode("admin");

        LoginRequestVo request =makeDefaultLoginRequestVo(adminAccountId,"admin");
        Account validAccount =  makeMockAccount(adminAccountId , validEncodedPassword , true);

        given(accountRepository.findByAccountId(eq(adminAccountId))).willReturn(validAccount);

        this.mockMvc.perform(
                post(authLoginUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", is(not(empty()))))
                .andExpect(jsonPath("$.data.accessToken", is(not(empty()))))
                .andExpect(jsonPath("$.data.refreshToken", is(not(empty()))));

    }

    private Account makeMockAccount(String accountId,String password , boolean active){
        Account account = new Account();
        account.setAccountId(accountId);
        account.setPassword(password);
        account.setActive(active);
        account.setRoles(Arrays.asList(Account.AccountAuthority.ROLE_ADMIN));
        return account;
    }
    private LoginRequestVo makeDefaultLoginRequestVo(String accountId, String password){
        LoginRequestVo request = new LoginRequestVo();
        request.setId(accountId);
        request.setPassword(password);

        return request;
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}