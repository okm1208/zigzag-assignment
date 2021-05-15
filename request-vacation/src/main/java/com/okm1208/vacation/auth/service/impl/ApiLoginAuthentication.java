package com.okm1208.vacation.auth.service.impl;

import com.okm1208.vacation.auth.service.LoginAuthentication;
import com.okm1208.vacation.auth.userdetails.AccountUserDetails;
import com.okm1208.vacation.common.exception.CustomBusinessException;
import com.okm1208.vacation.common.msg.ErrorMessageProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
@Slf4j
@Service
public class ApiLoginAuthentication implements LoginAuthentication {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public UserDetails authenticate(String id, String password) throws CustomBusinessException {

        AccountUserDetails userDetails = (AccountUserDetails)userDetailsService.loadUserByUsername(id);

        if(!isMatchPassword(password,userDetails.getPassword())){
            log.warn("password miss match : {}", id);
            throw new BadCredentialsException(ErrorMessageProperties.MISMATCH_PASSWORD);
        }

        if(!userDetails.isEnabled()){
            log.warn("invalid user account try to login : {}",id);
            throw new BadCredentialsException(ErrorMessageProperties.INVALID_ACCOUNT);
        }

        return userDetails;
    }

    private boolean isMatchPassword(String loginPassword, String password){
        return passwordEncoder.matches(loginPassword,password);
    }
}
