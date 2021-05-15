package com.okm1208.vacation.auth.service;

import com.okm1208.vacation.common.exception.CustomBusinessException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
public interface LoginAuthentication {
    UserDetails authenticate(String id, String password) throws CustomBusinessException;
}
