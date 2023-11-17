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
                .antMatchers("/","/showFile/**","/v1/showFile/**","/v1/upload",
                        "/user-register/**","/swagger-ui/**","/swagger-ui.html","/v2/api-docs/**",
                        "/swagger-resources/**","/user-login/**", "/forgot-password/**",
                        "/oauth/authorize**", "/login**", "/error**","/tester/**")
                .permitAll()
                .antMatchers("/v1/karyawan/**","/v1/taining/**","/v1/rekening/**",
                        "/v1/karyawan-training/**","/v1/karyawan/**","/v1/upload")
                .hasAnyAuthority("ROLE_ADMIN","ROLE_SUPERUSER")
                .antMatchers("/v1/karyawan/list","/v1/taining/list","/v1/rekening/list",
                        "/v1/karyawan-training/list","/v1/karyawan/list","/v1/upload")
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
