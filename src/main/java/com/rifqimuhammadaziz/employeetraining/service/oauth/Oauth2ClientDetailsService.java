package com.rifqimuhammadaziz.employeetraining.service.oauth;

import com.rifqimuhammadaziz.employeetraining.repository.oauth.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@SuppressWarnings("deprecation")
public class Oauth2ClientDetailsService implements ClientDetailsService {

    private final ClientRepository clientRepository;

    @Override
    public ClientDetails loadClientByClientId(String s) throws ClientRegistrationException {
        ClientDetails clientDetails = clientRepository.findOneByClientId(s);
        if (clientDetails == null) {
            throw new ClientRegistrationException("Client not found");
        }
        return clientDetails;
    }

    @CacheEvict("oauth_client_id")
    public void clearCache(String s) {
        System.out.println("ini cache oauth_client_id= "+s);
    }
}
