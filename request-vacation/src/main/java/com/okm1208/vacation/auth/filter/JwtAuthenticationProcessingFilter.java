package com.okm1208.vacation.auth.filter;

import com.okm1208.vacation.auth.model.JwtAuthenticationToken;
import com.okm1208.vacation.common.config.JwtProperties;
import com.okm1208.vacation.common.exception.AuthorityException;
import com.okm1208.vacation.common.msg.ErrorMessageProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Nick ( okm1208@gmail.com )
 * @created 2021-05-15
 */
@Slf4j
public class JwtAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {

    public JwtAuthenticationProcessingFilter(RequestMatcher matcher ){
        super(matcher);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        String accessToken = httpServletRequest.getHeader(JwtProperties.ACCESS_TOKEN_HEADER_NAME);
        if(StringUtils.isEmpty(accessToken)){
            throw AuthorityException.of(ErrorMessageProperties.REQUIRED_TOKEN);
        }
        return getAuthenticationManager().authenticate(new JwtAuthenticationToken(accessToken));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authResult);
        SecurityContextHolder.setContext(context);
        chain.doFilter(request,response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        SecurityContextHolder.clearContext();
        throw failed;
    }
}
