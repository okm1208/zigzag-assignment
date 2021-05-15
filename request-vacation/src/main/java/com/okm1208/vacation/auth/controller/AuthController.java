package com.okm1208.vacation.auth.controller;

import com.okm1208.vacation.auth.model.LoginRequestVo;
import com.okm1208.vacation.auth.model.LoginResponseVo;
import com.okm1208.vacation.auth.model.TokenIssueVo;
import com.okm1208.vacation.auth.model.TokenReIssueResponseVo;
import com.okm1208.vacation.auth.service.LoginAuthentication;
import com.okm1208.vacation.auth.service.LogoutService;
import com.okm1208.vacation.auth.service.impl.TokenIssueService;
import com.okm1208.vacation.common.model.CommonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */

@RestController
@RequestMapping("/auth")
@Api(tags = "인증 APIs")
public class AuthController {
    @Autowired
    private LoginAuthentication loginAuthentication;

    @Autowired
    private TokenIssueService tokenIssueService;

    @Autowired
    private LogoutService logoutService;


    @ApiOperation(value = "로그인 API")
    @PostMapping(value="/login")
    public CommonResponse<LoginResponseVo> login(@RequestBody @Valid LoginRequestVo loginRequest){

        UserDetails userDetails = loginAuthentication.authenticate(loginRequest.getId(), loginRequest.getPassword());

        TokenIssueVo refreshToken = tokenIssueService.issueRefreshToken(userDetails);
        TokenIssueVo accessToken = tokenIssueService.issueAccessToken(userDetails);

        return CommonResponse.success(new LoginResponseVo(accessToken.getToken()
                , refreshToken.getToken()));
    }

    @ApiOperation(value = "AccessToken 재발급 API")
    @PostMapping(value = "/token/reissue")
    public CommonResponse<TokenReIssueResponseVo> reissueAccessToken(
            @RequestHeader(name = "Authorization") String refreshToken){

        TokenIssueVo reissueToken = tokenIssueService.issueAccessTokenFromRefreshToken(refreshToken);

        return CommonResponse.success(new TokenReIssueResponseVo(reissueToken.getToken()));
    }

    @ApiOperation(value = "로그아웃 API")
    @PostMapping(value="/logout")
    public CommonResponse logout(
            @RequestHeader(name ="Authorization") String accessToken){

        logoutService.logout(accessToken);

        return CommonResponse.success();
    }


}

