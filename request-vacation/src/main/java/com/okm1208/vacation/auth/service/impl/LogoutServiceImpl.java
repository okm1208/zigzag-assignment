package com.okm1208.vacation.auth.service.impl;

import com.okm1208.vacation.auth.service.LogoutService;
import com.okm1208.vacation.common.entity.Account;
import com.okm1208.vacation.common.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
@Service
public class LogoutServiceImpl implements LogoutService {


    @Autowired
    private TokenIssueService tokenIssueService;

    @Override
    @Transactional
    public void logout(String accessToken) {
        Account account = JwtUtils.parseAccount(accessToken);
        tokenIssueService.expireAllRefreshTokenWhereAccountId(account.getAccountId());
    }
}
