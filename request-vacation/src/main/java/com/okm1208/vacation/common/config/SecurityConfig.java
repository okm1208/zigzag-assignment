package com.okm1208.vacation.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author Nick (okm1208@gmail.com)
 * @created 2021-05-14
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    final List<String> permitAllPatterns = new ArrayList<>(
            Arrays.asList(
                    "/error",
                    "/auth/**"
            ));
    final List<String> authorizationPatterns = new ArrayList<>(
            Arrays.asList(
                    "/register"
            ));
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .authorizeRequests()
                .antMatchers(permitAllPatterns.toArray(new String[0])).permitAll()
                .antMatchers(authorizationPatterns.toArray(new String[0])).hasRole("USER")
                .anyRequest().permitAll()
            .and()
            .   formLogin().disable();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }


}
