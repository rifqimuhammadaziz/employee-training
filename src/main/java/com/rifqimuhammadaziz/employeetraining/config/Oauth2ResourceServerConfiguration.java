package com.rifqimuhammadaziz.employeetraining.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(securedEnabled = true)
public class Oauth2ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        super.configure(resources);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf()
                .disable()
                .antMatcher("/**")
                .authorizeRequests()
                .antMatchers(
                        "/",
                        "/showFile/**",
                        "/v1/showFile/**",
                        "/v1/upload",
                        "/user-register/**",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-ui.html",
                        "/v2/api-docs/**",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/configuration/**",
                        "/actuator",
                        "/user-login/**",
                        "/forgot-password/**",
                        "/oauth/token",
                        "/oauth/authorize**",
                        "/login**",
                        "/error**",
                        "/tester/**",
                        "/api/v1/user-register/register",
                        "/web/user-register/**",
                        "/api/v1/user-register/register-confirm-otp/**",
                        "/api/v1/user-register/send-otp",
                        "/api/v1/user-login/login"
                )
                .permitAll()
                .antMatchers(
                        "/api/v1/karyawans/**",
                        "/api/v1/trainings/**",
                        "/api/v1/rekenings/**",
                        "/api/v1/karyawan-trainings/**",
                        "/api/v1/upload"
                )
                .hasAnyAuthority("ROLE_ADMIN","ROLE_SUPERUSER")
                .antMatchers(
                        "/api/v1/karyawans",
                        "/api/v1/trainings",
                        "/api/v1/rekenings",
                        "/api/v1/karyawan-trainings",
                        "/api/v1/upload"
                )
                .hasAnyAuthority("ROLE_USER")
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .permitAll();
    }
}
