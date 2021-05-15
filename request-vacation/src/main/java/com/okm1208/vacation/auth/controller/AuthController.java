package com.okm1208.vacation.auth.controller;

import com.okm1208.vacation.auth.model.LoginRequestVo;
import com.okm1208.vacation.auth.service.LoginAuthentication;
import com.okm1208.vacation.common.model.CommonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation(value = "로그인 API")
    @PostMapping(value="/login")
    public CommonResponse login(@RequestBody @Valid LoginRequestVo loginRequest){

        return CommonResponse.success();
    }
}

