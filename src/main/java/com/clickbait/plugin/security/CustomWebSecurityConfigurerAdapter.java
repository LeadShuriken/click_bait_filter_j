package com.clickbait.plugin.security;

import com.clickbait.plugin.CCUserDetailsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

@Configuration
@EnableWebSecurity
public class CustomWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    @Value("${authentication}")
    private Boolean shouldAuthenticate;

    @Value("${api.endpoints.authentication}")
    private String authenticationEndpoint;

    @Value("${api.endpoints.processing}")
    private String processing;

    @Autowired
    private EncryptionHandlers encryptionHandlers;

    @Autowired
    private CCUserDetailsService applicationUserService;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.httpBasic().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        if (shouldAuthenticate) {
            http.addFilterBefore(
                    new AuthenticationFilter(encryptionHandlers, applicationUserService, authenticationEndpoint),
                    AbstractPreAuthenticatedProcessingFilter.class)
                    .addFilterAfter(new AuthenticateJwtFilter(encryptionHandlers, applicationUserService, processing),
                            AbstractPreAuthenticatedProcessingFilter.class)
                    .authorizeRequests().anyRequest().authenticated();
        } else {
            http.authorizeRequests().anyRequest().permitAll();
        }

        http.csrf() // DEV ONLY
                .disable() // DEV ONLY
                .cors().disable();
    }

    // @Override
    // public void addInterceptors(InterceptorRegistry registry)
    // {
    // // registry.addInterceptor(new CustomInterceptor());
    // }

    // @Primary
    // @Bean
    // public CustomRestExceptionHandler restResponseEntityExceptionHandler() {
    // return new CustomRestExceptionHandler();
    // }

    // @Bean
    // public AccessDeniedHandler accessDeniedHandler() {
    // return new CustomAccessDeniedHandler();
    // }

    // @Bean
    // public AuthenticationFailureHandler authenticationFailureHandler() {
    // return new CustomAuthenticationFailureHandler();
    // }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // @Override
    // protected void configure(final AuthenticationManagerBuilder authentication)
    // throws Exception {
    // authentication.userDetailsService(applicationUserService);
    // }
}
