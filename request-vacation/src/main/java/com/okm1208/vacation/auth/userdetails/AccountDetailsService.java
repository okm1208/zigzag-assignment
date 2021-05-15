package com.okm1208.vacation.auth.userdetails;

import com.okm1208.vacation.account.repository.AccountRepository;
import com.okm1208.vacation.common.entity.Account;
import com.okm1208.vacation.common.msg.ErrorMessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */

@Service
public class AccountDetailsService implements UserDetailsService {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account account = accountRepository.findByAccountId(username);
        if(account == null){
            throw new UsernameNotFoundException(ErrorMessageProperties.ACCOUNT_NOT_FOUND);
        }
        return new AccountUserDetails(account);
    }
}
