package com.rifqimuhammadaziz.employeetraining.config;

import com.rifqimuhammadaziz.employeetraining.service.oauth.Oauth2ClientDetailsService;
import com.rifqimuhammadaziz.employeetraining.service.oauth.Oauth2UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class Oauth2AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    private Oauth2ClientDetailsService clientDetailsService;
    private Oauth2UserDetailsService userDetailsService;
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private AccessTokenConverter accessTokenConverter;
    private TokenStore tokenStore;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer server) throws Exception {
        server.allowFormAuthenticationForClients()
                .passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(clientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStore)
                .accessTokenConverter(accessTokenConverter)
                .userDetailsService(userDetailsService);
    }
}
