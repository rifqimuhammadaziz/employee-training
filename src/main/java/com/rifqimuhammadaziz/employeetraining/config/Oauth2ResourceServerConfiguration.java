package com.rifqimuhammadaziz.employeetraining.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
@SuppressWarnings("deprecation")
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
                .authorizeRequests().antMatchers("/",
                        "/user-register/**", "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-ui.html",
                        "/swagger-resources/**",
                        "/configuration/security",
                        "/configuration/**",
                        "/webjars/**",
                        "/swagger",
                        "/swagger-config/**",
                        "/actuator/**", "/api/v1/forget-password/**", "/oauth/token",
                        "/oauth/authorize**", "/login**", "/error**", "/tester/**", "/api/v1/user-register/register",
                        "/web/user-register/**", "/api/v1/user-register/register-confirm-otp/**", "/api/v1/user-register/send-otp",
                        "/api/v1/user-login/login", "/api/v1/user-login/signin-google").permitAll()
                .antMatchers("/api/v1/karyawans/**", "/api/v1/karyawan-trainings/**", "/api/v1/rekenings/**",
                        "/api/v1/trainings/**", "/api/v1/file/upload", "/api/v1/file/show-file/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_SUPERUSER")
                .antMatchers("/api/v1/karyawans/**", "/api/v1/karyawan-trainings/**", "/api/v1/rekenings/**",
                        "/api/v1/trainings/**", "/api/v1/file/upload", "/api/v1/file/show-file/**")
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
