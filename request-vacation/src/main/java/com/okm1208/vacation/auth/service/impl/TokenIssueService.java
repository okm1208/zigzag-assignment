package com.okm1208.vacation.auth.service.impl;

import com.okm1208.vacation.account.repository.AccountRepository;
import com.okm1208.vacation.auth.repository.RefreshTokenRepository;
import com.okm1208.vacation.auth.model.TokenIssueVo;
import com.okm1208.vacation.auth.userdetails.AccountUserDetails;
import com.okm1208.vacation.common.entity.Account;
import com.okm1208.vacation.common.entity.RefreshToken;
import com.okm1208.vacation.common.exception.AuthorityException;
import com.okm1208.vacation.common.exception.BadRequestException;
import com.okm1208.vacation.common.msg.ErrorMessageProperties;
import com.okm1208.vacation.common.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */

@Slf4j
@Service
public class TokenIssueService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Value("${auth.token.expired-minute.access-token:5}")
    private Integer accessTokenExpiredMin;

    @Value("${auth.token.expired-minute.refresh-token:60}")
    private Integer refreshTokenExpiredMin;

    public TokenIssueVo issueAccessToken(UserDetails userDetails){
        LocalDateTime expiresDt = LocalDateTime.now().plusMinutes(accessTokenExpiredMin);

        return new TokenIssueVo(JwtUtils.createAccessToken(userDetails,expiresDt),expiresDt);
    }

    @Transactional
    public TokenIssueVo issueRefreshToken(UserDetails userDetails){
        String accountId = userDetails.getUsername();
        Account account = accountRepository.findByAccountId(accountId);

        if(account == null){
            log.warn("not found account : {}",accountId);
            throw new UsernameNotFoundException(ErrorMessageProperties.ACCOUNT_NOT_FOUND);
        }

        List<RefreshToken> refreshTokenList = account.getRefreshTokens();

        if(!CollectionUtils.isEmpty(refreshTokenList)){
            //기존 유효한 RefreshToken이 있다면 재사용
            RefreshToken findValidRefreshToken =
                    refreshTokenList.stream()
                    .filter(refreshToken ->
                            LocalDateTime.now().isBefore(refreshToken.getExpiresDt()))
                    .findFirst().orElse(null);
            if(findValidRefreshToken != null){
                log.debug("refresh token recycle : {},{}",accountId, findValidRefreshToken.getRefreshToken());
                return new TokenIssueVo(findValidRefreshToken.getRefreshToken(), findValidRefreshToken.getExpiresDt());
            }
        }

        //Token 재발급
        LocalDateTime expiresDt = LocalDateTime.now().plusMinutes(refreshTokenExpiredMin);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setAccountId(accountId);
        refreshToken.setExpiresDt(expiresDt);
        refreshToken.setRefreshToken(JwtUtils.createRefreshToken(userDetails, expiresDt));

        refreshTokenRepository.save(refreshToken);

        log.debug("refresh token new issue : {},{}",accountId, refreshToken.getRefreshToken());
        return new TokenIssueVo(refreshToken.getRefreshToken(), expiresDt);
    }

    public TokenIssueVo issueAccessTokenFromRefreshToken(String refreshToken){
        if(!StringUtils.isEmpty(refreshToken)){
            Account tokenAccount = JwtUtils.parseAccount(refreshToken);
            if(tokenAccount == null || StringUtils.isEmpty(tokenAccount.getAccountId())){
                throw new UsernameNotFoundException(ErrorMessageProperties.ACCOUNT_NOT_FOUND);
            }

            Account foundAccount = accountRepository.findByAccountId(tokenAccount.getAccountId());
            if(foundAccount == null){
                throw new UsernameNotFoundException(ErrorMessageProperties.ACCOUNT_NOT_FOUND);
            }

            List<Account.AccountAuthority> accountRoleList = foundAccount.getRoles();
            if(CollectionUtils.isEmpty(accountRoleList)){
                log.warn("account role is empty : {}",foundAccount.getAccountId());
                throw AuthorityException.of(ErrorMessageProperties.EMPTY_AUTHORITIES);
            }

            AccountUserDetails accountUserDetails = new AccountUserDetails(foundAccount);
            LocalDateTime expiresDt = LocalDateTime.now().plusMinutes(accessTokenExpiredMin);

            return new TokenIssueVo(JwtUtils.createAccessToken(accountUserDetails, expiresDt),expiresDt);
        }else{
            throw new BadRequestException();
        }
    }

    @Transactional
    public void expireAllRefreshTokenWhereAccountId(String accountId){
        log.debug("expire all refresh toke : {}",accountId);
        refreshTokenRepository.updateExpiresDatetimeByUserId(accountId, LocalDateTime.now());
    }
}
