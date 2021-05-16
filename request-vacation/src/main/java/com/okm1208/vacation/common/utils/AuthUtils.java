package com.okm1208.vacation.common.utils;

import com.okm1208.vacation.auth.model.JwtAuthenticationToken;
import com.okm1208.vacation.common.exception.AuthorityException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
public class AuthUtils {

    public static String getAccountIdFromSecurityContext(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null){
            throw new AuthorityException();
        }

        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken)authentication;
        return jwtAuthenticationToken.getId();
    }

}
