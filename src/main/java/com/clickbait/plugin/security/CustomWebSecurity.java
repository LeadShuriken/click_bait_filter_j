package com.clickbait.plugin.security;

import org.springframework.context.annotation.Bean;
import com.clickbait.plugin.services.CustomUserDetailsService;
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
public class CustomWebSecurity extends WebSecurityConfigurerAdapter {

    @Value("${spring.profiles.active}")
    private String activeSpringProfile;

    @Value("${authentication}")
    private Boolean shouldAuthenticate;

    @Value("${api.endpoints.authentication}")
    private String authenticationEndpoint;

    @Value("${api.endpoints.processing}")
    private String processing;

    @Autowired
    private EncryptionHandlers encryptionHandlers;

    @Autowired
    private CustomUserDetailsService applicationUserService;

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        Boolean isProd = activeSpringProfile.equals("prod");
        http.httpBasic().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        if (isProd || shouldAuthenticate) {
            http.addFilterBefore(
                    new AuthenticationFilter(encryptionHandlers, applicationUserService, authenticationEndpoint),
                    AbstractPreAuthenticatedProcessingFilter.class)
                    .addFilterAfter(new AuthenticateJwtFilter(encryptionHandlers, applicationUserService, processing),
                            AbstractPreAuthenticatedProcessingFilter.class)
                    .authorizeRequests().anyRequest().authenticated();
        } else {
            http.authorizeRequests().anyRequest().permitAll();
        }

        if (!isProd) {
            http.csrf().disable().cors().disable();
        }
    }

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
