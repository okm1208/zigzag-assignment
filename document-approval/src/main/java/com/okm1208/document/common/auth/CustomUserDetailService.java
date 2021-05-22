package com.okm1208.document.common.auth;

import com.okm1208.document.account.repository.AccountRepository;
import com.okm1208.document.common.entity.Account;
import com.okm1208.document.common.msg.ErrorMessageProperties;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-22
 */
public class CustomUserDetailService implements UserDetailsService {
    final private AccountRepository accountRepository;

    public CustomUserDetailService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account account = accountRepository.findByAccountId(username)
                .orElseThrow(()->
                        new UsernameNotFoundException(ErrorMessageProperties.ACCOUNT_NOT_FOUND));
        UserDetails userDetails = new User(
                account.getAccountId(),
                account.getPassword(),
                true,
                true,
                true,
                true,
                account.getRoles()
        );
        return userDetails;
    }
}
