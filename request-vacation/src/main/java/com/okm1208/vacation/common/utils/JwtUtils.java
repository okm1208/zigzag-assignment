package com.okm1208.vacation.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.okm1208.vacation.auth.userdetails.AccountUserDetails;
import com.okm1208.vacation.common.config.JwtProperties;
import com.okm1208.vacation.common.entity.Account;
import com.okm1208.vacation.common.exception.AuthenticationException;
import com.okm1208.vacation.common.exception.AuthorityException;
import com.okm1208.vacation.common.exception.CustomBusinessException;
import com.okm1208.vacation.common.msg.ErrorMessageProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
public class JwtUtils {
    public static String createAccessToken(UserDetails userDetails, LocalDateTime expireDt ){
        return createToken(userDetails,expireDt);
    }
    public static String createRefreshToken(UserDetails userDetails, LocalDateTime expireDt){
        return createToken(userDetails, expireDt);
    }

    public static DecodedJWT tokenToJwt(String token){
        verify(token);
        return JWT.decode(token);
    }

    public static void verify(String token)  throws CustomBusinessException {
        try{
            JWTVerifier verifier = JWT.require(getAlgorithm()).build();
            verifier.verify(token);
        }catch(TokenExpiredException e){
            throw new AuthenticationException(ErrorMessageProperties.EXPIRED_ACCESS_TOKEN);
        }catch(JWTVerificationException | IllegalArgumentException e){
            throw new AuthenticationException( ErrorMessageProperties.INVALID_TOKEN );
        }
    }
    public static Account parseAccount(String token){
        DecodedJWT decodedJWT = tokenToJwt(token);
        Account account = new Account();
        account.setAccountId(decodedJWT.getClaim("accountId").asString());
        account.setActive(decodedJWT.getClaim("active").asBoolean());
        List<String> roleList = decodedJWT.getClaim("authorities").asList(String.class);
        if(!CollectionUtils.isEmpty(roleList)){
            account.setRoles(roleList.stream().map(role->Account.AccountAuthority.findByName(role)).collect(Collectors.toList()));
        }
        return account;
    }
    private static String createToken(UserDetails userDetails, LocalDateTime expireDt){
       try{
           AccountUserDetails customUserDetails = (AccountUserDetails)userDetails;

           String accountId = customUserDetails.getUsername();
           String[] authoritiesArray = null;

           if(userDetails.getAuthorities() != null){
               authoritiesArray = userDetails.getAuthorities()
                       .stream().map(auth->auth.getAuthority()).toArray(String[]::new);

               return JWT.create()
                       .withIssuer(JwtProperties.ISSUER)
                       .withClaim("accountId",accountId)
                       .withClaim("active",true)
                       .withArrayClaim("authorities", authoritiesArray)
                       .withExpiresAt(Date.from(expireDt.toInstant(ZoneOffset.ofHours(9))))
                       .sign(getAlgorithm());
           }else{
               throw new AuthorityException();
           }
       }catch (JWTCreationException createEx){
           throw AuthorityException.of(ErrorMessageProperties.CREATE_TOKEN_FAILED);
       }
    }

    private static Algorithm getAlgorithm(){
        try{
            return Algorithm.HMAC256(JwtProperties.TOKEN_SECRET);
        }catch(IllegalArgumentException e){
            return Algorithm.none();
        }
    }
}
