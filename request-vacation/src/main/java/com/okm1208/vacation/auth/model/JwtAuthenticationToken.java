package com.okm1208.vacation.auth.model;

import com.okm1208.vacation.auth.userdetails.AccountUserDetails;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private AccountUserDetails userDetails;
    private String token;

    public JwtAuthenticationToken(String token){
        super(null);
        this.token = token;
        this.setAuthenticated(false);
    }

    public JwtAuthenticationToken(AccountUserDetails userDetails,
                                  Collection<? extends GrantedAuthority> authorities ,
                                  String token) {
        super(authorities);
        this.eraseCredentials();
        this.userDetails = userDetails;
        this.token = token;
        super.setAuthenticated(true);
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        if (authenticated) {
            throw new IllegalArgumentException(
                    "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return this.token;
    }

    @Override
    public Object getPrincipal() {
        return this.userDetails;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
    }

    @Override
    public Object getDetails() {
        return this.userDetails;
    }
}
