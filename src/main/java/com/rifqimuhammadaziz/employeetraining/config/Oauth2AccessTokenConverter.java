package com.rifqimuhammadaziz.employeetraining.config;

import com.rifqimuhammadaziz.employeetraining.repository.oauth.UserRepository;
import com.rifqimuhammadaziz.employeetraining.service.oauth.Oauth2UserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

@Component
@SuppressWarnings("unchecked")
@RequiredArgsConstructor
public class Oauth2AccessTokenConverter extends DefaultAccessTokenConverter {

    private UserRepository userRepository;
    private Oauth2UserDetailsService userDetailsService;

    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
        final OAuth2Authentication auth = super.extractAuthentication(map);
        final UserDetails user = userDetailsService.loadUserByUsername((String) auth.getPrincipal());
        return new OAuth2Authentication(auth.getOAuth2Request(), auth.getUserAuthentication()) {
            @Override
            public Collection<GrantedAuthority> getAuthorities() {
                if (user != null) {
                    return (Collection<GrantedAuthority>) user.getAuthorities();
                }
                return auth.getAuthorities();
            }
        };
    }
}
