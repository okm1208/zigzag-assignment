package com.okm1208.vacation.auth.userdetails;

import com.okm1208.vacation.common.entity.Account;
import com.okm1208.vacation.common.exception.AuthorityException;
import com.okm1208.vacation.common.msg.ErrorMessageProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
public class AccountUserDetails implements UserDetails {
    private Account account;

    public AccountUserDetails(Account account){
        if(account == null){
            throw AuthorityException.of(ErrorMessageProperties.ACCOUNT_NOT_FOUND);
        }
        this.account = account;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return account.getRoles();
    }

    @Override
    public String getPassword() {
        return account.getPassword();
    }

    @Override
    public String getUsername() {
        return account.getAccountId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return account.isActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !account.getStatus().equals(Account.AccountStatus.LOCKED);
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return account.isActive();
    }

    @Override
    public boolean isEnabled() {
        return account.isActive();
    }
}
