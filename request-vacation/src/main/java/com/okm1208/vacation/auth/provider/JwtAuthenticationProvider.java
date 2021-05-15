package com.okm1208.vacation.auth.provider;

import com.okm1208.vacation.auth.model.JwtAuthenticationToken;
import com.okm1208.vacation.auth.userdetails.AccountUserDetails;
import com.okm1208.vacation.common.entity.Account;
import com.okm1208.vacation.common.exception.AuthorityException;
import com.okm1208.vacation.common.msg.ErrorMessageProperties;
import com.okm1208.vacation.common.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.util.CollectionUtils;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
@Slf4j
public class JwtAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String accessToken = (String) authentication.getCredentials();
        Account account = JwtUtils.parseAccount(accessToken);

        if(account == null){
            throw new BadCredentialsException(ErrorMessageProperties.INVALID_ACCOUNT);
        }
        if(CollectionUtils.isEmpty(account.getRoles())){
            log.warn("token provider role is empty : {}",account.getAccountId());
            throw AuthorityException.of(ErrorMessageProperties.EMPTY_AUTHORITIES);
        }
        AccountUserDetails customUserDetails = new AccountUserDetails(account);
        return new JwtAuthenticationToken(customUserDetails, customUserDetails.getAuthorities(), accessToken);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
